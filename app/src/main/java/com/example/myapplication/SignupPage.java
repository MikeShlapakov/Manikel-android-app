package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

public class SignupPage extends AppCompatActivity {

//    private ImageView captureIV;
    private ImageView image_taken;
    private Uri image_uri;
    private ActivityResultLauncher<Uri> contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        image_taken.setImageURI(null);
        image_taken.setImageURI();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // link with layout
        setContentView(R.layout.activity_signup_page);
//
        image_taken = findViewById(R.id.image_taken);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(contract.launch(image_uri));
    }

    private Uri setImgURI() {
        Uri img = File(getFilesDir(), "camera_photos.png");
        return FileProvider.getUriForFile(this, "com.example.myapplication.FileProvider", img);
    }
}