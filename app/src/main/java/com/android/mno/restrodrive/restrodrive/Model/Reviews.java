package com.android.mno.restrodrive.restrodrive.Model;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.io.Serializable;
import java.util.ArrayList;

public class Reviews implements Serializable {

    private int total;
    private ArrayList<Review> reviews;

    @JsonGetter("total")
    public int getTotal() {
        return this.total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    @JsonGetter("reviews")
    public ArrayList<Review> getReviews() {
        return this.reviews;
    }
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
