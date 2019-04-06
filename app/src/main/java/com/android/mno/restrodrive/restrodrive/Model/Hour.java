package com.android.mno.restrodrive.restrodrive.Model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.yelp.fusion.client.models.Open;

import java.io.Serializable;
import java.util.ArrayList;

public class Hour implements Serializable {

    private boolean isOpenNow;
    private String hoursType;
    private ArrayList<Open> open;

    @JsonGetter("is_open_now")
    public boolean getIsOpenNow() {
        return this.isOpenNow;
    }
    public void setIsOpenNow(boolean isOpenNow) {
        this.isOpenNow = isOpenNow;
    }

    @JsonGetter("hours_type")
    public String getHoursType() {
        return this.hoursType;
    }
    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    @JsonGetter("open")
    public ArrayList<Open> getOpen() {
        return this.open;
    }
    public void setOpen(ArrayList<Open> open) {
        this.open = open;
    }

}

