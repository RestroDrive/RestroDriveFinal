package com.android.mno.restrodrive.restrodrive.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.restrodrive.Helper.Restaurant;
import com.android.mno.restrodrive.restrodrive.Model.Business;
import com.android.mno.restrodrive.restrodrive.View.BusinessListFragment;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BusinessRecyclerAdapter extends RecyclerView.Adapter<BusinessRecyclerAdapter.BusinessViewHolder>{

    private List<Business> businessesList;
    private Context context;

    public static class BusinessViewHolder extends RecyclerView.ViewHolder {
        CardView businessCardView;
        TextView businessName;
        TextView businessStar;
        TextView businessAddress;
        ImageView businessPhoto;
        ImageView arrowDown;

        BusinessViewHolder(View itemView) {
            super(itemView);
            businessCardView = itemView.findViewById(R.id.business_card_view);
            businessName = itemView.findViewById(R.id.resto_name);
            businessStar = itemView.findViewById(R.id.resto_star);
            businessAddress = itemView.findViewById(R.id.resto_address);
            businessPhoto = itemView.findViewById(R.id.resto_photo);
            arrowDown = itemView.findViewById(R.id.drop_down);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(v.getContext(),"Let's Go to " + businessName.getText(), Toast.LENGTH_LONG).show();
                    if(businessStar.getVisibility() != View.VISIBLE) {
                        businessStar.setVisibility(View.VISIBLE);
                        businessName.setTextSize(30);
                        businessAddress.setVisibility(View.VISIBLE);
                        businessPhoto.setVisibility(View.VISIBLE);
                        arrowDown.setRotation(180);

                    } else {
                        businessStar.setVisibility(View.GONE);
                        businessAddress.setVisibility(View.GONE);
                        businessPhoto.setVisibility(View.GONE);
                        arrowDown.setRotation(360);
                    }
                }
            });
        }
    }

    public BusinessRecyclerAdapter(List<Business> businessesList, Context context){
        this.businessesList = businessesList;
        this.context = context;
    }

    public void updatedBusinessList(List<Business> businessesList){
        this.businessesList = businessesList;
    }

    @Override
    public int getItemCount() {
        Log.d("BusinessRecyclerAdapter", "Size: " + businessesList.size());
        return businessesList.size();
    }
    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resto_card_view, viewGroup, false);
        BusinessViewHolder businessViewHolder = new BusinessViewHolder(v);
        return businessViewHolder;
    }
    @Override
    public void onBindViewHolder(BusinessViewHolder businessViewHolder, int i) {

        businessViewHolder.businessName.setText(businessesList.get(i).getName());
        businessViewHolder.businessStar.setText(businessesList.get(i).getReviewCount());

        String address = businessesList.get(i).getLocation().getAddress1();
        businessViewHolder.businessAddress.setText(address);
        Glide.with(context).load(businessesList.get(i).getImageUrl())
                .into(businessViewHolder.businessPhoto);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}