package com.android.mno.restrodrive.restrodrive.View;

import android.Manifest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.restrodrive.Callbacks.INearbyPlaces;
import com.android.mno.restrodrive.restrodrive.Helper.Filter;
import com.android.mno.restrodrive.restrodrive.Helper.FirebaseLogin;
import com.android.mno.restrodrive.restrodrive.Helper.YelpApiCall;
import com.android.mno.restrodrive.restrodrive.Model.Business;
import com.android.mno.restrodrive.restrodrive.Model.MapAddress;
import com.android.mno.restrodrive.restrodrive.Utility.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback,
        INearbyPlaces, View.OnClickListener {

    private static final String TAG = "MapActivity";

    //permissions
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final float ZOOM_OUT_TO = 11f;

    // Google Maps API Key
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyBRGMS7YNI2_oXvPlBn7p7f3rSlAoT74WI";

    //variables
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProvideClient;

    private LatLng mOriginCoordinates;
    private FragmentManager fragmentManager;

    private String DIRECTION_FRAGMENT = "DirectionFragment";
    private String MAP_FRAGMENT = "MapFragment";

    private final int GPS_REQUEST_CODE = 300;
    private double currentLat;
    private double currentLng;

    private String businessType = "Restaurant";
    private String subBusinessType = "Indian";

    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        init();

        // Call for location if GPS is enable
        askForLocationPermission();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                return true;
            case R.id.help:
                return true;
            case R.id.about:
                return true;
            case R.id.sign_out:
                FirebaseLogin firebaseLogin = new FirebaseLogin(this);
                firebaseLogin.signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    /*
    For making keyboard enter key as Done deal
     */
    private void init(){
        Log.d(TAG, "init: initializing");

        findViewById(R.id.destination_fb_button).setOnClickListener(this);

        Places.initialize(getApplicationContext(), GOOGLE_MAPS_API_KEY);

        fragmentManager = getSupportFragmentManager();
        mapFragment = SupportMapFragment.newInstance();

        MapViewModel viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        viewModel.getSelectedAddress().observe(this, new Observer<MapAddress>() {
            @Override
            public void onChanged(MapAddress mapAddress) {

                switchToMapFragment();
                pointToSource(mapAddress.getSourceAddress());
                pointToDestination(mapAddress.getDestinationAddress());
            }
        });
    }

    private void showDirection(LatLng origin, LatLng destination) {
        if (origin == null || destination == null) {
            Toast.makeText(this, "Direction cannot be calculated", Toast.LENGTH_SHORT).show();
            return;
        }

        GoogleDirection.withServerKey(GOOGLE_MAPS_API_KEY).from(origin).to(destination).execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            Leg leg = route.getLegList().get(0);
            ArrayList<LatLng> sectionPositionList = leg.getSectionPoint();

            /*for (LatLng position : sectionPositionList) {
                mMap.addMarker(new MarkerOptions().position(position));
            }*/

            List<Step> stepList = leg.getStepList();
            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);

            for (PolylineOptions polylineOption : polylineOptionList) {
                mMap.addPolyline(polylineOption);
            }
            setCameraWithCoordinationBounds(route);

        } else {
            Log.d(TAG, "DNot: " + rawBody);
            Toast.makeText(MapActivity.this, "Direction Not Found", Toast.LENGTH_LONG).show();
        }
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }

    private LatLng geoLocateLocation(String locationAddress) {
        Log.d(TAG, "geoLocateLocation: geolocating " + locationAddress);

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(locationAddress,1);
        } catch (IOException e){
            Log.e(TAG, "geoLocateLocation: IO Exception" + e.getMessage());
        }

        if (list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocateLocation: found a location" + address.toString());
            return new LatLng(address.getLatitude(), address.getLongitude());
        }

        return null;
    }

    private void askForLocationPermission() {
        String[] permissions = {ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;

                checkGPSEnable();
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    private void initMap(){

        SupportMapFragment mapFragment = switchToMapFragment();

        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE :{
                if (grantResults.length>0){

                    for (int i = 0; i< grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;

                    checkGPSEnable();
                }
            }
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: lets have the device location");

        mFusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(this);

        try{

            if (mLocationPermissionGranted){
                Task<Location> location = mFusedLocationProvideClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: device location found");
                            Location currentLocation = task.getResult();

                            try {

                                currentLat = currentLocation.getLatitude();
                                currentLng = currentLocation.getLongitude();

                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else {
                            Log.d(TAG, "onComplete: Unable to find device location");
                            Toast.makeText(MapActivity.this,"Unable to find location, please check your data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: Security exception"+ e.getMessage() );
        }
    }

    private void moveCamera(@NonNull LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to " + latLng.latitude + "long," + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public void nextClick(View v) {
        Intent i = new Intent(this, RestaurantListActivity.class);

        startActivity(i);
    }

    @Override
    public void getNearbyPlaces(List<Business> businessArrayList) {

        String businessName = businessArrayList.get(0).getName();

        Log.e(TAG, "In MapActivity first businessName - "+businessName);

        for(int i =0 ; i<businessArrayList.size(); i++){

            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(businessArrayList.get(i).getCoordinates().getLatitude(),
                            businessArrayList.get(i).getCoordinates().getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(businessArrayList.get(i).getName()));
        }

        //mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_OUT_TO));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.e(TAG, "Marker clicked - "+marker.getTitle());

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.destination_fb_button) {

            switchToDirectionFragment(findCurrentAddressUsingLatLng());
        }
    }

    private String findCurrentAddressUsingLatLng() {

        String currentAddress = null;

        try {
            List<Address> addresses;
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(currentLat, currentLng, 1);

            currentAddress = addresses.get(0).getAddressLine(0);

        }catch (Exception e){
            e.printStackTrace();
        }

        return currentAddress;
    }

    private void switchToDirectionFragment(String currentAddress){

        DirectionFragment directionFragment = new DirectionFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_layout, directionFragment, DIRECTION_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();

        directionFragment.setDefaultSource(currentAddress);

        findViewById(R.id.btn_next).setVisibility(View.GONE);
        findViewById(R.id.destination_fb_button).setVisibility(View.GONE);
    }

    private SupportMapFragment switchToMapFragment(){

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_layout, mapFragment, MAP_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();

        findViewById(R.id.btn_next).setVisibility(View.VISIBLE);
        findViewById(R.id.destination_fb_button).setVisibility(View.VISIBLE);

        return mapFragment;
    }

    private void pointToSource(String sourceAddress){

        if(sourceAddress != null)
            mOriginCoordinates = geoLocateLocation(sourceAddress);

        if (mOriginCoordinates == null) {
            Toast.makeText(MapActivity.this, "Place Not Found", Toast.LENGTH_LONG).show();
        }

        //moveCamera(mOriginCoordinates, DEFAULT_ZOOM);
    }

    private void pointToDestination(String destinationAddress){

        LatLng mDestinationCoordinates = geoLocateLocation(destinationAddress);

        if (mDestinationCoordinates == null) {
            Toast.makeText(MapActivity.this, "Destination Not Found", Toast.LENGTH_LONG).show();
        }

        showDirection(mOriginCoordinates, mDestinationCoordinates);

        moveCamera(mDestinationCoordinates, ZOOM_OUT_TO);

        findPlaces(mDestinationCoordinates.latitude, mDestinationCoordinates.longitude);
    }

    private void findPlaces(double lat, double lng){

        YelpApiCall yelpApiCall = new YelpApiCall(MapActivity.this);

        Filter filter = new Filter();
        filter.setBusinessType(businessType);
        filter.setGetBusinessSubType(subBusinessType);

        yelpApiCall.yelpApiBusinessSearch(lat,lng, filter);
    }

    public void checkGPSEnable() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            //Initialize the Map
            initMap();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(
                                new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_REQUEST_CODE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult");

        if (requestCode == GPS_REQUEST_CODE) {

            //Initialize the Map
            initMap();

        }
    }
}
