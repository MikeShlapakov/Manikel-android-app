package com.example.project_part2;

import static com.example.project_part2.RegisterActivity.UPLOAD_PIC_REQUEST;
import static com.example.project_part2.util.MyApplication.context;
import static com.example.project_part2.util.MyApplication.registeredUser;
import static com.example.project_part2.util.MyApplication.token;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project_part2.adapters.IncomingFriendsAdapter;
import com.example.project_part2.adapters.PostListAdapter;
import com.example.project_part2.apis.PostAPI;
import com.example.project_part2.apis.TokenAPI;
import com.example.project_part2.apis.UserAPI;
import com.example.project_part2.entities.Friend;
import com.example.project_part2.entities.Post;
//import com.example.project_part2.util.Util;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.ImageUtil;
import com.example.project_part2.util.MyApplication;
import com.example.project_part2.viewmodels.PostsViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private ImageView newPostImageView;
    private EditText newPostEditText;
    private PostsViewModel viewModel;
    private PostAPI postAPI;
    private UserAPI userAPI;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Uri img_taken = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_view);

        // init posts repo
        // holds sample posts from the .json at first
        // this also asks for posts from server
        viewModel = new ViewModelProvider(this).get(PostsViewModel.class);
        postAPI = new PostAPI(); userAPI = new UserAPI();


        ImageButton settingsButton = findViewById(R.id.settings);

        MutableLiveData<List<Friend>> incomingFriendRequests = new MutableLiveData<>();
        userAPI.getFriendRequests(incomingFriendRequests);

        // only after response from server the button can be used
        incomingFriendRequests.observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {

                settingsButton.setOnClickListener(item -> {

                    if (incomingFriendRequests.getValue() != null && incomingFriendRequests.getValue().size() > 0) {
                        PopupWindow popupWindow = new PopupWindow(context);
                        ListView listView = new ListView(context);

                        IncomingFriendsAdapter adapter = new IncomingFriendsAdapter(context, incomingFriendRequests.getValue());
                        listView.setAdapter(adapter);

//                    popupWindow.setBackgroundDrawable(AppCompatResources.getDrawable(R.drawable.ddog3));
                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
                        popupWindow.setContentView(listView);
//            popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//            popupWindow.setWidth(findViewById(R.id.topLayout).getWidth());
//                        popupWindow.setWidth(getLayoutWidth(MyApplication.context, R.id.incomingLayout));
                        popupWindow.setWidth(770);
//            popupWindow.setWidth(findViewById(R.id.topLayout).getWidth());
                        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                        popupWindow.setFocusable(true);
                        popupWindow.showAsDropDown(settingsButton);

                    } else {
                        Toast.makeText(MyApplication.context, "don't have any friend requests to show", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        setDarkMode();

        RecyclerView postList = findViewById(R.id.postList);
        PostListAdapter adapter = new PostListAdapter(this);
        postList.setAdapter(adapter);
        postList.setLayoutManager(new LinearLayoutManager(this));

//        if (viewModel.getPosts() != null) {

        // sets the posts
        adapter.setPosts(viewModel.getPosts().getValue());

        // if we change the viewModel posts this should be called
        viewModel.getPosts().observe(this, posts -> {
                adapter.setPosts(posts);
                adapter.notifyDataSetChanged();
        });

        // set the user info display
        setUserInfoFeed();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getPostsFromServer();
                userAPI.updateUser(MyApplication.registeredUser);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void setUserInfoFeed() {
        TextView activeUserInfo = findViewById(R.id.displayName);
        String displayName = MyApplication.registeredUser.getValue().getDisplayName();
        activeUserInfo.setText(displayName);

        ImageView activeUserPic = findViewById(R.id.pfp);
        if (MyApplication.registeredUser.getValue().getPfp().equals("")) {
            activeUserPic.setImageURI(null);
        } else {
            activeUserPic.setImageURI(ImageUtil.decodeBase64ToUri(MyApplication.registeredUser.getValue().getPfp(), context));
        }
    }

    public void setDarkMode() {
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
    }

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

    public void addPost(View view) {
        showAddDialog();
    }

//    public void checkInfo(View view) {
//        // make a dropdown menu with
//        // - add friend (message at the bottom saying friend request sent)
//        //      afterwards button disabled if already sent
//        // - show posts
//    }

    public void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.add_post_layout, null);

        newPostEditText = dialogView.findViewById(R.id.postText);
        newPostImageView = dialogView.findViewById(R.id.postPicture);

        // add post
        dialogBuilder.setView(dialogView)
                .setTitle("Add Post")
                .setPositiveButton("Add", (dialog, which) -> {
                    Context context = ((AlertDialog) dialog).getContext();
                    String content = newPostEditText.getText().toString().trim();

//                    Uri img = Uri.parse(newPostImageView.getAbsolutePath());
//                    String imageString = "data:image/png;base64," + ImageUtil.imageViewToBase64(newPostImageView, MyApplication.context);
                    // don't allow empty posts
                    if (content.isEmpty() || img_taken == null) {
                        // Show an error message with Toast
                        Toast.makeText(context, "Fill all the fields!", Toast.LENGTH_SHORT).show();

                    } else {

//                      Uri imageUri = Uri.parse(imageUriString);
                        Post newPost = new Post(content, ImageUtil.imageViewToBase64(newPostImageView, MyApplication.context), MyApplication.registeredUser.getValue().id(), MyApplication.registeredUser.getValue().getPfp(), MyApplication.registeredUser.getValue().getDisplayName());

                        // send newPost to server
                        postAPI.createPost(newPost, viewModel);
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
                    if (thumbnail != null) {
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    }

                    img_taken = Uri.parse(imageFile.getAbsolutePath());
                    newPostImageView.setImageURI(null); // clear image view cache
                    newPostImageView.setImageURI(img_taken);
                    // set the tag if the image view to the image Uri for easy access across app
                    newPostImageView.setTag(img_taken.toString());

                } catch (IOException e){
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Error saving image. Please try again.", Toast.LENGTH_SHORT).show());
                }
            } else {
                // get the new image URI
                img_taken = data.getData();
                // update the imageView to display the selected image
                newPostImageView.setImageURI(img_taken);
                newPostImageView.setTag(img_taken.toString());
            }
        }
    }

    public void sharePost(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        // the post URL is stored as TAG in the timeStamp TextView
        TextView tv = findViewById(R.id.tvContent);
        shareIntent.putExtra(Intent.EXTRA_TEXT, tv.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT, tv.getText().toString());
        startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

//    public int getLayoutWidth(Context context, int layoutResId) {
//        // Ensure you're using a LayoutInflater from the correct context
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        // Inflate the layout. The second parameter is the root view, which is null because we're just measuring.
//        View layoutView = inflater.inflate(layoutResId, null);
//
//        // Make the view measure itself
//        layoutView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        // Return the measured width
//        return layoutView.getMeasuredWidth();
//    }

}

