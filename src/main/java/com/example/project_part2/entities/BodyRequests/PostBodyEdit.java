package com.example.project_part2.entities.BodyRequests;

public class PostBodyEdit {

    String content;
    String image;

    public PostBodyEdit(String content, String image) {
        this.content = content;
        this.image = image;
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
}
