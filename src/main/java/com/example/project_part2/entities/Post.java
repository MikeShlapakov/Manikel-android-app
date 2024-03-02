package com.example.project_part2.entities;

import android.net.Uri;

import com.example.project_part2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Post{
    private String content; // the post text content
    private Uri image; // the post image (when applicable)
    private final User author; // the post's author


    String url; // temp default URL for all posts
    private final String timeStamp; // the post's creation Date
    private int likes;
    private boolean liked;
    private final List<String> comments;


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
    public Post(JSONObject postJson) throws JSONException {

        // extract the nested author object
        JSONObject authorJson = postJson.getJSONObject("author");

        // construct the user using User JSON constructor
        this.author = new User(authorJson);
        this.content = postJson.getString("text");
        this.timeStamp = new Date().toString();

        // check if post has image
        if (postJson.getString("image").equals("")) {
            this.image = null;
        } else {
            this.image = Uri.parse(postJson.getString("image"));
        }

        this.comments = new ArrayList<>();
    }

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

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(Uri imageUri) {
        this.image = imageUri;
    }

    public String getUrl() {
        return this.url;
    }

    public List<String> getComments() {
        return this.comments;
    }

    public void addComment(String comment) {
        this.comments.add(comment);
    }
}
