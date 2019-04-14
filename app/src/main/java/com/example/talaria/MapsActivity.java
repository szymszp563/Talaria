package com.example.talaria;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location myLocation;
    Double latitude = 0.0, longitude = 0.0;
    float zoomLevel;
    Float distanceFromStart = 0.0f;
    private static final long UPDATE_INTERVAL = 100, FASTEST_INTERVAL = 100; // = 0,1 seconds
    //TextView distView = findViewById(R.id.distText);
    TextView timerView;
    Button startButton;
    Button endButton;
    CountDownTimer countDownTimer;
    long timeLeftInMiliseconds = 10_000;
    boolean timerRunnging = false;
    boolean afterCountDown = false;

    private Polyline usersPath;
    private FusedLocationProviderClient fusedLocationClient;
    private List<LatLng> knownUserLocations;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        gpsTracker = new GPSTracker(getApplicationContext());
        zoomLevel = 16.0f;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        knownUserLocations = new ArrayList<>();
        //knownUserLocations.add(new LatLng(0, 0));

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
                            knownUserLocations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                            usersPath.setPoints(knownUserLocations);
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
                    if(afterCountDown)
                    {
                        knownUserLocations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                        usersPath.setPoints(knownUserLocations);
                        if(myLocation != null)
                        {
                            Float didtancePassed = myLocation.distanceTo(location);
                            distanceFromStart+=didtancePassed;
                            Intent i = new Intent(MapsActivity.this, VersusActivity.class);
                            Float passedDistance = distanceFromStart;
                            i.putExtra("DISTANCE", passedDistance);

                            //distView.setText(distanceFromStart.toString());
                            //Toast.makeText(getApplicationContext(), "Distance: " + didtancePassed.toString() + "m" + "Distance passed from beginning:"
                                   // + distanceFromStart.toString() + "m", Toast.LENGTH_SHORT).show();
                            //distView.setText(distanceFromStart.toString());
                            //Toast.makeText(getApplicationContext(), "Distance: " + didtancePassed.toString() + "m" + "Distance passed from beginning:"
                              //      + distanceFromStart.toString() + "m", Toast.LENGTH_SHORT).show();
                        }
                        myLocation = location;
                    }
                }
            };
        };

        timerView = findViewById(R.id.timerView);
        startButton = findViewById(R.id.startButton);
        endButton = findViewById(R.id.endButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                startButton.setVisibility(View.INVISIBLE);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterCountDown = false;
                endButton.setVisibility(View.INVISIBLE);
                saveResultToFile();
                resetDistanceAndPath();
            }
        });
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(timeLeftInMiliseconds , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMiliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunnging = false;
                afterCountDown = true;
                endButton.setVisibility(View.VISIBLE);
                timerView.setText("");
            }
        }.start();

        timerRunnging = true;
    }

    private void updateTimer()
    {
        Integer seconds = (int) timeLeftInMiliseconds % 60000 / 1000;
        String timeLeftString = seconds.toString();
        timerView.setText(timeLeftString);
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

    private void saveResultToFile()
    {
        String filename = "results";
        String fileContents = distanceFromStart.toString();
        fileContents+="\n";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Toast.makeText(getApplicationContext(), fileContents, Toast.LENGTH_SHORT).show();
    }

    private void resetDistanceAndPath()
    {
        distanceFromStart = 0.0f;
        startButton.setVisibility(View.VISIBLE);
        knownUserLocations = new ArrayList<>();
        usersPath.setPoints(knownUserLocations);
        timeLeftInMiliseconds = 10_000;
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        usersPath = mMap.addPolyline(new PolylineOptions());
        myLocation = gpsTracker.getLocation();
        if(myLocation != null) {
            latitude = myLocation.getLatitude();
            longitude = myLocation.getLongitude();
        }
        LatLng myCustomerLatLng = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCustomerLatLng, zoomLevel), 1000, null);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().isZoomControlsEnabled();

        startLocationUpdates();
    }
}
