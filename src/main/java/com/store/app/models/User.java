package com.store.app.models;

public class User {
    private int id;
    private String username;
    private String avatar;
    private String about;
    private String theme;
    public User(int id, String username, String avatar, String about, String theme) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.about = about;
        this.theme = theme;
    }
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getTheme() { return theme; }
    public void setUsername(String username) { this.username = username; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getAbout() { return about; }
    public String getAvatar() { return avatar; }
    public void setAbout(String about) {
        this.about = about;
    }
}