package com.android.mno.restrodrive.View.View;

import android.content.Intent;
import android.os.Bundle;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.View.Adapters.RVAdapter;
import com.android.mno.restrodrive.View.Utility.Constants;
import com.android.mno.restrodrive.View.Utility.Restaurant;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto_list_view_demo);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        //initializeData();
        Intent intent = getIntent();
        Object object = intent.getSerializableExtra(Constants.RestaurantData);
        List<Restaurant> restaurants = (List<Restaurant>)object;

        RVAdapter adapter = new RVAdapter(restaurants);
        rv.setAdapter(adapter);

    }

    private List<Restaurant> restaurants;

    private void initializeData(){
        restaurants = new ArrayList<Restaurant>();
        restaurants.add(new Restaurant("Persis Biryani", "1665 Stelton Rd, Piscataway Township, NJ 08854", "4.8 Star", R.drawable.veg));
        restaurants.add(new Restaurant("Paradise Biryani", "1980 NJ-27, North Brunswick Township, NJ 08902", "4.3 Star", R.drawable.veg));
        restaurants.add(new Restaurant("Bawarchi Biryani", "3201 NJ-27, Franklin Park, NJ 08823", "4.1 Star", R.drawable.veg));
    }

}
