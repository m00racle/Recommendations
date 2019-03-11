package com.ideproject.mooracle.recommendations.google;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
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
    // little mistakes here since I put apiClient as argument for constructor whilst it was already defined:
    public GoogleServicesHelper(Activity activity, GoogleServicesListener listener) {

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
        If it's available, we want to connect, and if it's not available, well, what we're gonna do is, if for some
        reason it was already connected, we can just handle this by saying, okay, listener, you're disconnected now.
        We weren't actually connected, but for the listener, the listener, remember, is the activity display in the UI.
        All they care about is it's not available.
     */
    public void connect(){
        //test if the Google Play services is available, if not tell the listener that it was onDisconnected state
        //the activity needs to know this to take appropriate actions later
        if (isGooglePlayServicesAvailable()) {
            apiClient.connect();
        } else {
            listener.onDisconnected();
        }
    }

    /*  Here we're gonna also check if it's not available, and if it is available, we want to go ahead and disconnect.
        Otherwise, we're gonna do the same thing and just say okay, hey, listener you're disconnected.
        Google Play services is not available, you need to go ahead and handle that situation.
     */
    public void disconnect(){
        //test if the Google Play services is available, if not tell the listener that it was onDisconnected state
        //the activity needs to know this to take appropriate actions later
        if (isGooglePlayServicesAvailable()) {
            apiClient.disconnect();
        } else {
            listener.onDisconnected();
        }
    }

    /*  And one thing to think about when we're writing code like this is that what happens if Google Play services
        isn't available, what should we do then? Well, we should check that and handle those cases to make sure our code
        doesn't crash, or throw a null pointer exception, or another error. So let's go ahead and add in a method, and
        what this is gonna do is just check if Google Play services is possibly available for us to connect to.
        */
    private boolean isGooglePlayServicesAvailable(){
        //check availability by using GoogleApiAvailability since the one in GoogleApiServicesUtil is already deprecated
        // you cannot instantiate directly GoogleApiAvailability instead you need to getInstance() to get static version
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

    /*  This is, we've connected. We're successful, so let's tell the user, yeah. You're connected.
        That's all we need to do here.
        */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        listener.onConnected();
    }

    /*  1:18
        Now, onConnectionSuspended, again, the listener, whatever's displaying the UI, doesn't really matter if it's
        suspended, or failed, or whatnot. It, all that matters is it's not available. So we're just gonna call through
        to onDisconnected.
    * */
    @Override
    public void onConnectionSuspended(int i) {
        listener.onDisconnected();
    }

    /*  1:32
        The final one here is onConnectionFailed. And this is gonna happen if the connection wasn't successful.
        It failed initially, but this method really nice because it returns back to us this ConnectionResult object,
        and this ConnectionResult object has a method on it called hasResolution. So again, ConnectionResult is part of
        Google Play services library and it has this method, hasResolution, so if there is resolution,
        we wanna go ahead and try to resolve that.
    * */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()){
            /*try to resolve this:
                2:19
                And earlier I said I created one called REQUEST_CODE_RESOLUTION. It had a unique value that was
                different than my availability one.*/
            try {
                connectionResult.startResolutionForResult(activity, REQUEST_CODE_RESOLUTION);
                /*  2:37
                    So, remember whenever you call to startActivityForResult, you're passing it in intent.
                    So, if this intent failed to send, we need to handle that case. So let's add a catch block,
                    and go ahead and handle this by supplying it the SendIntentException here.*/
            } catch (IntentSender.SendIntentException e){
                /*  3:02
                    And what we wanna do here is we're actually gonna call back to connect, and you might think that's
                    a little bit weird, but what we're doing here is, multiple errors might be coming in, and some of
                    them might resolve and some of them might not. But if one comes in and is resolved, but then
                    another comes in and is failed, well, maybe we can go back and try to connect again and
                    take a different path that time.
                * */
                connect();
            }
        } else {
            /*  3:43
                Finally we're gonna come to a case where, you know what, there is no resolution.
                We've come across too many errors. If all else fails, we will finally just wanna say,
                all right, listener, you're disconnected.
            * */
            listener.onDisconnected();
        }
    }

    /*  4:15
        Now we need to remember that we called multiple times these methods that are calling through to
        startActivityForResult. And that means that they're gonna come back in onActivityResult of our activity,
        so we need a way to handle and check what's actually coming back.
    * */
    public void handleActivityResult(int requestCode, int resultCode, Intent data){
        /*  4:58
            Now what we wanna do is just check, okay, well if it was one of our codes that we sent, we're gonna check
            the request codes here. So if the request code was either the resolution or the request code was our
            availability, okay, that means we're gonna handle these cases.
        * */
        if (requestCode == REQUEST_CODE_AVAILABILITY || requestCode == REQUEST_CODE_RESOLUTION){
            /*  5:31
                And if it was okay, that means some error is resolved. Okay, let's go ahead and try to connect again.
                Otherwise, if it wasn't okay, what we're just gonna do is say, all right, listener, you're disconnected.
            * */
            if (resultCode == activity.RESULT_OK){
                connect();
            } else {
                listener.onDisconnected();
            }
        }
    }
}
