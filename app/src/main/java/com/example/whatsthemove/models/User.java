package com.example.whatsthemove.models;

public class User {
    String email;
    String userId;
    String userName;

    public User(String email, String userId, String userName) {
        this.email = email;
        this.userId = userId;
        this.userName = userName;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

