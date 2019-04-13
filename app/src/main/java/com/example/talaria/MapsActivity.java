package com.example.talaria;

import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    Double latitude = 0.0, longitude = 0.0;
    float zoomLevel;
    private Marker myCustomerMarker;
    Button centerMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        centerMap = (Button) findViewById(R.id.centerButton);
        gpsTracker = new GPSTracker(getApplicationContext());
        myLocation = gpsTracker.getLocation();
        if(myLocation != null)
        {
            latitude = myLocation.getLatitude();
            longitude = myLocation.getLongitude();
        }
        zoomLevel = 16.0f;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        centerMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap != null && myLocation != null){
                    LatLng latlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel), 1000, null);
                }
            }
        });
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

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCustomerLatLng, zoomLevel), 1000, null);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().isZoomControlsEnabled();

        final Handler handler = new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                if (myCustomerMarker != null) {

                    Location oldLocation = myLocation;
                    myLocation = gpsTracker.getLocation();
                    if(myLocation != null && oldLocation != null)
                    {
                        Float distance = oldLocation.distanceTo(myLocation);
                        latitude = myLocation.getLatitude();
                        longitude = myLocation.getLongitude();
                        myCustomerMarker.setPosition(new LatLng(latitude,longitude));
                        if(distance != 0.0f)
                            Toast.makeText(getApplicationContext(), "Distance between updates: " + distance.toString() + "m", Toast.LENGTH_SHORT).show();
                    }
                }
                else if( myCustomerMarker == null && myLocation != null){
                    MarkerOptions options = new MarkerOptions();
                    options.position(new LatLng(latitude, longitude));
                    options.title("You are here");
                    myCustomerMarker = mMap.addMarker(options);
                }

                handler.postDelayed(this,500); // set time here to refresh textView
            }
        });
    }


}
