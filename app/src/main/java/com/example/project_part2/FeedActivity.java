package com.example.project_part2;

import static com.example.project_part2.RegisterActivity.UPLOAD_PIC_REQUEST;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_part2.adapters.PostListAdapter;
import com.example.project_part2.entities.Post;
import com.example.project_part2.util.Util;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private ImageView newPostImageView;
    private EditText newPostEditText;
    private List<Post> postList;

    /**
     * sets up the recycler view to display the provided posts
     */
    private void setRecyclerFeed(List<Post> posts) {
        // store the posts list for modification
        this.postList = posts;
        RecyclerView postList = findViewById(R.id.postList);
        PostListAdapter postListAdapter = new PostListAdapter(this);
        postList.setAdapter(postListAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));
        postListAdapter.setPosts(this.postList);
    }

    private void setUserInfoFeed() {
        TextView activeUserInfo = findViewById(R.id.activeUserInfo);
//        String fullName = MainActivity.registeredUser.getFirstName() + " " + MainActivity.registeredUser.getLastName();
        String fullName = MainActivity.registeredUser.getId();
        activeUserInfo.setText(fullName);

        ImageView activeUserPic = findViewById(R.id.activeUserPic);
        // use hard-coded image for pfp in top left
        activeUserPic.setImageURI(MainActivity.registeredUser.getProfilePictureUri());
//        activeUserPic.setImageResource(R.drawable.ddog1);;

//        try {
//            activeUserPic.setImageURI((Util.ReadPfpUriFromJson(FeedActivity.this)));
//        } catch (JSONException | IOException e) {
//        throw new RuntimeException(e);
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_view);

        // get the sample posts from the .json
        List<Post> posts;
        try {
            Context context = FeedActivity.this.getApplicationContext();
            posts = Util.samplePostList(context);
        } catch (JSONException e) {
            throw new RuntimeException("json parse failed");
        } catch (IOException e) {
            throw new RuntimeException("IO failed");
        }

//        // set the dark mode button
//        SwitchMaterial darkModeSwitch = findViewById(R.id.darkModeSwitch);
//        darkModeSwitch.setChecked(isDarkMode());
//
//        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            }
//        });

        SwitchMaterial darkModeSwitch = findViewById(R.id.darkModeSwitch);

        darkModeSwitch.setOnCheckedChangeListener(null);

        darkModeSwitch.setChecked(isDarkMode());

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });


        // set the recycler with the provided posts
        setRecyclerFeed(posts);

        // set the user info display
        setUserInfoFeed();
    }

    /**
     * checks if the app is in dark mode
     * @return true if the app is in dark mode, false otherwise
     */
    private boolean isDarkMode() {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                return true;
            case AppCompatDelegate.MODE_NIGHT_NO:
                return false;
            default:    // defaults to light mode
                return false;
        }
    }

    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addPost(View view) {
        showAddDialog();
    }

    public void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.add_post_layout, null);

        newPostEditText = dialogView.findViewById(R.id.postText);
        newPostImageView = dialogView.findViewById(R.id.postPicture);

        dialogBuilder.setView(dialogView)
                .setTitle("Add Post")
                .setPositiveButton("Add", (dialog, which) -> {
                    Context context = ((AlertDialog) dialog).getContext();
                    String postText = newPostEditText.getText().toString().trim();
                    String imageUriString = newPostImageView.getTag().toString().trim();
                    // don't allow empty posts
                    if (postText.isEmpty()) {
                        // Show an error message with Toast
                        Toast.makeText(context, "Post cannot be empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        Uri imageUri = Uri.parse(imageUriString);
                        Post newPost = new Post(postText, imageUri, MainActivity.registeredUser);
                         postList.add(newPost);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
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
                String fileName = "post_pic";
                File internalStorageDir = getFilesDir();
                File imageFile = new File(internalStorageDir, fileName);
                try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                    // save the image to the desired location
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    Uri imagePath = Uri.parse(imageFile.getAbsolutePath());
                    newPostImageView.setImageURI(null); // clear image view cache
                    newPostImageView.setImageURI(imagePath);
                    // set the tag if the image view to the image Uri for easy access across app
                    newPostImageView.setTag(imagePath.toString());

                } catch (IOException e){
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Error saving image. Please try again.", Toast.LENGTH_SHORT).show());
                }
            } else {
                // get the new image URI
                Uri newImageUri = data.getData();
                // update the imageView to display the selected image
                newPostImageView.setImageURI(newImageUri);
                newPostImageView.setTag(newImageUri.toString());
            }
        }
    }
    public void sharePost(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        // the post URL is stored as TAG in the timeStamp TextView
        String postUrl = findViewById(R.id.tvTimeStamp).getTag().toString();
        shareIntent.putExtra(Intent.EXTRA_TEXT, postUrl);
        startActivity(Intent.createChooser(shareIntent, "Share with"));
    }
}

