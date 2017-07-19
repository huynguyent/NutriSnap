package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    static final int SELECT_PHOTO = 1;
    static final int  IMAGE_CAPTURE = 2;

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
                        Toast.makeText(this, "Photo selected!", Toast.LENGTH_SHORT).show();
                        // Get the URI of the selected image and pass it to resultItent
                        Uri selectedImage = intent.getData();
                        resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                        resultIntent.putExtra("selectedImage", selectedImage.toString());
                        startActivity(resultIntent);
                    }
                    break;
                case IMAGE_CAPTURE:
                    if (intent != null) {
                        Toast.makeText(this, "Image captured!", Toast.LENGTH_SHORT).show();
                        resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                        startActivity(resultIntent);
                    }

                    break;


            }
        }
    }
    public void galleryBtnOnClick(View v) {
        // Dispatch gallery intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    public void cameraBtnOnClick(View v) {
        // Dispatch camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
        }

    }



}
