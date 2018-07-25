package com.example.dell.letchat.model;

import java.sql.Timestamp;

public class ChatModel {

    private String content;
    private String username;
    private String id;
    private String type;
    private String channelId;
    private Timestamp createdAt;

    public ChatModel(String content, String username, String id, String type, String channelId) {
        this.content = content;
        this.username = username;
        this.id = id;
        this.type = type;
        this.channelId = channelId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
