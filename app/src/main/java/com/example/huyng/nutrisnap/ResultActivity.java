package com.example.huyng.nutrisnap;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // Display food image
        ImageView foodImageView = (ImageView) findViewById(R.id.food_image);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Image was selected from gallery
            if (extras.containsKey("selectedImage")) {
                Uri imageUri = Uri.parse(extras.getString("selectedImage"));
                foodImageView.setImageURI(imageUri);
            }
            // Image was captured by camera
            else if (extras.containsKey("capturedImage")) {
                Uri imageUri = Uri.parse(extras.getString("capturedImage"));
                Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                foodImageView.setImageURI(imageUri);

            }
        }

    }
}