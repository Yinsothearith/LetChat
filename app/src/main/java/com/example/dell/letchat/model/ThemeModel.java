package com.example.dell.letchat.model;

public class ThemeModel {

    private String themeName, primaryColor, secondaryColor;
    private int itemType;

    public ThemeModel(String themeName, String primaryColor, String secondaryColor, int itemType) {
        this.themeName = themeName;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.itemType = itemType;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
