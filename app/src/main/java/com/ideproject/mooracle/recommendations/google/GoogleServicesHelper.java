package com.ideproject.mooracle.recommendations.google;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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

    //static constant request codes:
    private static final int REQUEST_CODE_RESOLUTION = -100;
    private static final int REQUEST_CODE_AVAILABILITY = -101;

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

    /*  According to the documentation, you should initiate talking to Google Play services by calling connect in on start
        and disconnect in on stop of your activity.
     */
    public void connect(){
        apiClient.connect();
    }
    public void disconnect(){
        apiClient.disconnect();
    }

    /*  And one thing to think about when we're writing code like this is that what happens if Google Play services
        isn't available, what should we do then? Well, we should check that and handle those cases to make sure our code
        doesn't crash, or throw a null pointer exception, or another error. So let's go ahead and add in a method, and
        what this is gonna do is just check if Google Play services is possibly available for us to connect to.
        */
    private boolean isGooglePlayServicesAvailable(){
        //check availability by using GoogleApiAvailability since the one in GoogleApiServicesUtil is already deprecated
        // you cannot instantiate directly GoogleApiAvailability instead you need to getInstace() to get static version
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int availability = apiAvailability.isGooglePlayServicesAvailable(activity);

        //test case using switch for all possible availability results:
        switch (availability){
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
                //get the error dialog for the user:
                apiAvailability.getErrorDialog(activity, availability, REQUEST_CODE_AVAILABILITY).show();
                return false;

                default: return false;
        }
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
