package com.example.talaria;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

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
                //ConnectionThread t = new ConnectionThread("37.47.50.247", false, view);
                //t.start();
                Socket socket = null;
                try {
                    socket = IO.socket("http://51.38.134.31:3000/");
                    socket.connect();

                    socket.on("test", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            JSONObject data = (JSONObject)args[0];
                            //Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("CONNECTION", "JSON LECI");
                        }
                    });
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

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
