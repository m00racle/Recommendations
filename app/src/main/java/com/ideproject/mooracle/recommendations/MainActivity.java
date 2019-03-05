package com.ideproject.mooracle.recommendations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import com.ideproject.mooracle.recommendations.Adapter.ListingsAdapter;
import com.ideproject.mooracle.recommendations.api.Etsy;
import com.ideproject.mooracle.recommendations.model.ActiveListings;

public class MainActivity extends AppCompatActivity {
    //constant string key for the saved instance
    private static final String STATE_ACTIVE_LISTINGS = "stateActiveListings";

    //field for views
    private RecyclerView recyclerView;
    private View progressBar;
    private TextView errorText;

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

        if (savedInstanceState == null){
            showLoading();
            Etsy.getActiveListings(adapter); //the adapter is the callback
        } else {
            if (savedInstanceState.containsKey(STATE_ACTIVE_LISTINGS)){
                adapter.success((ActiveListings) savedInstanceState.getParcelable(STATE_ACTIVE_LISTINGS), null);
                showList();
            } else {
                //reload the listings from Etsy API
                showLoading();
                Etsy.getActiveListings(adapter);
            }
        }

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
