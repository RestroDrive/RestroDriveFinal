package com.android.mno.restrodrive.restrodrive.View;

import android.os.Bundle;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.restrodrive.Adapters.RVAdapter;
import com.android.mno.restrodrive.restrodrive.Helper.Restaurant;
import com.android.mno.restrodrive.restrodrive.Utility.Utility;

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

        List<Restaurant> restaurants = Utility.getInstance().initializeRestaurantList();
        RVAdapter adapter = new RVAdapter(restaurants);
        rv.setAdapter(adapter);
    }
}
