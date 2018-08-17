package com.example.richardvdriest.twitter;

public class Tweet {

    private int profilePicId;
    private String username, content;

    public Tweet(int profilePicId, String username, String content){
        this.profilePicId = profilePicId;
        this.username = username;
        this.content = content;
    }

    public int getProfilePicId() {
        return profilePicId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
}
