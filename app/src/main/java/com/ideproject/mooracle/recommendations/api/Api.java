package com.ideproject.mooracle.recommendations.api;

import com.ideproject.mooracle.recommendations.model.ActiveListings;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface Api {

    // using retrofit make abstract method on calling the activeListing from the api
    @GET("listings/active")
    void activeListings(@Query("includes") String includes,
                        Callback<ActiveListings> callback);
}
