package com.ideproject.mooracle.recommendations.api;


import com.ideproject.mooracle.recommendations.model.ActiveListings;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class Etsy {
    private static final String API_KEY = "dy8ev4xxs7t2xf9fbnpq49al";

    // build request interceptor to input API_KEY when calling rest api
    private static RequestInterceptor getInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addEncodedQueryParam("api_key", API_KEY);
            }
        };
    }

    // build getApi method using RestAdapter builder class
    private static Api getApi(){
        return new RestAdapter.Builder()
                .setEndpoint("https://openapi.etsy.com/v2")
                .setRequestInterceptor(getInterceptor()) //this setRequestInterceptor to use above method to insert api key
                .build()
                .create(Api.class);
    }

    public static void getActiveListings(Callback<ActiveListings> callback){
        getApi().activeListings("Shop,Images", callback);
    }
}
