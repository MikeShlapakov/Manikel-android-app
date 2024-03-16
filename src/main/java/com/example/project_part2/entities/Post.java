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

    @PrimaryKey(autoGenerate = true)
    private long _lid;
    private String _id;
    private String content; // the post text content
    private String image; // the post image (when applicable)
    private final String authorId; // the post's authorId
    private final String authorPfp;
    private final String authorDisplayName;
    private String date; // the post's creation Date
    private int likes;
    @Ignore
    private transient boolean liked;

//  BONUS
//  private final List<Comment> comments;

    public Post (String content, String image, String authorId, String authorPfp, String authorDisplayName) {
        this.content = content;
        this.image = image;
        this.authorId = authorId;
        this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        this.authorPfp = authorPfp;
        this.authorDisplayName = authorDisplayName;
    }

    public String getContent() {
        return this.content;
    }

//    public Uri getImage() {
//        if (this.image == null) {
//            return null;
//        }
//        String uriString = this.image.toString();
//        return Uri.parse(uriString);
//    }

    public String getAuthorId() {
        return this.authorId;
    }

    public void like() {
        if (!this.liked)
            { likes++; liked = true; }
        else
            { likes--; liked = false;}
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getLikeCount() {
        return this.likes;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public void setImage(Uri imageUri) {
//        this.image = imageUri.toString();
//    }

    public String get_id() {
        return _id;
    }

    public String getDate() {
        return date;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isLiked() {
        return liked;
    }

//    public List<Comment> getComments() {
//        return this.comments;
//    }
//
//    public void addComment(Comment comment) {
//        this.comments.add(comment);
//    }

    public long get_lid() {
        return _lid;
    }

    public void set_lid(long _lid) {
        this._lid = _lid;
    }

    public String getImage() {
        return image;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getAuthorPfp() {
        return authorPfp;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }
}