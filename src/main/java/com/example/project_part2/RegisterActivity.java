package com.example.project_part2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    // the request code for picking an image
    public final static int UPLOAD_PIC_REQUEST = 1;

    private TextView invalidTextView;
    private EditText passwordEditText;
    private EditText verifyEditText;

    /**
     * the constructor for the Activity.
     * it sets up the EditText variables, and attached them to the their specific watcher
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // identify the invalidInput TextView to display errors in it later
        invalidTextView = findViewById(R.id.invalidInput);

        // attach the specific listener for each EditText
        passwordEditText = findViewById(R.id.passwordRegister);
        passwordEditText.addTextChangedListener(passwordWatcher);
        verifyEditText = findViewById(R.id.verifyPasswordRegister);
        verifyEditText.addTextChangedListener(verifyWatcher);
    }

    /**
     * watches the password field, and validates the field after typing
     */
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

    /**
     * watches the verify field and validates the field after typing
     */
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

    /**
     * this function validates the password in the password EditText
     * @param password the string extracted from the password field
     * @return true if the password is valid:
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
            invalidTextView.setVisibility(View.GONE);
        } else {
            invalidTextView.setVisibility(View.VISIBLE);
            invalidTextView.setText(R.string.invalidPassword);
        }

        return validPassword;
    }

    /**
     * this function checks the verify string is the same as the string in the password textview
     * @param verify the text collected from verify field
     * @return true if the fields is valid, false otherwise
     */
    private boolean validateVerify(String verify) {
        String password = passwordEditText.getText().toString();

        boolean validVerify = verify.equals(password);

        if (validVerify) {
            invalidTextView.setVisibility(View.GONE);
        } else {
            invalidTextView.setVisibility(View.VISIBLE);
            invalidTextView.setText(R.string.invalidVerify);
        }

        return validVerify;
    }

    /**
     * this functions checks the validity of all fields, and registers the user.
     * @param view the view context
     */
    public void register(View view) {
        String verify = verifyEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean validVerify = validateVerify(verify);
        boolean validPassword = validatePassword(password);

        if (validPassword && validVerify) {
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);
        } else {
            invalidTextView.setVisibility(View.VISIBLE);
            invalidTextView.setText(R.string.invalidFields);
        }
    }

    /**
     * this functions declares intent to let user upload an image
     * the result is collected in onActivityResult
     *
     * @param view the view context
     *
     */
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

    /**
     * check the returned image, and use it as the new profile pic
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: add saving image functionality
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
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    Uri imagePath = Uri.parse(imageFile.getAbsolutePath());
                    ImageView profilePicView = findViewById(R.id.profilePicRegister);
                    profilePicView.setImageURI(null); // clear image view cache
                    profilePicView.setImageURI(imagePath);
                    profilePicView.setColorFilter(Color.TRANSPARENT);

                } catch (IOException e){
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Error saving image. Please try again.", Toast.LENGTH_SHORT).show());
                }
            } else {
                // get the new image URI
                Uri newImageUri = data.getData();
                // update the imageView to display the selected image
                ImageView profilePicView = findViewById(R.id.profilePicRegister);
                profilePicView.setImageURI(newImageUri);
                profilePicView.setColorFilter(Color.TRANSPARENT);
            }
        }
    }
}