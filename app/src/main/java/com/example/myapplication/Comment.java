package com.example.myapplication;

public class Comment {
    private String userName;
    private String text;
    private long timestamp;

    public Comment(String userName, String text, long timestamp) {
        this.userName = userName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getUserName() { return userName; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
}