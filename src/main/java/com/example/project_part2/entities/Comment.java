package com.example.project_part2.entities;

import android.net.Uri;

public class Comment {

    private Uri pfp;
    private String content;

    // TODO: add support for date and likes
    private String date;
    private int likes;


    public Comment(User u, String c, String d, int l) {
        this.pfp = u.getPfp();
        this.content = c;
        this.date = d;
        this.likes = l;
    }

    public Comment(Uri p, String c, String d, int l) {
        this.pfp = p;
        this.content = c;
        this.date = d;
        this.likes = l;
    }

    public Comment(Uri p, String c) {
        this.pfp = p;
        this.content = c;
        this.date = "null";
        this.likes = 0;
    }

    public String getContent() {
        return this.content;
    }

    public String getDate() {
        return this.date;
    }
}
