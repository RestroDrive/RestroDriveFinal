package com.android.mno.restrodrive.restrodrive;

import com.android.mno.restrodrive.restrodrive.Model.Business;
import com.android.mno.restrodrive.restrodrive.Helper.Filter;

import java.util.List;

/**
 * Interface for fetching POI
 */
public interface INearbyPlaces {

    List<Business> getNearbyPlaces(double lat, double lon, Filter filter);

}
