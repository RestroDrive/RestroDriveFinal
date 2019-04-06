package com.android.mno.restrodrive.restrodrive.Model;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResponse implements Serializable {

    private int total;
    private ArrayList<Business> businesses;

    @JsonGetter("businesses")
    public ArrayList<Business> getBusinesses() {
        return this.businesses;
    }
    public void setBusinesses(ArrayList<Business> businesses) {
        this.businesses = businesses;
    }

    @JsonGetter("total")
    public int getTotal() {
        return this.total;
    }

}
