//package com.example.project_part2.entities;
//
//import android.net.Uri;
//
//public class Comment {
//
//    // pfp is a Uri saved as string, no one
//    // outside needs to know its saved as a string
//    private String authorId;
//    private String content;
//    private String date;
//
//
//    public Comment(User u, String c, String d) {
//        this.pfp = u.getPfp().toString();
//        this.content = c;
//        this.date = d;
//    }
//
//    public Comment(Uri p, String c, String d) {
//        this.pfp = p.toString();
//        this.content = c;
//        this.date = d;
//    }
//
//    public Comment(Uri p, String c) {
//        this.pfp = p.toString();
//        this.content = c;
//        this.date = "null";
//    }
//
//    public String getContent() {
//        return this.content;
//    }
//
//    public String getDate() {
//        return this.date;
//    }
//}
