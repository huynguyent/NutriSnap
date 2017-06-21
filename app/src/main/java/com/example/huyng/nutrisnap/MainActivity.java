package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.content.Intent;
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

        switch(requestCode) {
            case SELECT_PHOTO: {
                Toast.makeText(this, "Photo selected!", Toast.LENGTH_LONG).show();
            }
            case IMAGE_CAPTURE: {
                Toast.makeText(this, "Image captured!", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void galleryBtnOnClick(View v) {
        dispatchGalleryIntent();
    }

    public void cameraBtnOnClick(View v) {
        dispatchTakePictureIntent();
    }

    private void dispatchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
        }
    }

}
