package com.android.mno.restrodrive.restrodrive.Helper;

import com.android.mno.restrodrive.restrodrive.Callbacks.INearbyPlaces;
import com.android.mno.restrodrive.restrodrive.Utility.Utility;
import com.android.mno.restrodrive.restrodrive.View.MapActivity;

public class PlacesHelper {

    private static PlacesHelper placesHelperInstance = null;
    private String businessType = "Restaurant";
    private String subBusinessType = "Indian";

    private PlacesHelper() {}

    public static PlacesHelper getInstance(){
        if(placesHelperInstance == null)
            placesHelperInstance = new PlacesHelper();

        return placesHelperInstance;
    }

    public void findPlaces(double lat, double lng, INearbyPlaces iNearbyPlaces){

        YelpApiCall yelpApiCall = new YelpApiCall(iNearbyPlaces);

        Filter filter = new Filter();
        filter.setBusinessType(businessType);
        filter.setGetBusinessSubType(subBusinessType);

        yelpApiCall.yelpApiBusinessSearch(lat,lng, filter);
    }

}
