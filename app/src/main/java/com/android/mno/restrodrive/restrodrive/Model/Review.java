package com.android.mno.restrodrive.restrodrive.Model;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;

public class Review implements Serializable {

    private String text;
    private User user;
    private int rating;
    private String timeCreated;
    private String url;

    @JsonGetter("text")
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @JsonGetter("user")
    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @JsonGetter("rating")
    public int getRating() {
        return this.rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    @JsonGetter("time_created")
    public String getTimeCreated() {
        return this.timeCreated;
    }
    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    @JsonGetter("url")
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
