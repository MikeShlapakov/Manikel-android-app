package com.example.project_part2.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_part2.Repos.PostsRepository;
import com.example.project_part2.apis.PostAPI;
import com.example.project_part2.entities.Post;

import java.util.List;

public class PostsViewModel extends ViewModel {

    private PostsRepository repo;
    private MutableLiveData<List<Post>> posts;
    PostAPI postAPI;

    public PostsViewModel() {
        repo = new PostsRepository();
        posts = PostsRepository.getPosts();
        postAPI = new PostAPI();
    }

    public MutableLiveData<List<Post>> getPosts() {
        return posts;
    }

//    public void add(Post post) {
//        repo.add(post);
//    }
//
//    public void delete(Post post) {
//        repo.delete(post);
//    }

    public void getPostsFromServer () {
        postAPI.getFeedPosts(posts);
//        repo.reload();
    }
}
