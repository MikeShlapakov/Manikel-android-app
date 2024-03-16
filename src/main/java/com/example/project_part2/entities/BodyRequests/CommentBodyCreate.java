package com.example.project_part2.entities.BodyRequests;

public class CommentBodyCreate {

    String authorId;
    String content;
    String date;

    public CommentBodyCreate(String authorId, String content, String date) {
        this.authorId = authorId;
        this.content = content;
        this.date = date;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
