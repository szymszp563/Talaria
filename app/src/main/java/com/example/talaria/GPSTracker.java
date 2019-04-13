package com.example.talaria;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;

    private Location location;
    protected LocationManager locationManager;

    public Location getLocation() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (isGPSEnabled) {
                if(location == null){
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1_000, 5, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    }
                }
            }
            if (location == null) {
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1_000, 5, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    }
                }
            }
        }
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public GPSTracker(Context conext) {
        this.context = conext;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.setLocation(location);

    }

    // @androidx.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
