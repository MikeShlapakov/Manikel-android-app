package com.example.project_part2.entities.BodyRequests;

import java.util.List;

public class PostBodyEdit {
    String content;
    String image;
    String likes;
    List<String> comments;

    public PostBodyEdit(String content, String image, String likes, List<String> comments) {
        this.content = content;
        this.image = image;
        this.likes = likes;
        this.comments = comments;
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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
