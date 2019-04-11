package com.android.mno.restrodrive.restrodrive.View;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.restrodrive.Model.MapAddress;
import com.android.mno.restrodrive.restrodrive.Utility.MapViewModel;
import com.android.mno.restrodrive.restrodrive.Utility.Utility;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Fragment to choose source and destination using place autocomplete
 */
public class DirectionFragment extends Fragment implements View.OnClickListener {

    private final int AUTOCOMPLETE_SOURCE_REQUEST_CODE = 100;
    private final int AUTOCOMPLETE_DESTINATION_REQUEST_CODE = 200;
    private final String TAG = "DirectionFragment";

    private String sourceAddress = null;
    private String destinationAddress = null;
    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_direction, container, false);

        fragmentView.findViewById(R.id.source_text).setOnClickListener(this);
        fragmentView.findViewById(R.id.destination_text).setOnClickListener(this);
        fragmentView.findViewById(R.id.find_route_button).setOnClickListener(this);

        if(sourceAddress != null)
            ((TextView)fragmentView.findViewById(R.id.source_text)).setText(sourceAddress);

        return fragmentView;
    }

    public void setDefaultSource(String sourceAddress){
        this.sourceAddress = sourceAddress;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            Log.i(TAG, "Place: " + place.getAddress() + ", " + place.getId());

            if(requestCode == AUTOCOMPLETE_SOURCE_REQUEST_CODE){
                sourceAddress = place.getAddress();
                ((TextView)fragmentView.findViewById(R.id.source_text)).setText(sourceAddress);
            }else if(requestCode == AUTOCOMPLETE_DESTINATION_REQUEST_CODE){
                destinationAddress = place.getAddress();
                ((TextView)fragmentView.findViewById(R.id.destination_text)).setText(destinationAddress);
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.i(TAG, status.getStatusMessage());
        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.source_text){
            chooseDestination(AUTOCOMPLETE_SOURCE_REQUEST_CODE);
        }else if (id == R.id.destination_text){
            chooseDestination(AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
        }else if(id == R.id.find_route_button){
            findRoute();
        }
    }

    private void chooseDestination(int requestCode) {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME , Place.Field.ADDRESS);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getContext());
        startActivityForResult(intent, requestCode);
    }

    private void findRoute(){

        if(sourceAddress == null){
            Utility.getInstance().showSnackbar(fragmentView,"Enter valid source", getContext());
        }else if(destinationAddress == null){
            Utility.getInstance().showSnackbar(fragmentView,"Enter valid Destination", getContext());
        } else{

            MapViewModel viewModel = ViewModelProviders.of(getActivity()).get(MapViewModel.class);
            MapAddress mapAddress = new MapAddress(sourceAddress, destinationAddress);
            viewModel.selectedAddress(mapAddress);
        }
    }

}
