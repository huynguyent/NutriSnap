package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ResultActivity extends Activity {
    private static final int INPUT_SIZE = 299;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final String INPUT_NAME = "Mul:0";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/rounded_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/retrained_labels.txt";

    private static final boolean SAVE_PREVIEW_BITMAP = false;

    private static final boolean MAINTAIN_ASPECT = true;

    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Display food image
        ImageView foodImageView = (ImageView) findViewById(R.id.food_image);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Uri imageUri = null;
            //  Get image URI from intent
            if (extras.containsKey("selectedImage"))
                imageUri = Uri.parse("file://" + extras.getString("selectedImage"));
            else if (extras.containsKey("capturedImage"))
                imageUri = Uri.parse("file://" + extras.getString("capturedImage"));

            try {
                // Display image
                InputStream image_stream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap= BitmapFactory.decodeStream(image_stream );
                foodImageView.setImageBitmap(bitmap);

                // Crop bitmap for classifier
                Bitmap croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Config.ARGB_8888);
                Matrix transformMatrix = getTransformationMatrix(
                                bitmap.getWidth(), bitmap.getHeight(),
                                INPUT_SIZE, INPUT_SIZE,
                                0, MAINTAIN_ASPECT);
                final Canvas canvas = new Canvas(croppedBitmap);
                canvas.drawBitmap(bitmap, transformMatrix, null);

                // Classify image in background
                new ClassifyImageTask().execute(croppedBitmap);

            } catch (Exception ex) {
                Toast.makeText(this, "There was a problem", Toast.LENGTH_SHORT).show();
                Log.i(TAG, imageUri.toString());
                Log.e(TAG, ex.toString());
            }

        }

    }


    private Matrix getTransformationMatrix(
            final int srcWidth,
            final int srcHeight,
            final int dstWidth,
            final int dstHeight,
            final int applyRotation,
            final boolean maintainAspectRatio) {
        final Matrix matrix = new Matrix();

        if (applyRotation != 0) {
            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

            // Rotate around origin.
            matrix.postRotate(applyRotation);
        }

        // Account for the already applied rotation, if any, and then determine how
        // much scaling is needed for each axis.
        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;

        final int inWidth = transpose ? srcHeight : srcWidth;
        final int inHeight = transpose ? srcWidth : srcHeight;

        // Apply scaling if necessary.
        if (inWidth != dstWidth || inHeight != dstHeight) {
            final float scaleFactorX = dstWidth / (float) inWidth;
            final float scaleFactorY = dstHeight / (float) inHeight;

            if (maintainAspectRatio) {
                // Scale by minimum factor so that dst is filled completely while
                // maintaining the aspect ratio. Some image may fall off the edge.
                final float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                // Scale exactly to fill dst from src.
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }

        if (applyRotation != 0) {
            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
        }

        return matrix;
    }

    public Context getActivityContext() {
        return this;
    }

    /*
     * Classify image in background
     */
    private class ClassifyImageTask extends AsyncTask<Bitmap, Void, List> {
        @Override
        protected List doInBackground(Bitmap... bitmap) {
            // Create image classifier
            Classifier classifier =
                    TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);

            // Classify image
            List<Classifier.Recognition> results = classifier.recognizeImage(bitmap[0]);

            return results;
        }

        @Override
        protected void onPostExecute(List results) {
            Toast.makeText(getActivityContext(), results.get(0).toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
