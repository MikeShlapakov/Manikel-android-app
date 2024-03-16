package com.example.project_part2.apis;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.project_part2.FeedActivity;
import com.example.project_part2.MainActivity;
import com.example.project_part2.R;
import com.example.project_part2.entities.BodyRequests.FriendBodySend;
import com.example.project_part2.entities.BodyRequests.UserBody;
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
    // this will update registeredUser
    public void createUser(String displayName, String username, String password, String pfp) {

        UserBody userBody = new UserBody(displayName, username, password, pfp);
        Toast.makeText(MyApplication.context, "creating user", Toast.LENGTH_SHORT).show();
        Call<Void> call = webServiceAPI.createUser(userBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // after the user was created, update him
                updateUser(MyApplication.registeredUser);
//                MyApplication.registeredUser.setValue(getUserByUsername(MyApplication.registeredUser.getValue().username()));
                Toast.makeText(MyApplication.context, "user created successfully", Toast.LENGTH_SHORT).show();
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

    // sets the value to the user with the same username that's on server
    // CHECK
    public void updateUser (MutableLiveData<User> user) {

        Call<List<User>> call = webServiceAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.isSuccessful()) {

                    List<User> userList = response.body();

                    if (userList == null) { return; }

                    for (User u : userList) {
                        if (user.getValue() != null && u.username().equals(user.getValue().username())) {
                            u.set_lid(user.getValue().lid());
                            user.setValue(u);
                        }
                    }
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                return;
            }
        });
    }

//    public void updateUserViaUsername (String username) {
//
//        MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
//        getUsers(usersLiveData);
//
//        try {
//            Thread.sleep(3000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        List<User> userList = usersLiveData.getValue();
//
//        if (userList == null) { return null; }
//
//        for (User u : userList) {
//            if (u.username().equals(username)) {
//                return u;
//            }
//        }
//        return null;
//    }


    public void getUserById(MutableLiveData<User> userLiveData, String id) {

        Call<User> call = webServiceAPI.getUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userLiveData.postValue(response.body());
                } else {
                    userLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userLiveData.postValue(null);
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

        FriendBodySend friendBodySend = new FriendBodySend(dstId);
        Call<Post> call = webServiceAPI.sendFriendRequest(dstId, "Bearer " + MyApplication.token, friendBodySend);
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
