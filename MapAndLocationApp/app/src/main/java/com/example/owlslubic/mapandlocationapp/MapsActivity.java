package com.example.owlslubic.mapandlocationapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
private static final String TAG = "MapsActivity";
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    GoogleApiClient mApiClient = null;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get instance of google api client
        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);




            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onMapReady: permission granted");


//                mApiClient.connect();

                mMap.setMyLocationEnabled(true);

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);

                if (mLastLocation != null) {
                    LatLng yrLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(yrLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(yrLocation)
                            .title("Your Current Position")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);


                }
            }


//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "onMapReady: permission granted");
//
//
//                mApiClient.connect();
//
//                mMap.setMyLocationEnabled(true);
//
//
//                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
//                if (mLastLocation != null) {
//
//
//                    LatLng yrLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(yrLocation));
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    markerOptions.position(yrLocation)
//                            .title("Your Current Position")
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//                    mCurrLocationMarker = mMap.addMarker(markerOptions);
//
//
//                }
//            } else {
//            LatLng nyc = new LatLng(40, -74);
//            mMap.addMarker(new MarkerOptions().position(nyc).title("Marker in New York"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(nyc));
            }
//        }




    @Override
    protected void onStart() {
        mApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mApiClient.disconnect();
        super.onStop();

    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {



        final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {

               final Status status = result.getStatus();
                final LocationSettingsStates settingsStates = result.getLocationSettingsStates();
                switch(status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:
                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(1000)
                                .setFastestInterval(1000)
                                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                .addLocationRequest(mLocationRequest);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        status.startResolutionForResult(MapsActivity.this, );

                }
            }
        });

  //  onRequestPermissionsResult()

//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
//            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, this);
        }



//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
//            if (mLastLocation != null) {
//                //can extract long and lat from the Location object returned by getLastLocation
////            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
////            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//            }






    @Override
    public void onConnectionSuspended(int i) {

    }


    //this is something i saw in a tutorial and watned to try
//    @Override
//    public void onLocationChanged(Location location){
//        mLastLocation = location;
//        if(mCurrLocationMarker!=null){
//            mCurrLocationMarker.remove();
//        }
//        //removed old marker, now we place current marker
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng)
//                .title("Your Current Position")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);
//
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//
//        //stop location updates
//        if(mApiClient != null){
//          //  LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
//        }
//
//    }

        public boolean checkLocationPermission(){

        }

}
