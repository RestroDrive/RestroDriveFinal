package com.android.mno.restrodrive.restrodrive.Helper;

import android.util.Log;

import com.android.mno.restrodrive.restrodrive.INearbyPlaces;
import com.android.mno.restrodrive.restrodrive.Model.Business;
import com.android.mno.restrodrive.restrodrive.Model.Category;
import com.android.mno.restrodrive.restrodrive.Model.Coordinates;
import com.android.mno.restrodrive.restrodrive.Model.Hour;
import com.android.mno.restrodrive.restrodrive.Model.Location;
import com.android.mno.restrodrive.restrodrive.Model.Review;
import com.android.mno.restrodrive.restrodrive.Model.Reviews;
import com.android.mno.restrodrive.restrodrive.Model.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class for searching places using Yelp API
 */
public class YelpApiCall implements INearbyPlaces {

    private static final String TAG = "YelpApiCall";
    private YelpFusionApiFactory yelpFusionApiFactory;
    private YelpFusionApi yelpFusionApi;
    private ArrayList<Business> businessArrayList;

    private String yelpApiKey = "L_xfihdZKMoy9sGtkjJRFlBZDn-TUvu5l4CmO2FUUEuQCJBVI96RTbOAq1kxlLjwnMyn5FfbKQAgHcnJM_jG4Z9mBS9rfKkjvXtIugR1IA9DDgokLxBV9ktt2BSmXHYx";

    public YelpApiCall(){

        try {
            yelpFusionApiFactory = new YelpFusionApiFactory();
            yelpFusionApi = yelpFusionApiFactory.createAPI(yelpApiKey);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to search businesses according to filler
     * @param lat
     * @param lon
     * @param filter
     */
    private List<Business> yelpApiBusinessSearch(Double lat, Double lon, Filter filter){

        Map<String, String> parms = new HashMap<>();
        parms.put("term", filter.getBusinessType()+" "+filter.getGetBusinessSubType());
        parms.put("latitude", Double.toString(lat));
        parms.put("longitude", Double.toString(lon));
        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(parms);

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();

                int totalNumberOfResult = searchResponse.getTotal();

                businessArrayList = searchResponse.getBusinesses();

                Log.e(TAG, "totalNumberOfResult - "+totalNumberOfResult);
                Log.d(TAG, "-----------------");

                for(int i =0 ; i<businessArrayList.size(); i++){
                    String businessName = businessArrayList.get(i).getName();
                    String displayPhone = businessArrayList.get(i).getDisplayPhone();
                    boolean closed = businessArrayList.get(i).getIsClosed();
                    String imageUrl = businessArrayList.get(i).getImageUrl();
                    Location location = businessArrayList.get(i).getLocation();
                    String phone = businessArrayList.get(i).getPhone();
                    boolean claimed = businessArrayList.get(i).getIsClaimed();
                    String url = businessArrayList.get(i).getUrl();
                    ArrayList<Category> categoryList = businessArrayList.get(i).getCategories();
                    ArrayList<Hour> hourList = businessArrayList.get(i).getHours();
                    double distance = businessArrayList.get(i).getDistance();
                    String text = businessArrayList.get(i).getText();
                    String price = businessArrayList.get(i).getPrice();
                    int reviewCount = businessArrayList.get(i).getReviewCount();
                    double rating = businessArrayList.get(i).getRating();
                    Coordinates coordinates = businessArrayList.get(i).getCoordinates();
                    ArrayList<String> photoList = businessArrayList.get(i).getPhotos();


                    Log.e(TAG, "businessName - "+businessName);
                    Log.e(TAG, "displayPhone - "+displayPhone);
                    Log.e(TAG, "closed - "+closed);
                    Log.e(TAG, "imageUrl - "+imageUrl);
                    Log.e(TAG, "location - "+location.getCity());
                    Log.e(TAG, "phone - "+phone);
                    Log.e(TAG, "claimed - "+claimed);
                    Log.e(TAG, "url - "+url);
                    Log.e(TAG, "categoryList - "+categoryList.get(0).getTitle());
                    //Log.e(TAG, "hourList - "+hourList.get(0).getIsOpenNow());
                    Log.e(TAG, "distance - "+distance);
                    Log.e(TAG, "text - "+text);
                    Log.e(TAG, "price - "+price);
                    Log.e(TAG, "reviewCount - "+reviewCount);
                    Log.e(TAG, "rating - "+rating);
                    Log.e(TAG, "coordinates - "+coordinates.getLatitude());
                    //Log.e(TAG, "photoList - "+photoList.get(0));

                    yelpApiBusinessReviews(businessArrayList.get(i).getId(), businessName);

                    Log.d(TAG, "-----------------");
                }

                getList(businessArrayList);
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.e(TAG, "t - "+t);
            }
        };

        call.enqueue(callback);

        return null;
    }

    private void yelpApiBusinessReviews(String id, String businessName){

        Call<Reviews> call = yelpFusionApi.getBusinessReviews(id);

        Callback<Reviews> callback = new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {

                Reviews reviews = response.body();

                try {

                    int totalReviews = reviews.getTotal();
                    ArrayList<Review> reviewArrayList = reviews.getReviews();

                    Log.e(TAG, "businessName - "+businessName);
                    Log.e(TAG, "totalReviews - "+totalReviews);
                    Log.e(TAG, "review text - "+reviewArrayList.get(0).getText());
                    Log.d(TAG, "...");

                }catch(Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.e(TAG, "t - "+t);
            }
        };


        call.enqueue(callback);
    }


    @Override
    public List<Business> getNearbyPlaces(double lat, double lon, Filter filter) {

        return yelpApiBusinessSearch(lat, lon, filter);
    }

    private List<Business> getList(List<Business> business){

        return business;
    }
}
