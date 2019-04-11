package com.android.mno.restrodrive.restrodrive.Utility;

import com.android.mno.restrodrive.restrodrive.Model.MapAddress;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    private final MutableLiveData<MapAddress> selectedAddress = new MutableLiveData<>();

    public void selectedAddress (MapAddress mapAddress) {
        selectedAddress.setValue(mapAddress);
    }

    public LiveData<MapAddress> getSelectedAddress() {
        return selectedAddress;
    }
}
