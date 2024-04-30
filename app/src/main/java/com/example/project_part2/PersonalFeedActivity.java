package com.example.project_part2;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_part2.adapters.PostListAdapter;
import com.example.project_part2.apis.PostAPI;
import com.example.project_part2.apis.UserAPI;
import com.example.project_part2.entities.Post;
import com.example.project_part2.util.ImageUtil;
import com.example.project_part2.util.MyApplication;
import com.example.project_part2.viewmodels.PostsViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.w3c.dom.Text;

import java.util.List;

public class PersonalFeedActivity extends AppCompatActivity {

    private PostsViewModel viewModel;
    private MutableLiveData<List<Post>> personalPosts;
    private UserAPI userAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_feed);

        // holds sample posts from the .json at first
        // this also asks for posts from server
        viewModel = new ViewModelProvider(this).get(PostsViewModel.class);
        userAPI = new UserAPI();
        personalPosts = new MutableLiveData<>();

        RecyclerView postList = findViewById(R.id.postList);
        PostListAdapter adapter = new PostListAdapter(this);
        postList.setAdapter(adapter);
        postList.setLayoutManager(new LinearLayoutManager(this));

        TextView activeUserInfo = findViewById(R.id.displayName);
        String displayName = MyApplication.registeredUser.getValue().getDisplayName();
        activeUserInfo.setText(displayName);

        ImageView activeUserPic = findViewById(R.id.pfp);
        activeUserPic.setImageURI(ImageUtil.decodeBase64ToUri(MyApplication.registeredUser.getValue().getPfp(), MyApplication.context));

        ImageView feedOwnerPfp = findViewById(R.id.feedOwnerPfp);
        feedOwnerPfp.setImageURI(ImageUtil.decodeBase64ToUri(getIntent().getStringExtra("PFP"), MyApplication.context));

        TextView feedOwnerDisplayName = findViewById(R.id.feedOwnerDisplayName);
        feedOwnerDisplayName.setText(getIntent().getStringExtra("DISPLAY_NAME"));

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

        adapter.setPosts(viewModel.getPosts().getValue());

        userAPI.getFriendPosts(getIntent().getStringExtra("ID"), personalPosts);
        personalPosts.observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                adapter.setPosts(posts);
                adapter.notifyDataSetChanged();
            }
        });


        // if we change the viewModel posts this should be called
//        viewModel.getPosts().observe(this, posts -> {
//            adapter.setPosts(posts);
//            adapter.notifyDataSetChanged();
//        });
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
}
