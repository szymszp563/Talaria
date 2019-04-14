package com.example.talaria;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.os.Handler;
import android.widget.Toast;

import com.example.talaria.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


public class VersusActivity extends AppCompatActivity {
    Float progressStatus=0.0f;
    int progressStatus2=0;
    int maxRoute=100;
    //Float myCurrentRoute;
    Float enemyCurrentRoute;
    //Float dist;
    ProgressBar prb1;
    ProgressBar prb2;
    //private GPSTracker gpsTracker;
    private Location myLocation;
   // Double latitude = 0.0, longitude = 0.0;
    Float distanceFromStart = 0.0f;
    private static final long UPDATE_INTERVAL = 100, FASTEST_INTERVAL = 100; // = 0,1 second
    private FusedLocationProviderClient fusedLocationClient;
    //private List<LatLng> knownUserLocations;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versus);
        prb1 = findViewById(R.id.pb);
        prb2 = findViewById(R.id.pb2);
        maxRoute = 100;
        prb1.setMax(maxRoute);
        prb2.setMax(maxRoute);
        prb1.setProgress(100);
        prb2.setProgress(100);

        //gpsTracker = new GPSTracker(getApplicationContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //knownUserLocations = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //knownUserLocations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                            //Toast.makeText(getApplicationContext(), "CALLBACK", Toast.LENGTH_SHORT).show();
                            myLocation = location;
                        }
                    }
                });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (myLocation != null) {
                        Float didtancePassed = myLocation.distanceTo(location);
                        distanceFromStart += didtancePassed;
                        Toast.makeText(getApplicationContext(), distanceFromStart.toString() + "m", Toast.LENGTH_SHORT).show();


                        enemyCurrentRoute = 0.0f;//tutaj pobieranie
                        progressStatus = distanceFromStart;
                        progressStatus2 = (int) (enemyCurrentRoute / maxRoute);
                        prb1.setProgress(distanceFromStart.intValue());
                        prb2.setProgress(progressStatus2);
                        Toast.makeText(getApplicationContext(), "progres:" + progressStatus + "m", Toast.LENGTH_SHORT).show();


                        // If task execution completed
                        if (progressStatus2 >= maxRoute || progressStatus >= maxRoute) {
                            // Set a message of completion
                            Toast.makeText(getApplicationContext(), ":OO", Toast.LENGTH_SHORT).show();
                        }
                        myLocation = location;
                    }
                }
            }
        };

        startLocationUpdates();

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }
}


