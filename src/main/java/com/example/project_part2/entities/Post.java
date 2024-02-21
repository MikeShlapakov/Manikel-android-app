package com.example.project_part2.entities;

import android.net.Uri;

import com.example.project_part2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * the class representation of a post
 */
public class Post{
    String url = "www.google.com"; // temp default URL for all posts
    private String text; // the post text content
    private Uri image; // the post image (when applicable)
    private final User author; // the post's author
    private final String timeStamp; // the post's creation Date
    private boolean liked = false;
    private final List<String> comments;

    /**
     * constructor
     * @param text the post's text content
     * @param image the post's image content
     * @param author the post's author
     */
    public Post(String text, Uri image, User author) {
        this.text = text;
        this.image = image;
        this.author = author;
        this.timeStamp = new Date().toString();
        this.comments = new ArrayList<>();
    }

    /**
     * default constructor
     */
    public Post() { this("I like trains", null, new User()); }

    /**
     * json constructor - parses data from json
     * @param postJson the JSON containing the post data
     */
    public Post(JSONObject postJson) throws JSONException {
        // extract the nested author object
        JSONObject authorJson = postJson.getJSONObject("author");
        // construct the user using User JSON constructor
        this.author = new User(authorJson);
        this.text = postJson.getString("text");
        this.timeStamp = new Date().toString();
        // check if the image field in JSON is empty - if so, set the image Uri as null.
        if (postJson.getString("image").equals("")) {
            this.image = null;
        } else {
            this.image = Uri.parse(postJson.getString("image"));
        }

        if (authorJson.getString("imageUri").equals("")) {
            authorJson.put("imageUri", R.drawable.ddog1);
        }
        this.comments = new ArrayList<>();
    }

    /**
     * getter for the post text content
     * @return the String value of the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * getter for the image Uri
     * @return a copy of the image Uri
     */
    public Uri getImage() {
        if (this.image == null) {
            return null;
        }
        String uriString = this.image.toString();
        return Uri.parse(uriString);
    }

    /**
     * getter for the author User
     * @return the author object (which is immutable)
     */
    public User getAuthor() {
        return this.author;
    }

    /**
     * getter for the post timeStamp Date
     * @return the Date timestamp object (which is immutable)
     */
    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void toggleLiked() {
        this.liked = !this.liked;
    }

    public boolean getLiked() {
        return this.liked;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(Uri imageUri) {
        this.image = imageUri;
    }

    /**
     * URL implementation will come with online hosting
     * @return the posts URL
     */
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
