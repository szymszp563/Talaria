package com.example.talaria;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button bMap, bClear, bPosition, bClient, bServer;
    View view;
    TextView tvText;

    //permissions
    private static final int REQUEST_LOCATION = 123;
    HandlePermission allPermissions;
    String[] appPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

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
        view = (View) findViewById(R.id.mainLayout);

        allPermissions =
                new HandlePermission(this, appPermissions, this, REQUEST_LOCATION);
        if (allPermissions.checkAndRequestPermissions()) {
            initApp();
        }

    }

    private void initApp() {
        bClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ConnectionThread t = new ConnectionThread("37.47.50.247", false, view);
                //t.start();
                Socket socket = null;
                try {
                    socket = IO.socket("http://51.38.134.31:3000/");
                    socket.connect();
                    //socket.emit("event", "ILYES");

                    socket.on("test", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                             JSONObject data = (JSONObject)args[0];
//                            Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("CONNECTION", "JSON LECI");
                        }
                    });
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (Exception e){
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
                Intent mapActivityIntent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(mapActivityIntent);
            }
        });
    }

    public AlertDialog showDialog(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnClick,
                                  String negativeLabel, DialogInterface.OnClickListener negativeOnClick, boolean isCancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnClick);
        builder.setNegativeButton(negativeLabel, negativeOnClick);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                initApp();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permissionName = entry.getKey();
                    int permissionResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                        showDialog("", "This app needs Location Permissions to work without any problems.",
                                "Yes, grant permissions",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        allPermissions.checkAndRequestPermissions();
                                    }
                                }, "no exit app", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();

                                    }
                                }, false);
                        break;
                    }
                }
            }
        }
    }

}


