package com.android.mno.restrodrive.restrodrive.Utility;

import com.android.mno.restrodrive.restrodrive.Model.Business;
import com.android.mno.restrodrive.restrodrive.Model.MapAddress;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    //For passing selected addresses to MapActivity
    private final MutableLiveData<MapAddress> selectedAddressLiveData = new MutableLiveData<>();

    public void selectedAddress (MapAddress mapAddress) {
        selectedAddressLiveData.setValue(mapAddress);
    }

    public LiveData<MapAddress> getSelectedAddress() {
        return selectedAddressLiveData;
    }


    //For passing Business object list to BusinessFragment
    private final MutableLiveData<List<Business>> businessLiveData = new MutableLiveData<>();

    public void setBusinessLiveData (List<Business> business) {
        businessLiveData.setValue(business);
    }

    public LiveData<List<Business>> getBusinessLiveData() {
        return businessLiveData;
    }
}
