package com.ideproject.mooracle.recommendations.google;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * This class is build to make the activity free of dealing with Google API client classes.
 * The activity will only deal on implementation of when the API is connected or disconnected
 * Other than that all other methods such as onConnected, onConnectionSuspended which are part of Connection callbacks
 * interface in GoogleApiClient class plus onConnectionFailed method which is part of onConnectionFailed interface of
 * GoogleApiClient class will be handled in this class
 * */

public class GoogleServicesHelper implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{
    // make interface to control what happen onConnection and onDisconnection which must be implemented by activity
    public interface GoogleServicesListener {
        public void onConnected();
        public void onDisconnected();
    }

    // the field to be initialized in constructor:
    private GoogleApiClient apiClient;
    private Activity activity;
    private GoogleServicesListener listener;

    //constructor:


    public GoogleServicesHelper(GoogleApiClient apiClient, Activity activity, GoogleServicesListener listener) {

        this.activity = activity;
        this.listener = listener;
        /*
         * We can create an apiClient by using the convenient GoogleApiClient.Builder class,
            it requires us to pass in the context.We're gonna use the activity.
         */
        this.apiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this) //since this class is the one which handles this callbacks not activity
                .addOnConnectionFailedListener(this) //same as above resoning
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
