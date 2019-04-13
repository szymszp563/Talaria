package com.example.talaria;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class HandlePermission {
    private boolean accessGranted = false;
    private Context context;
    private String permission;
    private Activity activity;
    private int requestCode;

    public boolean getAccessGranted() {
        return accessGranted;
    }

    public void setAccessGranted(boolean granted) {
        accessGranted = granted;
    }

    HandlePermission(Context context, String permission, Activity activity, int requestCode){
        this.permission=permission;
        this.context=context;
        this.activity=activity;
        this.requestCode=requestCode;
        getAccess();
    }

    public void getAccess(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission}, requestCode);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission}, requestCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            accessGranted = true;
        }
    }



}
