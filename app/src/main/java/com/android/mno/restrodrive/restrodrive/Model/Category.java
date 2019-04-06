package com.android.mno.restrodrive.restrodrive.Model;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;

public class Category implements Serializable {

    private String title;
    private String alias;

    @JsonGetter("title")
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonGetter("alias")
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

}
