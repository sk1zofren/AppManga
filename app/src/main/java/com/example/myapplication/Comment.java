package com.example.myapplication;

public class Comment {

    private String userName;
    private String commentText;
    private long timestamp;

    public Comment() {
    }

    public Comment( String userName, String commentText, long timestamp) {

        this.userName = userName;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
