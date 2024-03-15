package com.example.project_part2.apis;

import androidx.lifecycle.MutableLiveData;

import com.example.project_part2.MainActivity;
import com.example.project_part2.R;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAPI {
//    private MutableLiveData<List<Post>> postListData;
//    private PostDao postDao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public PostAPI() {
//        this.postListData = postListData;
//        this.dao = dao;

        // Gson will automatically convert Post to json and vice-versa
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getPosts(MutableLiveData<List<Post>> posts) {

        Call<List<Post>> call = webServiceAPI.getAllPosts(MyApplication.token);
        call.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                // setting posts of local db to response from server
                posts.setValue(response.body());
//
//                new Thread(() -> {
//                    dao.clear();
//                    dao.insertList(response.body());
//                    postListData.postValue(dao.get());
//                }).start()
            }


            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.out.println("nooni");
            }
        });
    }

    public void createPost(Post post) {
        Call<Void> call = webServiceAPI.createPost(MainActivity.registeredUser.id(), "Bearer " + MyApplication.token, post);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // hello
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

//    public void editPost(String userId, String postId, Post updatedPost) {
//        Call<Void> call = webServiceAPI.editPosts(userId, postId, "Bearer " + MyApplication.token, updatedPost);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                // Handle response
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // Handle failure
//            }
//        });
//    }

    public void deletePost(String userId, String postId) {
        Call<Void> call = webServiceAPI.deletePost(userId, postId, "Bearer " + MyApplication.token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Handle successful deletion
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }


//    public void getComments(String postId, MutableLiveData<List<Comment>> commentsLiveData) {
//        Call<List<Comment>> call = webServiceAPI.getComments(postId, "Bearer " + YOUR_TOKEN);
//        call.enqueue(new Callback<List<Comment>>() {
//            @Override
//            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
//                if (response.isSuccessful()) {
//                    commentsLiveData.postValue(response.body());
//                } else {
//                    // Handle error response
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Comment>> call, Throwable t) {
//                // Handle failure
//            }
//        });
//    }
//
//    public void createComment(String postId, Post comment) {
//        Call<Post> call = webServiceAPI.createComment(postId, "Bearer " + YOUR_TOKEN, comment);
//        call.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                // Handle successful creation
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//                // Handle failure
//            }
//        });
//    }
//
//    public void deleteComment(String userId, String commentId) {
//        Call<Void> call = webServiceAPI.deleteComment(userId, commentId, "Bearer " + YOUR_TOKEN);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                // Handle successful deletion
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // Handle failure
//            }
//        });
//    }



}
