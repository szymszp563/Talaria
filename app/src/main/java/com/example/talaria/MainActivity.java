package com.example.talaria;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button bMap, bClear, bPosition, bClient, bServer;
    TextView tvText;
    private static final int REQUEST_LOCATION = 123;
    //permissions
    HandlePermission fineLocation;
    HandlePermission coarseLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bMap = (Button) findViewById(R.id.bShowMap);
        bClear = (Button) findViewById(R.id.bClear);
        bPosition = (Button) findViewById(R.id.bGetPosition);
        tvText = (TextView) findViewById(R.id.tvText);
        bServer = (Button) findViewById(R.id.bServer);
        bClient = (Button) findViewById(R.id.bClient);
        final View view = (View) findViewById(R.id.mainLayout);

        fineLocation =
                new HandlePermission(this, Manifest.permission.ACCESS_FINE_LOCATION, this,REQUEST_LOCATION);
        coarseLocation =
                new HandlePermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, this,REQUEST_LOCATION);

        bClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionThread t = new ConnectionThread("5.173.136.213", false, view);
                t.start();
            }
        });

        bServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionThread t = new ConnectionThread("", true, view);
                t.start();
            }
        });

        bClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvText.setText("");
            }
        });

        bPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvText.setText("Here will be position, przemo");
            }
        });

        bMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(fineLocation.getAccessGranted() || coarseLocation.getAccessGranted()){
                   Intent mapActivityIntent = new Intent(getApplicationContext(), MapsActivity.class);
                   startActivity(mapActivityIntent);
               }else{
                   Toast.makeText(getApplicationContext(), "Enable localisation!", Toast.LENGTH_SHORT).show();
               }

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        Toast.makeText(getApplicationContext(), "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    for (String permission:permissions) {
                        if(permission.equals(Manifest.permission.ACCESS_FINE_LOCATION))
                            fineLocation.setAccessGranted(true);
                        else if(permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION))
                            coarseLocation.setAccessGranted(true);

                    }
                    fineLocation.setAccessGranted(true);

                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
