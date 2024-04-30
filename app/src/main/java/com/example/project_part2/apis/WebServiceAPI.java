package com.example.project_part2.apis;

import com.example.project_part2.entities.BodyRequests.CommentBodyCreate;
import com.example.project_part2.entities.BodyRequests.CommentBodyEdit;
import com.example.project_part2.entities.BodyRequests.FriendBodySend;
import com.example.project_part2.entities.BodyRequests.IdBody;
import com.example.project_part2.entities.BodyRequests.PostBodyCreate;
import com.example.project_part2.entities.BodyRequests.PostBodyEdit;
import com.example.project_part2.entities.BodyRequests.TokenBody;
import com.example.project_part2.entities.BodyRequests.UserBody;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {


    // ===================  TOKENS =======================

    @POST("tokens/")
    Call<String> createToken(@Body TokenBody body);

    @GET("tokens/{id}")
    Call<Boolean> isLoggedIn(@Path("id") String id);


    // ===================  USERS =======================

    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/")
    Call<Void> createUser(@Body UserBody userBody);

    @GET("users/{id}")
    Call<User> getUserById (@Path("id") String id);

    @PATCH("users/{id}")
    Call<Void> editUser();

    @DELETE("users/{id}")
    Call<Void> deleteUser();


    // ===================  POSTS =======================

    @GET("posts/feed/{id}")
    Call<List<Post>> getFeedPosts(@Path("id") String id, @Header("Authorization") String authToken);

    @GET("posts/{id}/friend-posts/{fid}")
    Call<List<Post>> getFriendPosts(@Path("id") String id, @Path("fid") String fid, @Header("Authorization") String authToken);

    @POST("users/{id}/posts")
    Call<Post> createPost(@Path("id") String id, @Header("Authorization") String authToken, @Body PostBodyCreate postBodyCreate);

    @PATCH("users/{id}/posts/{pid}")
    Call<Void> editPost (@Path("id") String id, @Path("pid") String pid, @Header("Authorization") String authToken, @Body PostBodyEdit postBodyEdit);

    @DELETE("users/{id}/posts/{pid}")
    Call<Void> deletePost(@Path("id") String id, @Path("pid") String pid, @Header("Authorization") String authToken);

    // ===================  COMMENTS =======================

    @GET("posts/{id}")
    Call<List<Post>> getComments(@Path("id") String id, @Header("Authorization") String authToken);

    @POST("posts/{id}")
    Call<Post> createComment(@Path("id") String id, @Header("Authorization") String authToken, @Body CommentBodyCreate commentBodyCreate);

    @PATCH("users/{id}/comments/{cid}")
    Call<Void> editComment(@Path("id") String id, @Path("fid") String fid, @Header("Authorization") String authToken, @Body CommentBodyEdit commentBodyEdit);

    @DELETE("users/{id}/comments/{cid}")
    Call<Void> deleteComment(@Path("id") String id, @Path("fid") String fid, @Header("Authorization") String authToken);

    // ===================  FRIENDS =======================

    @GET("posts/{id}")
    Call<List<String>> getFriends(@Path("id") String id, @Header("Authorization") String authToken);

    @POST("users/{id}/friends")
    Call<Void> sendFriendRequest(@Path("id") String dstId, @Header("Authorization") String authToken, @Body FriendBodySend friendBodySend);

    @PATCH("users/{id}/friends/{fid}")
    Call<Void> acceptFriendRequest(@Path("id") String id, @Path("fid") String fid, @Header("Authorization") String authToken);

    @DELETE("users/{id}/friends/{fid}")
    Call<Void> deleteFriendRequest(@Path("id") String id, @Path("fid") String fid, @Header("Authorization") String authToken);

}