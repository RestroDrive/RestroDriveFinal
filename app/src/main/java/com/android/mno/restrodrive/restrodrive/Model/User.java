package com.android.mno.restrodrive.restrodrive.Model;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;

public class User implements Serializable {

    private String imageUrl;
    private String name;

    @JsonGetter("image_url")
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonGetter("name")
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

