package com.ideproject.mooracle.recommendations.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ideproject.mooracle.recommendations.R;
import com.ideproject.mooracle.recommendations.model.ActiveListings;
import com.ideproject.mooracle.recommendations.model.Listing;
import com.squareup.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ListingsHolder>
        implements Callback<ActiveListings> {
    private LayoutInflater inflater;

    private ActiveListings activeListings;
    public ListingsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
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
    }

    @Override
    public void failure(RetrofitError error) {

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
