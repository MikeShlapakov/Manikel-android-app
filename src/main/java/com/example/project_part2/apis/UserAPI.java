package com.example.project_part2.apis;

import androidx.lifecycle.MutableLiveData;

import com.example.project_part2.FeedActivity;
import com.example.project_part2.MainActivity;
import com.example.project_part2.R;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class UserAPI {

    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI() {
//        this.postListData = postListData;
//        this.dao = dao;

        // Gson will automatically convert Post to json and vice-versa
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    // doesn't have token when accessing these functions
    public void createUser(User user) {

        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Handle response
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void getUsers(MutableLiveData<List<User>> usersLiveData) {
        Call<List<User>> call = webServiceAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    usersLiveData.postValue(response.body());
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public User getUserByUsername (String username) {

        MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
        getUsers(usersLiveData);

        List<User> userList = usersLiveData.getValue();

        if (userList == null) { return null; }

        for (User u : userList) {
            if (u.username().equals(username)) {
                return u;
            }
        }
        return null;
    }


    public void getUserById(MutableLiveData<User> userLiveData, String id) {

        Call<User> call = webServiceAPI.getUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userLiveData.postValue(response.body());
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // has token when accessing these functions
    public void getUserPosts(String userId, MutableLiveData<List<Post>> postsLiveData) {
        Call<List<Post>> call = webServiceAPI.getUserPosts(userId, "Bearer " + MyApplication.token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    postsLiveData.postValue(response.body());
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void sendFriendRequest(String dstId) {
        Call<Post> call = webServiceAPI.sendFriendRequest(dstId, "Bearer " + MyApplication.token, MainActivity.registeredUser.id());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                // Handle successful request
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void acceptFriendRequest(String userId, String friendId) {
        Call<Void> call = webServiceAPI.acceptFriendRequest(userId, "Bearer " + MyApplication.token, friendId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Handle acceptance
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void deleteFriendRequest(String userId, String friendId) {
        Call<Void> call = webServiceAPI.deleteFriendRequest(userId, friendId, "Bearer " + MyApplication.token);
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
}
