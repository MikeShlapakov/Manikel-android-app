package com.example.project_part2.apis;

import static com.example.project_part2.util.MyApplication.incoming;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.project_part2.FeedActivity;
import com.example.project_part2.MainActivity;
import com.example.project_part2.R;
import com.example.project_part2.adapters.IncomingFriendsAdapter;
import com.example.project_part2.entities.BodyRequests.FriendBodySend;
import com.example.project_part2.entities.BodyRequests.UserBody;
import com.example.project_part2.entities.Friend;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.MyApplication;

import java.util.ArrayList;
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

        if (pfp.equals("")) { pfp = "data:image/png;base64,UklGRkwFAABXRUJQVlA4TD8FAAAvd8AdAKflIJIkRershTj/Ys4ePFpA0LZtnA7j2R4cg7aNJKV7/IEdpS+Z/3XIq0AioEUBSkkgESLEIAWkeRcEQsYEIi3CcQ+y7yO0lFoiQikNGmmdVi9BKK190rS8TygNAkJkpLEQ1BAhaUppEJhThHQgkVEiYyk1CeByXGb1EJBASJrLRRhekYYIiDREBBJpIBEBREgaovlr3EWj0XTHn8xH5GPz3/lvEQWFW9sMSVLV2LZt27Zt255o27ZtK2d/4fSbWRmR31rxZUT0H4IjSVKjqL0Zo8HWNuOGFwT+mZ5Z63afv/fyk2/usV4361dj7o5HIkTYn/srwAsvCFfxF1I348g34TK+nkHZ0O2fhUQ+81CyRp8XkmFN1Jy7xkFcfnVjR5/xdTXHCYfcnUPSorfCJrq4zXIMF0cbW94uImj2G4HCi/qskDGaBebNbHqPuGNQRrflKpwuMHo0MUPOGVRouY4RGD2Eli22YTWWRKpto7aQMu0j0pZUNPrIlBwKsVV64yFC5mNRTrOkk4almuk4KwZFdMnTEWYwZ+mUDMe/KfrTbCq2gqheFXo12ErFHft3dd80EZMFaFHD4Byn0LASxPiKwmANDdtArioa7KDhFKhSpQqcoeEWqFfFgJs0PLGPVDnwEQ2/gG5VGLym4YcXXtHwGLSqosEDGm57MeQ6DSdApSqV4CSl01G2KtlgGw3LcbE+oAbjEn4FDRMNpEkNxjlOCNDITZCvBtvKJBGbcXmlpHIwLrA2k1k9f1N3JtS4oKSzgj4OwjrkdWhwnNB7wS9mMKnSO+FUM0hTek+4D/89cmQZnN1eJmTCezyUCjnlOLf3EwKUshGFSX2wRhsCpASPCdtG1w9gIzDHggFaGXFNYFLb3eFUg66NCFDLlGfCNirHReFiowV6NiVATHD2sl0fhL2zktvguBvWueHGlg+7ls0OUhq26uhzESqRWeU1TR0+6/IsI0Ll+dFVI2iYd/CdUJ53rAm0X08Lj+Iv9NaY/d+EZ/E1e2jxC+NpfO2Z9V+Nk+T8yobWjs6faS5MDHOm/cLmzp/paG2o5GThlK/rvTHsgMPEw9MqQ9WK3tqizOSEGE4wuqjWD1VDKtPChT0Hhnlg7CWH5zG321Ic1g6vw6Wxyg2/amwyOywP0qGNLVdZtcMCRTdZHoWjDeawYpsESuqyPItOMhhWask327A+y8MY26hvrNB0W0XOtjxOtq1aT1fnDE40ZcBrrI3iLssSnGZMt+95OAaPQNWfgjewSLX6BNLC4EZQjaUGFFgkUoDHsFSNK7ii6KWh1wBW2W0us4jEKOxIb8XH9FOhWV1H+o7TdkqbtQJTDWimoxmPQ8tbg039ATpYq+pI77a94yQUA/bIO48XxijReE1a3kPQRgkDLV+cv4N+SjT4zrJG4jnBIhU8N4yQ7ijgqooWBuOke1UglpZYMPmvQwOF0cJVwhLqrM70cOEqnN5JQqFwHd0Ub1xHD1AQbepdN+hyRGyP5So9saadAmPcn9/SRabvMto0/ZkSbeo8GfLr3dGvtl79AaqTk/AmEVp1MgaMlzUKrwXS2lEEGCm9fsYO0gAlA9hNkm81PAPtlLSDpwFp2A6to6QOXJS3H+RRqpMa7Je3DndFaUexYJ28mTjjVjr1qhXnMjMgn0egiI4idTcK7bS9QlR+YZzbThUW4JSrqNTJKjyKBQpvm4jpo7GjvhiVN1asxqemhIZSPIbViu7av48Nyk4KOrE9eV/Vnf5r8clJ7PdefyIewVplF3sv224p975OZuD8ddANBgA="; }
        UserBody userBody = new UserBody(displayName, username, password, pfp);
        Toast.makeText(MyApplication.context, "creating user", Toast.LENGTH_SHORT).show();
        Call<Void> call = webServiceAPI.createUser(userBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // after the user was created, update him
//                    MyApplication.registeredUser.getValue().setPfp(pfp);
                    updateUser(MyApplication.registeredUser);
                    // MyApplication.registeredUser.setValue(getUserByUsername(MyApplication.registeredUser.getValue().username()));
                    Toast.makeText(MyApplication.context, "user created successfully", Toast.LENGTH_SHORT).show();
                } else { Toast.makeText(MyApplication.context, "error creating user", Toast.LENGTH_SHORT).show(); }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "error creating user", Toast.LENGTH_SHORT).show();
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
                            break;
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

    public void checkCredentials (String username, String password, MutableLiveData<Boolean> answer) {

        User user;
        if (MyApplication.registeredUser.getValue() != null) {
            user = MyApplication.userDao.get(MyApplication.registeredUser.getValue().lid());
        } else { user = null; }

        // if not found in local db, check with server
        if (user == null) {

            // create a local Mutable user that holds the entered username
            User temp = new User(); temp.setUsername(username);
            MutableLiveData<User> userFromServer = new MutableLiveData<>(temp);

            updateUser(userFromServer);

        } else {
            // if exists in local db
            answer.setValue(user.password().equals(password));
            return;
        }

        Call<List<User>> call = webServiceAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.isSuccessful() == true) {

                    List<User> userList = response.body();

                    if (userList == null) { return; }

                    boolean inServer = false;

                    // update registered User to his server counter-part
                    for (User u : userList) {
                        if (u.username().equals(username)) {
                            inServer = true;
//                            u.set_lid(MyApplication.registeredUser.getValue().lid());
                            MyApplication.registeredUser.setValue(u);
                            break;
                        }
                    }

                    // if not in server, then doesn't exist at all
                    if (!inServer) {
                        Toast.makeText(MyApplication.context, "user not found in server", Toast.LENGTH_SHORT).show();
                        answer.setValue(false);
                    }

                    if (MyApplication.registeredUser.getValue() != null) {
                        answer.setValue(MyApplication.registeredUser.getValue().password().equals(password));
                    } else { answer.setValue(false); }
                    // check if passwords match
                } else {
                    // if server response isn't valid
                    Toast.makeText(MyApplication.context, "user request timed out", Toast.LENGTH_SHORT).show();
                    answer.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MyApplication.context, "user request timed out", Toast.LENGTH_SHORT).show();
                answer.setValue(false);
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
    public void getFriendPosts(String friendId, MutableLiveData<List<Post>> postsLiveData) {
        Call<List<Post>> call = webServiceAPI.getFriendPosts(MyApplication.registeredUser.getValue().id(), friendId, "Bearer " + MyApplication.token.getValue());
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "received user's posts successfully", Toast.LENGTH_SHORT).show();
                    postsLiveData.postValue(response.body());
                } else {
                    Toast.makeText(MyApplication.context, "error getting user's posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MyApplication.context, "error getting user's posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // returns List<Friend> for incoming friend requests
    public void getFriendRequests (MutableLiveData<List<Friend>> friends) {

        List<Friend> friendsFromServer = new ArrayList<>();
        Call<List<User>> call = webServiceAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.isSuccessful()) {

//                    Toast.makeText(MyApplication.context, "received friend requests successfully", Toast.LENGTH_SHORT).show();

                    List<User> userList = response.body();

                    if (userList == null) { return; }

                    for (User u : userList) {
                        // if is friend of registered user
                        if (MyApplication.registeredUser.getValue() != null &&
                            MyApplication.registeredUser.getValue().getFriendRequests().contains(u.id())) {

                            Friend friend = new Friend(u.getDisplayName(), u.getPfp(), u.id());
                            friendsFromServer.add(friend);
                        }
                    }

                    friends.setValue(friendsFromServer);

                } else {
//                    Toast.makeText(MyApplication.context, "failed to receive friend requests", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
//                Toast.makeText(MyApplication.context, "failed to receive friend requests", Toast.LENGTH_SHORT).show();
            }
        });

//        Call<List<String>> call = webServiceAPI.getFriends(userId, "Bearer " + MyApplication.token.getValue());
//        call.enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                if (response.isSuccessful()) {
//                    friends.postValue(response.body());
//                } else {
//                    // Handle error response
//                }
//            }
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//                // Handle failure
//            }
//        });
    }

    public void sendFriendRequest(String dstId) {

        FriendBodySend friendBodySend = new FriendBodySend(dstId);
//        Toast.makeText(MyApplication.context, "friend request sent", Toast.LENGTH_SHORT).show();
        Call<Void> call = webServiceAPI.sendFriendRequest(MyApplication.registeredUser.getValue().id(), "Bearer " + MyApplication.token.getValue(), friendBodySend);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "friend request sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyApplication.context, "error sending friend request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "error sending friend request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void acceptFriendRequest(String friendId, MutableLiveData<List<Friend>> incoming, int position, IncomingFriendsAdapter adapter) {

        Call<Void> call = webServiceAPI.acceptFriendRequest(MyApplication.registeredUser.getValue().id(), friendId, "Bearer " + MyApplication.token.getValue());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "accepted friend request successfully", Toast.LENGTH_SHORT).show();
                    List<Friend> temp = incoming.getValue(); temp.remove(position);
                    incoming.setValue(temp);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyApplication.context, "error accepting friend request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "error accepting friend request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteFriendRequest(String friendId,  MutableLiveData<List<Friend>> incoming, int position, IncomingFriendsAdapter adapter) {
        Call<Void> call = webServiceAPI.deleteFriendRequest(MyApplication.registeredUser.getValue().id(), friendId, "Bearer " + MyApplication.token.getValue());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "denied friend request successfully", Toast.LENGTH_SHORT).show();
                    List<Friend> temp = incoming.getValue(); temp.remove(position);
                    incoming.setValue(temp);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyApplication.context, "error denying friend request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "error denying friend request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isFriend (User u, String fid) {
        if (u == null) { return false; }

        List<String> incoming = u.getFriends();
        for (String s : incoming) {
            if (s.equals(fid)) {
                return true;
            }
        }
        return false;
    }

    public void deleteFriend (String dstId) {

//        Toast.makeText(MyApplication.context, "friend request sent", Toast.LENGTH_SHORT).show();
        Call<Void> call = webServiceAPI.deleteFriendRequest(MyApplication.registeredUser.getValue().id(), dstId, "Bearer " + MyApplication.token.getValue());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "deleted friend sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyApplication.context, "error deleted friend", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "error deleted friend", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void getFriends (MutableLiveData<List<Friend>> friends) {
//
//        Call<Void> call = webServiceAPI.getUsers();
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(MyApplication.context, "denied friend request successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MyApplication.context, "error denying friend request", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(MyApplication.context, "error denying friend request", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    public boolean getFriends (User u, String fid) {
//        if (u == null) { return false; }
//
//        List<String> incoming = u.getFriends();
//        MutableLiveData<List<User>> friends = new MutableLiveData<>();
//        getUsers(friends);
//        friends.observe(this, new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//
//            }
//        });
//
//        for (String s : incoming) {
//            if (s.equals(fid)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
