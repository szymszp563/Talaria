package com.example.talaria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RaceSettingsActivity extends AppCompatActivity {

    Button bMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_settings);

        bMatch = (Button) findViewById(R.id.btnMatch);

        bMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVersus = new Intent(getApplicationContext(), VersusActivity.class);
                startActivity(intentVersus);
            }
        });
    }


}
