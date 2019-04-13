package com.example.talaria;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class HandlePermission {
    private Context context;
    private String[] permissions;
    private Activity activity;
    private int requestCode;

    HandlePermission(Context context, String[] permissions, Activity activity, int requestCode){
        this.permissions=permissions;
        this.context=context;
        this.activity=activity;
        this.requestCode=requestCode;
    }

    public boolean checkAndRequestPermissions(){
        List<String> listPermissionsNeeded = new ArrayList<>();
        for(String permission:permissions){
            if(ContextCompat.checkSelfPermission(context,permission)!=PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(permission);
            }
        }
        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(activity,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),requestCode);
            return false;
        }
        return true;
    }

}
