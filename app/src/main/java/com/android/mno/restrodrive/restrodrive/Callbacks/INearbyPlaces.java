package com.android.mno.restrodrive.restrodrive.Callbacks;

import com.android.mno.restrodrive.restrodrive.Model.Business;
import com.android.mno.restrodrive.restrodrive.Helper.Filter;

import java.util.List;

/**
 * Interface for fetching POI
 */
public interface INearbyPlaces {

    void findNearbyPlaces(List<Business> businessArrayList);

}
