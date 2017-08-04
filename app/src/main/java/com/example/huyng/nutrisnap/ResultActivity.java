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
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.huyng.nutrisnap.classifier.Classifier;
import com.example.huyng.nutrisnap.classifier.TensorFlowImageClassifier;

import java.io.InputStream;
import java.util.ArrayList;
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

    private static final boolean MAINTAIN_ASPECT = true;

    RecyclerAdapter adapter;
    ArrayList<FoodInfo> foodInfos;


    public Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Display food image
        Bundle extras = getIntent().getExtras();
        Uri imageUri = null;

        if (extras != null) {
            //  Get image URI from intent
            if (extras.containsKey("selectedImage"))
                imageUri = Uri.parse("file://" + extras.getString("selectedImage"));
            else if (extras.containsKey("capturedImage"))
                imageUri = Uri.parse("file://" + extras.getString("capturedImage"));

            try {
                InputStream image_stream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap= BitmapFactory.decodeStream(image_stream );

                // Crop bitmap for TensorFlow Classifier
                Bitmap croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Config.ARGB_8888);
                Matrix transformMatrix = getTransformationMatrix(
                                bitmap.getWidth(), bitmap.getHeight(),
                                INPUT_SIZE, INPUT_SIZE,
                                0, MAINTAIN_ASPECT);
                final Canvas canvas = new Canvas(croppedBitmap);
                canvas.drawBitmap(bitmap, transformMatrix, null);

                // Classify image in background
                new ClassifyImageTask().execute(croppedBitmap);

                // Set up RecyclerView to display results
                RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
                LinearLayoutManager llm = new LinearLayoutManager(this);
                rv.setLayoutManager(llm);

                // Initialize RecyclerAdapter
                foodInfos = new ArrayList<FoodInfo>();
                adapter = new RecyclerAdapter(bitmap, true, foodInfos);
                rv.setAdapter(adapter);

            } catch (Exception ex) {
                Toast.makeText(this, "There was a problem", Toast.LENGTH_SHORT).show();
                Log.e(TAG, ex.toString());
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Calculate transformation matrix so that an image can be
     * cropped and used by TensorFlow Classifier
     */
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
            // Hide loading panel
            View loadingPanel = findViewById(R.id.loading_panel);
            loadingPanel.setVisibility(View.GONE);

            // Display results
            for (Object res: results) {
                Classifier.Recognition recognition =(Classifier.Recognition)res;
                foodInfos.add(new FoodInfo(recognition.getTitle(),0,0,0));
            }

            adapter.notifyDataSetChanged();
        }
    }

}
