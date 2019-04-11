package com.android.mno.restrodrive.restrodrive.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.restrodrive.Adapters.BusinessRecyclerAdapter;
import com.android.mno.restrodrive.restrodrive.Model.Business;
import com.android.mno.restrodrive.restrodrive.Model.MapAddress;
import com.android.mno.restrodrive.restrodrive.Utility.MapViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BusinessListFragment extends Fragment {

    private View fragmentView;
    private RecyclerView businessRecycleView;
    private List<Business> businesses;
    private static final String TAG = "BusinessListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView =  inflater.inflate(R.layout.business_list_fragment, container, false);

        businessRecycleView = fragmentView.findViewById(R.id.business_recycle_view);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        businessRecycleView.setLayoutManager(llm);

        BusinessRecyclerAdapter adapter = new BusinessRecyclerAdapter(businesses, getContext());
        //businessRecycleView.setAdapter(adapter);

        MapViewModel viewModel = ViewModelProviders.of(this).get(MapViewModel.class);

        viewModel.getBusinessLiveData().observe(this, new Observer<List<Business>>() {
            @Override
            public void onChanged(List<Business> businesses) {
                Log.e(TAG, "onChanged: Inside View Model" + businesses.get(0).getName());
                adapter.updatedBusinessList(businesses);
                adapter.notifyDataSetChanged();
            }
        });


        return fragmentView;
    }

}
