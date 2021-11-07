package com.example.whatsthemove.Home;

import java.io.Serializable;

public class MyFeedItemList {
    int img;
    String postedBy;
    String time;
    String location;
    String description;

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public MyFeedItemList(int img, String postedBy, String time, String location, String description) {
        this.img = img;
        this.postedBy = postedBy;
        this.time = time;
        this.location = location;
        this.description = description;
    }
}
