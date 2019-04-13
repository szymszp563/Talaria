package com.example.talaria;

import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location myLocation;
    Double latitude, longitude;
    float zoomLevel;
    private Marker myCustomerMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gpsTracker = new GPSTracker(getApplicationContext());
        myLocation = gpsTracker.getLocation();
        latitude = myLocation.getLatitude();
        longitude = myLocation.getLongitude();
        zoomLevel = 16.0f;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng myCustomerLatLng = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions();
        options.position(myCustomerLatLng);
        options.title("You are here");
        myCustomerMarker = mMap.addMarker(options);

        // Add a marker in Sydney and move the camera
       // LatLng myLatLng = new LatLng(latitude, longitude);

       // mMap.addMarker(new MarkerOptions().position(myLatLng).title("You are here."));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCustomerLatLng, zoomLevel), 1000, null);
        final Handler handler = new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                if (myCustomerMarker != null) {
                    myLocation = gpsTracker.getLocation();
                    latitude = myLocation.getLatitude();
                    longitude = myLocation.getLongitude();
                    myCustomerMarker.setPosition(new LatLng(latitude,longitude));
                }

                handler.postDelayed(this,500); // set time here to refresh textView
            }
        });
    }


}
