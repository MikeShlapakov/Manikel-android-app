package com.example.project_part2.entities.BodyRequests;

public class PostBodyCreate {

    String content;
    String image;
    String date;
    String authorPfp;
    String authorDisplayName;

    public PostBodyCreate(String content, String image, String date, String authorPfp, String authorDisplayName) {
        this.content = content;
        this.image = image;
        this.date = date;
        this.authorPfp = authorPfp;
        this.authorDisplayName = authorDisplayName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
