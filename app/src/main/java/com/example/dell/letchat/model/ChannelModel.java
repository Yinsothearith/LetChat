package com.example.dell.letchat.model;

public class ChannelModel {

    String name;
    int count;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChannelModel(String name, int limit, String id) {
        this.name = name;
        this.count = limit;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
