package com.android.mno.restrodrive.restrodrive.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.restrodrive.Helper.Restaurant;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RestaurantViewHolder>{

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView restoName;
        TextView restoStar;
        TextView restoAddress;
        ImageView restoPhoto;
        ImageView arrowDown;

        RestaurantViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            restoName = (TextView)itemView.findViewById(R.id.resto_name);
            restoStar = (TextView)itemView.findViewById(R.id.resto_star);
            restoAddress = (TextView)itemView.findViewById(R.id.resto_address);
            restoPhoto = (ImageView)itemView.findViewById(R.id.resto_photo);
            arrowDown = (ImageView)itemView.findViewById(R.id.drop_down);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(v.getContext(),"Let's Go to " + restoName.getText(), Toast.LENGTH_LONG).show();
                    if(restoStar.getVisibility() != View.VISIBLE) {
                        restoStar.setVisibility(View.VISIBLE);
                        restoName.setTextSize(30);
                        restoAddress.setVisibility(View.VISIBLE);
                        //restoPhoto.setVisibility(View.VISIBLE);
                        arrowDown.setRotation(180);

                    } else {
                        restoStar.setVisibility(View.GONE);
                        restoAddress.setVisibility(View.GONE);
                        //restoPhoto.setVisibility(View.GONE);
                        arrowDown.setRotation(360);
                    }
                }
            });
        }
    }
    List<Restaurant> restaurants;

    public RVAdapter(List<Restaurant> restaurants){
        this.restaurants = restaurants;
    }
    @Override
    public int getItemCount() {
        Log.d("RVAdapter", "Size: " + restaurants.size());
        return restaurants.size();
    }
    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resto_card_view, viewGroup, false);
        RestaurantViewHolder pvh = new RestaurantViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(RestaurantViewHolder RestoViewHolder, int i) {
        RestoViewHolder.restoName.setText(restaurants.get(i).restaurantName);
        RestoViewHolder.restoStar.setText(restaurants.get(i).restaurantStar);
        RestoViewHolder.restoAddress.setText(restaurants.get(i).restaurantAddress);
            RestoViewHolder.restoPhoto.setImageResource(restaurants.get(i).photoId);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}