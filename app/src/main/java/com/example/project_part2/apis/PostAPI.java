package com.example.project_part2.apis;

import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_part2.MainActivity;
import com.example.project_part2.R;
import com.example.project_part2.adapters.PostListAdapter;
import com.example.project_part2.entities.BodyRequests.IdBody;
import com.example.project_part2.entities.BodyRequests.PostBodyCreate;
import com.example.project_part2.entities.BodyRequests.PostBodyEdit;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.MyApplication;
import com.example.project_part2.viewmodels.PostsViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
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


    public void createPost(Post newPost, PostsViewModel viewModel) {

        PostBodyCreate postBodyCreate = new PostBodyCreate(newPost.getContent(), newPost.getImage(), newPost.getDate(), newPost.getAuthorPfp(), newPost.getAuthorDisplayName());
        Call<Post> call = webServiceAPI.createPost(MyApplication.registeredUser.getValue().id(), "Bearer " + MyApplication.token.getValue(), postBodyCreate);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "post created successfully", Toast.LENGTH_SHORT).show();
                    if (viewModel.getPosts().getValue() != null) {
                        List<Post> newPosts = viewModel.getPosts().getValue();
                        newPosts.add(0, response.body());
                        viewModel.getPosts().setValue(newPosts);
                    }

                } else {
                    if (response.code() == 403) {
                        Toast.makeText(MyApplication.context, "malicious URL spotted in post, aborting current action", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyApplication.context, "failed to create post", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(MyApplication.context, "failed to create post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editPost(Post updatedPost, EditText editText, RecyclerView.Adapter<PostListAdapter.PostViewHolder> adapter) {


        PostBodyEdit postBodyEdit = new PostBodyEdit(editText.getText().toString(), updatedPost.getImage(), Integer.toString(updatedPost.getLikes()), null);

        Call<Void> call = webServiceAPI.editPost(MyApplication.registeredUser.getValue().id(), updatedPost.get_id(),"Bearer " + MyApplication.token.getValue(), postBodyEdit);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "post edited successfully", Toast.LENGTH_SHORT).show();
                    // update with updated post
                    String newText = editText.getText().toString();
                    updatedPost.setContent(newText);
                    adapter.notifyDataSetChanged();
                } else {
                    if (response.code() == 403) {
                        Toast.makeText(MyApplication.context, "malicious URL spotted in post, aborting current action", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyApplication.context, "failed to edit post", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "failed to edit post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deletePost(List<Post> posts, int position, RecyclerView.Adapter<PostListAdapter.PostViewHolder> adapter) {

        Call<Void> call = webServiceAPI.deletePost(MyApplication.registeredUser.getValue().id(), posts.get(position).get_id(), "Bearer " + MyApplication.token.getValue());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "post deleted successfully", Toast.LENGTH_SHORT).show();
                    // delete from view
                    posts.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyApplication.context, "failed to delete post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "failed to delete post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void likePost(Post updatedPost) {

        PostBodyEdit postBodyEdit = new PostBodyEdit(updatedPost.getContent(), updatedPost.getImage(), Integer.toString(updatedPost.getLikes()), null);

        Call<Void> call = webServiceAPI.editPost(MyApplication.registeredUser.getValue().id(), updatedPost.get_id(),"Bearer " + MyApplication.token.getValue(), postBodyEdit);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "like was successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyApplication.context, "failed to like", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "failed to like", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getFeedPosts(MutableLiveData<List<Post>> posts) {

        Call<List<Post>> call = webServiceAPI.getFeedPosts(MyApplication.registeredUser.getValue().id(), "Bearer " + MyApplication.token.getValue());
        call.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.isSuccessful()) {
                    // setting posts of local db to response from server
                    if (response.body() != null && response.body().size() != 0) {
//                        posts.setValue(null);
                        posts.setValue(response.body());
                        System.out.println("first img" + response.body().get(0).getImage());
                    }
                } else { Toast.makeText(MyApplication.context, "error feed posts", Toast.LENGTH_SHORT).show(); }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MyApplication.context, "error getting feed posts", Toast.LENGTH_SHORT).show();
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
