package com.ideproject.mooracle.recommendations.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ideproject.mooracle.recommendations.MainActivity;
import com.ideproject.mooracle.recommendations.R;
import com.ideproject.mooracle.recommendations.api.Etsy;
import com.ideproject.mooracle.recommendations.google.GoogleServicesHelper;
import com.ideproject.mooracle.recommendations.model.ActiveListings;
import com.ideproject.mooracle.recommendations.model.Listing;
import com.squareup.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ListingsHolder>
        implements Callback<ActiveListings>, GoogleServicesHelper.GoogleServicesListener {
    /*  1:03
        So by default we're just gonna say false, and now this is gonna need to know when Google Play Services
        becomes available. So, in order to know that, we're gonna use the interface that we defined in our helper.
        So remember, let's go to our helper, and at the top we defined this interface called GoogleServicesListener,
        and it has a method on connected on disconnected and that's what our adapter wants to know is when it's
        connected and disconnected. So let's going ahead and have it implement that interface.
    * */

    private LayoutInflater inflater;
    private ActiveListings activeListings;
    private MainActivity activity;

    /*  0:24
        Let's handle the adapter first. Remember the adapter is what actually creates our views and displays them on
        the screen, and in on bind view holder, we can hide or show views as we need to, so this is what's really going
        to care about whether Google Play Services is available or is not available.
    * */
    private boolean isGooglePlayServicesAvailabe;

    //constructor
    public ListingsAdapter(MainActivity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        /*  0:41
            So let's go ahead and add in a way to store and know whether Google Play Services is available or not.
            Let's just store a boolean and we're going to call it, isGooglePlayServicesAvailable.
            By default, we're just going to assume it's not available. So that way, we make sure we handle that case.
        * */
        this.isGooglePlayServicesAvailabe = false;
    }

    @NonNull
    @Override
    public ListingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListingsHolder(inflater.inflate(R.layout.layout_listing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsHolder holder, int position) {
        //retrieve the listings object from the active listing and then bind them to the views in view holder
        Listing listing = activeListings.results[position];
        holder.titleTextView.setText(listing.title);
        holder.shopNameTextView.setText(listing.Shop.shop_name);
        holder.priceTextView.setText(listing.price);

        /*  2:37
            Okay and now that that's happening we can actually handle the cases where it's available and not available
            in our on bind view holder method, so up in our on bind view holder method, here we are. Right here,
            we're just gonna add a place where later on, we can check and display the UI in a different way.
        * */
        if (isGooglePlayServicesAvailabe){
            /*  todo: 2:56
                So we're just gonna put this in here and make sure we come back to it later on, but we'll have an
                opportunity to say, okay, if Google Play Services is available, we're gonna be able to display
                that +1 button once we've added it in to each item, and initialize it, initialize the share button,
                do whatever we need to do,
            * */
        }else {
            /*  todo: 3:12
                and we're also gonna handle the case where it's not available, and that's gonna be our else case.
            * */
        }

        //for image we must use Picasso library
        Picasso.with(holder.imageView.getContext())
                .load(listing.Images[0].url_570xN)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (activeListings == null)
        return 0;

        if (activeListings.results == null) return 0;

        return activeListings.results.length;
    }

    @Override
    public void success(ActiveListings activeListings, Response response) {
        this.activeListings = activeListings;
        notifyDataSetChanged();
        this.activity.showList();
    }

    @Override
    public void failure(RetrofitError error) {
        this.activity.showError();
    }

    public ActiveListings getActiveListings (){
        return activeListings;
    }

    /*  3:18
        Okay, and because the listening adapter is the thing that actually gets the data,
        holds our active listings object, displays the UI, let's go ahead and move the logic of loading,
        doing the load to the network, calling Etsy.getActiveListings. Let's move that into our adapter here.
    * */
    @Override
    public void onConnected() {
        //2:09
        //But if they're, Google Play Services is now available, we wanna display the options to plus one and share
        //to Google+. So, we're gonna call notifyDataSetChanged.
        //And this will cause this adapter and recycler  together to redraw those items.
        //*

        //3:48
        //So, what we're gonna do here is just check and if it connects but we don't have any items yet,
        //we can use getItemCount equals 0. That means we don't have any items yet. We're gonna go ahead and
        //now call to getActiveListings and we're gonna pass in this because remember the adapter itself was
        //the callback for retrofit.
        //*

        if (getItemCount() == 0){
            Etsy.getActiveListings(this);
        }
        isGooglePlayServicesAvailabe = true;
        notifyDataSetChanged();
    }

    @Override
    public void onDisconnected() {
        //2:25
        //And the same thing is gonna happen down here at on disconnected. We're just going to say it's false
        //now it's not available and we want to make sure that we update the recycler view items to reflect that.
        //*

        //4:07
        //And we're actually gonna do the same thing in disconnected as well.
        // So either way, we still wanna do the load to the API. We wanna call that network and get the response back.
        // So, in either case, either we're connected or disconnected,
        // but if we don't have any data to show anyway we need to go ahead and start a network request.
        if (getItemCount() == 0){
            Etsy.getActiveListings(this);
        }

        isGooglePlayServicesAvailabe = false;
        notifyDataSetChanged();
    }

    public class ListingsHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleTextView;
        public TextView shopNameTextView;
        public TextView priceTextView;

        public ListingsHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.listing_image);
            titleTextView = itemView.findViewById(R.id.listing_title);
            shopNameTextView = itemView.findViewById(R.id.listing_shop_name);
            priceTextView = itemView.findViewById(R.id.listing_price);
        }
    }
}
