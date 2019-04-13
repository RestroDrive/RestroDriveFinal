package com.android.mno.restrodrive.restrodrive.Helper;

import com.android.mno.restrodrive.restrodrive.Callbacks.YelpFusionApi;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yelp.fusion.client.connection.interceptors.ApiKeyInterceptor;
import com.yelp.fusion.client.exception.ErrorHandlingInterceptor;
import com.yelp.fusion.client.models.ApiKey;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Yelp API Factory class
 */
public class YelpFusionApiFactory {

    private static final String YELP_API_BASE_URL = "https://api.yelp.com";

    private OkHttpClient httpClient;
    private ApiKey apiKey;

    public YelpFusionApiFactory() {}

    public YelpFusionApi createAPI(String apiKey) throws IOException {
        this.apiKey = new ApiKey(apiKey, "Bearer");
        return getYelpFusionApi();
    }

    private YelpFusionApi getYelpFusionApi() {
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new ApiKeyInterceptor(apiKey))
                .addInterceptor(new ErrorHandlingInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getAPIBaseUrl())
                .addConverterFactory(getJacksonFactory())
                .client(this.httpClient)
                .build();
        return retrofit.create(YelpFusionApi.class);
    }

    private static JacksonConverterFactory getJacksonFactory(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return JacksonConverterFactory.create(mapper);
    }

    public String getAPIBaseUrl() {
        return YELP_API_BASE_URL;
    }

}
