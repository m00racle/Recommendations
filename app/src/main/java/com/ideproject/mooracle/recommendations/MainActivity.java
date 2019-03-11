package com.ideproject.mooracle.recommendations;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import com.ideproject.mooracle.recommendations.Adapter.ListingsAdapter;
import com.ideproject.mooracle.recommendations.api.Etsy;
import com.ideproject.mooracle.recommendations.google.GoogleServicesHelper;
import com.ideproject.mooracle.recommendations.model.ActiveListings;

public class MainActivity extends AppCompatActivity {
    //constant string key for the saved instance
    private static final String STATE_ACTIVE_LISTINGS = "stateActiveListings";

    //field for views
    private RecyclerView recyclerView;
    private View progressBar;
    private TextView errorText;

    //4:29
    //Okay, now that we've moved the network request into the adaptor and the adaptor handles when
    // Google Play Services is available or not available.
    // We need to go ahead and modify our activity to account for this.
    // So, let's go to our activity and when we initialize our activity we were initializing a calling here into API.
    // Well we don't wanna do that anymore. We want to now create a Google Services helper object instead and initialize it.
    private GoogleServicesHelper googleServicesHelper;

    //fields for adapters to be used when bundling with retrofitted API
    ListingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prepares views by ids
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        errorText = findViewById(R.id.error_text);

        //set up the recycler view
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ListingsAdapter(this);
        recyclerView.setAdapter(adapter);

        //5:04
        //So we create our googleServicesHelper object, and then we're always going to initialize it.
        // And we need to initialize it after we create our adapter.
        // Because remember, our adapter is the listener for our googleServicesHelper.
        googleServicesHelper = new GoogleServicesHelper(this, adapter);

        //5:35
        //And now we can remove the call to getActiveListings, because remember, Google Services helper will call back
        // to the adapters on connected or on disconnected, and that will start the load to the API. So, in either case,
        // we don't need to do that in the activity anymore. All we really need to do is just show loading.
        // So let's go ahead and move that in here. We're just gonna say, show loading by default always.
        showLoading();

        if (savedInstanceState != null){
            //5:59
            //And now, we're just gonna move this check to say, okay. If the savings and state was not available and,
            // we did have some saved data. We're gonna go ahead and call through to success.
            // And if we look at success, we're gonna see it, it, it's actually calling through to show the list already.
            // So, we don't even, we don't even need that anymore. Let's go ahead and remove that as well.
            if (savedInstanceState.containsKey(STATE_ACTIVE_LISTINGS)){
                adapter.success((ActiveListings) savedInstanceState.getParcelable(STATE_ACTIVE_LISTINGS), null);
                showList();
            }
        }

    }

    //6:22
    //Okay, and now that we have our googleServicesHelper object created, we need to go ahead and connect or disconnect,
    // and to do that, remember from the documentation, we need to do this in our onStart and onStop methods.
    // So, let's go ahead and get those methods in here.
    @Override
    protected void onStart() {
        super.onStart();
        //6:55
        //And in onStart we want to connect.
        googleServicesHelper.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //6:53
        //And onStop you wanna disconnect.
        googleServicesHelper.disconnect();
    }

    //7:07
    //Okay and finally we need to handle those cases where we called through to resolve an error or check availability
    // with the googleServicesHelper. And when doing that, remember it would call back to
    // the activities onActivityResult, so we need to add in that method as well.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //7:28
        //So onActivityResult, here, yep, and we're going to simply pass that through to our helpers method.
        // Called handleActivityResult. And we just, remember we were passing in the exact same parameters that were
        // coming through here.
        googleServicesHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActiveListings activeListings = adapter.getActiveListings();
        if (activeListings != null){
            outState.putParcelable(STATE_ACTIVE_LISTINGS, activeListings);
        }
    }

    private void showLoading(){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
    }

    public void showList(){
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }

    public void showError(){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
    }
}
