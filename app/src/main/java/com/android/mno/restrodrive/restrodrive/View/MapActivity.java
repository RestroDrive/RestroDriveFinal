package com.android.mno.restrodrive.restrodrive.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.mno.restrodrive.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/*import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;*/

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback {

    private static final String TAG = "MapActivity";

    //permissions
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    // Google Maps API Key
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyBRGMS7YNI2_oXvPlBn7p7f3rSlAoT74WI";

    //variables
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProvideClient;

    //widgets
    private EditText mOriginSearchText;
    private EditText mDestinationSearchText;

    // Longitudes and Latitudes
    LatLng mOriginCoordinates;
    LatLng mDestinationCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mOriginSearchText = findViewById(R.id.origin_search_input);
        mDestinationSearchText = findViewById(R.id.dest_search_input);

        findViewById(R.id.btnRestaurant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPlaces();
            }
        });

        getLocationPermission();
        init();

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
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseAuth.getInstance().signOut();
                    finish();

                }
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

        mOriginSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    final String searchString = mOriginSearchText.getText().toString();
                    //mOriginSearchText.setText(searchString);

                    mOriginCoordinates = geoLocateLocation(searchString);

                    if (mOriginCoordinates == null) {
                        Toast.makeText(MapActivity.this, "Place Not Found", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    moveCamera(mOriginCoordinates, DEFAULT_ZOOM);
                }
                return false;
            }
        });

        mDestinationSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    // Find direction to destination from origin
                    final String searchString = mDestinationSearchText.getText().toString();

                    mDestinationCoordinates = geoLocateLocation(searchString);

                    if (mDestinationCoordinates == null) {
                        Toast.makeText(MapActivity.this, "Destination Not Found", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    showDirection(mOriginCoordinates, mDestinationCoordinates);
                }
                return false;
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


            for (LatLng position : sectionPositionList) {
                mMap.addMarker(new MarkerOptions().position(position));
            }

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

    private void getLocationPermission() {
        String[] permissions = {ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
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
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
                    //initialize our map
                    initMap();
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

    /**
     * Shows nearby places
     */
    private void showPlaces(){

        // Initialize Places.
        Places.initialize(getApplicationContext(), GOOGLE_MAPS_API_KEY);

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG,
                Place.Field.RATING, Place.Field.TYPES, Place.Field.PHOTO_METADATAS);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                        Log.e(TAG, "Place Name "+
                                placeLikelihood.getPlace().getName());
                        Log.e(TAG,         "Place Address "+
                                placeLikelihood.getPlace().getAddress());
                        Log.e(TAG,       "Place LatLang "+
                                placeLikelihood.getPlace().getLatLng());
                        Log.e(TAG,      "Place Rating "+
                                placeLikelihood.getPlace().getRating());
                        Log.e(TAG,    "Place Types "+
                                placeLikelihood.getPlace().getTypes());
                        Log.e(TAG,   "Place Photo "+
                                placeLikelihood.getPlace().getPhotoMetadatas());
                        Log.e(TAG,    "Place Likehood "+
                                placeLikelihood.getLikelihood());

                        Log.d(TAG, "---------------");
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            getLocationPermission();
        }
    }
}
