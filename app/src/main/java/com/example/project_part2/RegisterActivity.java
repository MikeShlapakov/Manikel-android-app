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
import androidx.lifecycle.MutableLiveData;

import com.example.project_part2.apis.UserAPI;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.ImageUtil;
import com.example.project_part2.util.MyApplication;

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

    private UserAPI userAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userAPI = new UserAPI();

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

        if (validPassword && validVerify) {

            String picString;

          if (pic_taken == null) {
              picString = "data:image/webp;base64,UklGRkwFAABXRUJQVlA4TD8FAAAvd8AdAKflIJIkRershTj/Ys4ePFpA0LZtnA7j2R4cg7aNJKV7/IEdpS+Z/3XIq0AioEUBSkkgESLEIAWkeRcEQsYEIi3CcQ+y7yO0lFoiQikNGmmdVi9BKK190rS8TygNAkJkpLEQ1BAhaUppEJhThHQgkVEiYyk1CeByXGb1EJBASJrLRRhekYYIiDREBBJpIBEBREgaovlr3EWj0XTHn8xH5GPz3/lvEQWFW9sMSVLV2LZt27Zt255o27ZtK2d/4fSbWRmR31rxZUT0H4IjSVKjqL0Zo8HWNuOGFwT+mZ5Z63afv/fyk2/usV4361dj7o5HIkTYn/srwAsvCFfxF1I348g34TK+nkHZ0O2fhUQ+81CyRp8XkmFN1Jy7xkFcfnVjR5/xdTXHCYfcnUPSorfCJrq4zXIMF0cbW94uImj2G4HCi/qskDGaBebNbHqPuGNQRrflKpwuMHo0MUPOGVRouY4RGD2Eli22YTWWRKpto7aQMu0j0pZUNPrIlBwKsVV64yFC5mNRTrOkk4almuk4KwZFdMnTEWYwZ+mUDMe/KfrTbCq2gqheFXo12ErFHft3dd80EZMFaFHD4Byn0LASxPiKwmANDdtArioa7KDhFKhSpQqcoeEWqFfFgJs0PLGPVDnwEQ2/gG5VGLym4YcXXtHwGLSqosEDGm57MeQ6DSdApSqV4CSl01G2KtlgGw3LcbE+oAbjEn4FDRMNpEkNxjlOCNDITZCvBtvKJBGbcXmlpHIwLrA2k1k9f1N3JtS4oKSzgj4OwjrkdWhwnNB7wS9mMKnSO+FUM0hTek+4D/89cmQZnN1eJmTCezyUCjnlOLf3EwKUshGFSX2wRhsCpASPCdtG1w9gIzDHggFaGXFNYFLb3eFUg66NCFDLlGfCNirHReFiowV6NiVATHD2sl0fhL2zktvguBvWueHGlg+7ls0OUhq26uhzESqRWeU1TR0+6/IsI0Ll+dFVI2iYd/CdUJ53rAm0X08Lj+Iv9NaY/d+EZ/E1e2jxC+NpfO2Z9V+Nk+T8yobWjs6faS5MDHOm/cLmzp/paG2o5GThlK/rvTHsgMPEw9MqQ9WK3tqizOSEGE4wuqjWD1VDKtPChT0Hhnlg7CWH5zG321Ic1g6vw6Wxyg2/amwyOywP0qGNLVdZtcMCRTdZHoWjDeawYpsESuqyPItOMhhWask327A+y8MY26hvrNB0W0XOtjxOtq1aT1fnDE40ZcBrrI3iLssSnGZMt+95OAaPQNWfgjewSLX6BNLC4EZQjaUGFFgkUoDHsFSNK7ii6KWh1wBW2W0us4jEKOxIb8XH9FOhWV1H+o7TdkqbtQJTDWimoxmPQ8tbg039ATpYq+pI77a94yQUA/bIO48XxijReE1a3kPQRgkDLV+cv4N+SjT4zrJG4jnBIhU8N4yQ7ijgqooWBuOke1UglpZYMPmvQwOF0cJVwhLqrM70cOEqnN5JQqFwHd0Ub1xHD1AQbepdN+hyRGyP5So9saadAmPcn9/SRabvMto0/ZkSbeo8GfLr3dGvtl79AaqTk/AmEVp1MgaMlzUKrwXS2lEEGCm9fsYO0gAlA9hNkm81PAPtlLSDpwFp2A6to6QOXJS3H+RRqpMa7Je3DndFaUexYJ28mTjjVjr1qhXnMjMgn0egiI4idTcK7bS9QlR+YZzbThUW4JSrqNTJKjyKBQpvm4jpo7GjvhiVN1asxqemhIZSPIbViu7av48Nyk4KOrE9eV/Vnf5r8clJ7PdefyIewVplF3sv224p975OZuD8ddANBgA=";
          } else {  picString = ImageUtil.imageViewToBase64(findViewById(R.id.pfp), MyApplication.context); }


            User newUser = new User(firstNameET.getText().toString() + lastNameET.getText().toString(),
                                                   usernameET.getText().toString(),
                                                   passwordET.getText().toString(),
                                                   picString);

            // set registeredUser to the new user
            MyApplication.registeredUser.setValue(newUser);

            // add new user to local db, via registeredUser
            MyApplication.insertRegisteredUserToLocalDB();

            // update MyApplication.registeredUser asynchronously, will give it its id
            userAPI.createUser(newUser.getDisplayName(), newUser.username(), newUser.password(), newUser.getPfp());

            Intent intent = new Intent(this, MainActivity.class);
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