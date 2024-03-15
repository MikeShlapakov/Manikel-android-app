package com.example.project_part2.entities;

import android.net.Uri;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
public class Post {
//    @PrimaryKey(autoGenerate = true)

    private String _id;
    private String content; // the post text content
    private String image; // the post image (when applicable)
    private final String authorId; // the post's authorId
    private final String date; // the post's creation Date
    private int likes;
    @Ignore
    private transient boolean liked;

//    private final List<Comment> comments;


//    public Post() {
//        // create default post
//        this("I like trains", null, "");
//    }

    public Post (String content, String image, String authorId) {
        this.content = content;
        this.image = image;
        this.authorId = authorId;
        this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

//    public Post () {
//        content = "";
//        image = "";
//        authorId = "";
//        date = "";
//        likes = 0;
//    }

//    public Post(String content, String authorId) {
//
//        this.content = content;
//
//
//        if (image != null) {
//            this.image = image.toString();
//        } else {
//            this.image = null;
//        }
//        this.authorId = authorId;
//
//        this.date = new Date().toString();
//
//        // default stuff
//        this.likes = 0;
////        this.comments = new ArrayList<>();
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

    public String getAuthorId() {
        return this.authorId;
    }

    public String getTimestamp() {
        return this.date;
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

//    public List<Comment> getComments() {
//        return this.comments;
//    }
//
//    public void addComment(Comment comment) {
//        this.comments.add(comment);
//    }

    public String getId() {
        return _id;
    }
}