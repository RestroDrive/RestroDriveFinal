package com.android.mno.restrodrive.restrodrive.Model;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;
import java.util.ArrayList;

public class Business implements Serializable {

    private boolean isClosed;
    private String displayPhone;
    private String id;
    private String imageUrl;
    private Location location;
    private boolean isClaimed;
    private String url;
    private ArrayList<Category> categories;
    private ArrayList<Hour> hours;
    private double distance;
    private String phone;
    private String name;
    private String text;
    private String price;
    private int reviewCount;
    private double rating;
    private Coordinates coordinates;
    private ArrayList<String> photos;

    @JsonGetter("is_closed")
    public boolean getIsClosed() {
        return this.isClosed;
    }
    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    @JsonGetter("display_phone")
    public String getDisplayPhone() {
        return this.displayPhone;
    }
    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    @JsonGetter("id")
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter("image_url")
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonGetter("location")
    public Location getLocation() {
        return this.location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonGetter("phone")
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonGetter("is_claimed")
    public boolean getIsClaimed() {
        return this.isClaimed;
    }
    public void setIsClaimed(boolean isClaimed) {
        this.isClaimed = isClaimed;
    }

    @JsonGetter("url")
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonGetter("categories")
    public ArrayList<Category> getCategories() {
        return this.categories;
    }
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @JsonGetter("hours")
    public ArrayList<Hour> getHours() {
        return this.hours;
    }
    public void setHours(ArrayList<Hour> hours) {
        this.hours = hours;
    }

    @JsonGetter("distance")
    public double getDistance() {
        return this.distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @JsonGetter("name")
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("text")
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @JsonGetter("price")
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonGetter("review_count")
    public int getReviewCount() {
        return this.reviewCount;
    }
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    @JsonGetter("rating")
    public double getRating() {
        return this.rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    @JsonGetter("coordinates")
    public Coordinates getCoordinates() {
        return this.coordinates;
    }
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @JsonGetter("photos")
    public ArrayList<String> getPhotos() {
        return this.photos;
    }
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }
}
