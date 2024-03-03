package com.example.project_part2.entities;

import android.net.Uri;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    // id used in db to differentiate between posts
    private int id;
    private String content; // the post text content
    private Uri image; // the post image (when applicable)
    private final User author; // the post's author
    String url; // temp default URL for all posts
    private final String timeStamp; // the post's creation Date
    private int likes;
    @Ignore
    private boolean liked;
    private final List<Comment> comments;


    public Post() {
        // create default post
        this("I like trains", null, new User());
    }

    public Post(String content, Uri image, User author) {

        this.content = content;
        this.image = image;
        this.author = author;

        this.timeStamp = new Date().toString();

        // default stuff
        this.likes = 0;
        this.comments = new ArrayList<>();
        this.url = "www.google.com";
    }

    // from json to object
//    public Post(JSONObject postJson) throws JSONException {
//
//        // extract the nested author object
//        JSONObject authorJson = postJson.getJSONObject("author");
//
//        // construct the user using User JSON constructor
//        this.author = new User(authorJson);
//        this.content = postJson.getString("text");
//        this.timeStamp = new Date().toString();
//
//        // check if post has image
//        if (postJson.getString("image").equals("")) {
//            this.image = null;
//        } else {
//            this.image = Uri.parse(postJson.getString("image"));
//        }
//
//        if (!postJson.getString("likes").equals("")) {
//            this.likes = Integer.parseInt(postJson.getString("likes"));
//        }
//
//        this.comments = new ArrayList<>();
//    }

    public String getContent() {
        return this.content;
    }

    public Uri getImage() {
        if (this.image == null) {
            return null;
        }
        String uriString = this.image.toString();
        return Uri.parse(uriString);
    }

    public User getAuthor() {
        return this.author;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void like() {
        if (!this.liked)
            { likes++; liked = true; }
        else
            { likes--; liked = false;}
    }

    public boolean getLiked() {
        return this.liked;
    }

    public int getLikeCount() {
        return this.likes;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(Uri imageUri) {
        this.image = imageUri;
    }

    public String getUrl() {
        return this.url;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public int getId() {
        return id;
    }
}
