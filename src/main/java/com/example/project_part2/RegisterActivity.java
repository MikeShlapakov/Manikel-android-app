package com.example.project_part2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_part2.entities.Credentials;
import com.example.project_part2.entities.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // the request code for picking an image
    public final static int UPLOAD_PIC_REQUEST = 1;

    private TextView invalidInputTV;
    private TextView usernameET;
    private EditText passwordET;
    private EditText verifyPasswordET;
    private EditText firstNameET;
    private EditText lastNameET;

    private Uri pic_taken = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // identify the invalidInput TextView to display errors in it later
        invalidInputTV = findViewById(R.id.invalidInput);

        // attach the specific listener for each EditText
        passwordET = findViewById(R.id.passwordInput);
        passwordET.addTextChangedListener(passwordWatcher);

        verifyPasswordET = findViewById(R.id.verifyPasswordInput);
        verifyPasswordET.addTextChangedListener(verifyWatcher);

        usernameET = findViewById(R.id.usernameInput);

        firstNameET = findViewById(R.id.firstNameInput);
        lastNameET = findViewById(R.id.lastNameInput);
    }


    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            validatePassword(s.toString());
        }
    };


    private final TextWatcher verifyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            validateVerify(s.toString());
        }
    };

    /*
     * 1. at least 1 uppercase char
     * 2. at least 1 lowercase char
     * 3. at least 1 digit
     */
    private boolean validatePassword(String password) {
        //check at least one uppercase char
        boolean containsUppercase = !password.equals(password.toLowerCase());
        // check at least one lowercase char
        boolean containsLowercase = !password.equals(password.toUpperCase());
        // check at least one digit
        boolean containsDigit = Pattern.compile("[0-9]").matcher(password).find();
        boolean has8Chars = password.length() >= 8;

        boolean validPassword = containsUppercase && containsLowercase && containsDigit && has8Chars;

        if (validPassword) {
            invalidInputTV.setVisibility(View.GONE);
        } else {
            invalidInputTV.setVisibility(View.VISIBLE);
            invalidInputTV.setText(R.string.invalidPassword);
        }

        return validPassword;
    }


    private boolean validateVerify(String verify) {
        String password = passwordET.getText().toString();

        boolean validVerify = verify.equals(password);

        if (validVerify) {
            invalidInputTV.setVisibility(View.GONE);
        } else {
            invalidInputTV.setVisibility(View.VISIBLE);
            invalidInputTV.setText(R.string.invalidVerify);
        }

        return validVerify;
    }

    public void register(View view) {

        String password = passwordET.getText().toString();
        String secondPassword = verifyPasswordET.getText().toString();

        boolean validVerify = validateVerify(secondPassword);
        boolean validPassword = validatePassword(password);

        // if all is good, register and goto feed
        if (validPassword && validVerify) {

            MainActivity.registeredUser = new User(firstNameET.getText().toString(),
                                                   lastNameET.getText().toString(),
                                                   pic_taken);

            MainActivity.registeredCredentials = new Credentials(usernameET.getText().toString(),
                                                                 passwordET.getText().toString());

            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);

        } else {
            invalidInputTV.setVisibility(View.VISIBLE);
            invalidInputTV.setText(R.string.invalidFields);
        }
    }

    public void uploadPic(View view) {
        // init the gallery upload intent
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
        pickImageIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        // init the camera upload intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create combined Intent
        Intent combinedIntent = new Intent(Intent.ACTION_CHOOSER);
        combinedIntent.putExtra(Intent.EXTRA_INTENT, pickImageIntent); // Gallery option
        combinedIntent.putExtra(Intent.EXTRA_ALTERNATE_INTENTS, new Intent[] {takePictureIntent}); // Camera option
        combinedIntent.putExtra(Intent.EXTRA_TITLE, "Select Image"); // Optional title

        // Launch activity
        startActivityForResult(combinedIntent, UPLOAD_PIC_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the result is the result to the image pick request, and that the result is ok
        if (requestCode == UPLOAD_PIC_REQUEST && resultCode == RESULT_OK) {
            // identify camera upload because data will be null
            if (data.getData() == null) {
                // get the camera pic as a Bitmap
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                // save the Bitmap as image file
                String fileName = "user_profile_pic";
                File internalStorageDir = getFilesDir();
                File imageFile = new File(internalStorageDir, fileName);
                try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                    // save the image to the desired location
                    if (thumbnail != null) {
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    }

                    pic_taken = Uri.parse(imageFile.getAbsolutePath());
                    ImageView profilePicView = findViewById(R.id.pfpInput);
                    profilePicView.setImageURI(null); // clear image view cache

                } catch (IOException e){
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Error saving image. Please try again.", Toast.LENGTH_SHORT).show());
                }
            } else {
                // get the new image URI
                pic_taken = data.getData();
            }


            // update the imageView to display the selected image
            ImageView profilePicView = findViewById(R.id.pfpInput);
            profilePicView.setImageURI(pic_taken);
            profilePicView.setColorFilter(Color.TRANSPARENT);

        }
    }
}