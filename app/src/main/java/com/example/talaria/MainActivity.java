package com.example.talaria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button bMap, bClear, bPosition, bClient, bServer;
    TextView tvText;


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

        bClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
