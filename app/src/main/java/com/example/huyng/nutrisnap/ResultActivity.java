package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // Display the selected image from gallery
        ImageView foodImageView = (ImageView) findViewById(R.id.food_image);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("selectedImage")) {
            Uri imageUri = Uri.parse(extras.getString("selectedImage"));
            foodImageView.setImageURI(imageUri);
        }

    }
}