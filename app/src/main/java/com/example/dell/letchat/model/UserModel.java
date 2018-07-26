package com.example.dell.letchat.model;

import android.graphics.drawable.Drawable;

import java.sql.Timestamp;

public class UserModel {

    private String content;
    private String username;
    private String type;
    private Timestamp createdAt;
    private Drawable emoji;

    public UserModel(String content, String username, String type) {
        this.content = content;
        this.username = username;
        this.type = type;
    }

    public UserModel(String username, String type, Drawable emoji) {
        this.username = username;
        this.type = type;
        this.emoji = emoji;
    }

    public Drawable getEmoji() {
        return emoji;
    }

    public void setEmoji(Drawable emoji) {
        this.emoji = emoji;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
