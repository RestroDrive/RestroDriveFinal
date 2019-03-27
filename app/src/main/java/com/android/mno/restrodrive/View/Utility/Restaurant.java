package com.android.mno.restrodrive.View.Utility;

import java.io.Serializable;

public class Restaurant implements Serializable {

    public String restaurantName;
    public String restaurantAddress;
    public String restaurantStar;
    public int photoId;

    public Restaurant(String restaurantName, String restaurantAddress, String restaurantStar, int photoId) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantStar = restaurantStar;
        this.photoId = photoId;
    }
}
