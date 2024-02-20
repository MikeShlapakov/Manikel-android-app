package com.example.myapplication;

import android.Manifest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.ref.Cleaner;

public class SignupPage extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private EditText UsernameEditText, PasswordEditText, ConfirmPasswordEditText, NicknameEditText;
    private TextView passwordStrengthTextView;
    private ImageView imageViewProfilePicture;
    private static final int REQUEST_PERMISSIONS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        UsernameEditText = findViewById(R.id.UsernameEditText);
        PasswordEditText = findViewById(R.id.PasswordEditText);
        ConfirmPasswordEditText = findViewById(R.id.ConfirmPasswordEditText);
        passwordStrengthTextView = findViewById(R.id.passwordStrengthTextView);
        NicknameEditText = findViewById(R.id.NicknameEditText);
        imageViewProfilePicture = findViewById(R.id.profilePicture);

        Button btnSelectProfilePicture = findViewById(R.id.btnSelectProfilePicture);

        requestPermissions();

        PasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                checkPasswordStrength(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnSelectProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceDialog();
            }
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openCamera();
                                break;
                            case 1:
                                openGallery();
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        }
    }

    private void checkPasswordStrength(String password) {
        // Check if the password has at least 8 characters and contains numbers, letters, and special characters
        if (password.length() >= 8 && containsNumbers(password) && containsLetters(password) && containsSpecialCharacters(password)) {
            passwordStrengthTextView.setText("Strong Password!");
        } else {
            passwordStrengthTextView.setText("Password should have at least 8 characters. Including letters, numbers, and special characters.");
        }
    }

    private boolean containsNumbers(String s) {
        return s.matches(".*\\d.*");
    }

    private boolean containsLetters(String s) {
        return s.matches(".*[a-zA-Z].*");
    }

    private boolean containsSpecialCharacters(String s) {
        return !s.matches("[a-zA-Z0-9 ]*");
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void signUp() {
        // Implement your sign-up logic here
        // Retrieve data from EditText itText and the selected profile picture
        String username = UsernameEditText.getText().toString();
        String password = PasswordEditText.getText().toString();
        String confirmPassword = ConfirmPasswordEditText.getText().toString();
        String nickname = NicknameEditText.getText().toString();

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showAlert("Passwords do not match");
            return;
        }

        // You can use the collected data for user registration or validation
        // Implement the logic for your application
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle the captured image from the camera
                if (data != null && data.getExtras() != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    imageViewProfilePicture.setVisibility(View.VISIBLE);
                    imageViewProfilePicture.setImageBitmap(imageBitmap);
                }
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // Handle the selected image from the gallery
                if (data != null && data.getData() != null) {
                    imageViewProfilePicture.setVisibility(View.VISIBLE);
                    imageViewProfilePicture.setImageURI(data.getData());
                }
            }
        }
    }
}