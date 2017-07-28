package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {
    static final int SELECT_PHOTO = 1;
    static final int  IMAGE_CAPTURE = 2;
    private static final String FILE_PROVIDER_ATHORITY = "com.example.huyng.nutrisnap.fileprovider";

    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Intent resultIntent;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PHOTO:
                    if (intent != null) {
                        // Get the URI of the selected image and pass it to resultItent
                        Uri selectedImage = intent.getData();
                        resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                        resultIntent.putExtra("selectedImage", selectedImage.toString());
                        startActivity(resultIntent);
                    }
                    break;

                case IMAGE_CAPTURE:
                    // Get the URI of the temporary file and pass it to resultIntent
                    resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                    resultIntent.putExtra("capturedImage", mCurrentPhotoPath);
                    startActivity(resultIntent);

                    break;
            }
        }
    }

    // Dispatch gallery intent
    public void galleryBtnOnClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    // Dispatch camera intent
    public void cameraBtnOnClick(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Save image to a temporary file
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "There was an error while capturing photo", Toast.LENGTH_SHORT).show();
            }
            // Send file URI to camera intent
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, FILE_PROVIDER_ATHORITY, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
            }
        }
    }

    // Create a temporary file to store captured photo
    private File createImageFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir + "/temp.jpg");
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
