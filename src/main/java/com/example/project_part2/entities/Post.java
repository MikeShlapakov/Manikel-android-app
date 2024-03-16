package com.example.project_part2.entities;

import android.net.Uri;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)

    // id used in db to differentiate between posts
    private int id;
    private String content; // the post text content
    private String image; // the post image (when applicable)
    private final User author; // the post's author
    private final String timestamp; // the post's creation Date
    private int likes;
    @Ignore
    private transient boolean liked;

    private final List<Comment> comments;


    public Post() {
        // create default post
        this("I like trains", null, new User());
    }

    public Post(String content, Uri image, User author) {

        this.content = content;
        if (image != null) {
            this.image = image.toString();
        } else {
            this.image = null;
        }
        this.author = author;

        this.timestamp = new Date().toString();

        // default stuff
        this.likes = 0;
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

    public String getTimestamp() {
        return this.timestamp;
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
        this.image = imageUri.toString();
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