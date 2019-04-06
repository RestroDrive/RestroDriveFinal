package com.android.mno.restrodrive.restrodrive.Helper;

import com.android.mno.restrodrive.restrodrive.Model.Business;
import com.android.mno.restrodrive.restrodrive.Model.Reviews;
import com.android.mno.restrodrive.restrodrive.Model.SearchResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface YelpFusionApi {

    @GET("/v3/businesses/search")
    Call<SearchResponse> getBusinessSearch(@QueryMap Map<String, String> params);

    @GET("/v3/businesses/{id}")
    Call<Business> getBusiness(@Path("id") String id);

    @GET("/v3/businesses/{id}/reviews")
    Call<Reviews> getBusinessReviews(@Path("id") String id);
}
