package com.example.whatsthemove.models;

import java.time.LocalTime;

public class Post {
    String postID;
    String imageURL;
    String postCreationTime;
//    LocalTime postCreationTime;
    String location;
    String description;
    String postCreatedByUserId;
    String postCreatedByUserName;

    public String getPostCreatedByUserName() {
        return postCreatedByUserName;
    }

    public void setPostCreatedByUserName(String postCreatedByUserName) {
        this.postCreatedByUserName = postCreatedByUserName;
    }

    public String getPostCreatedByUserId() {
        return postCreatedByUserId;
    }

    public void setPostCreatedByUserId(String postCreatedByUserId) {
        this.postCreatedByUserId = postCreatedByUserId;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPostCreationTime() {
        return postCreationTime;
    }

    public void setPostCreationTime(String postCreationTime) {
        this.postCreationTime = postCreationTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
