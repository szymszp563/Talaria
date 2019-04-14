package com.example.talaria;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOrRegister extends AppCompatActivity {

    Button bIn, bUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        bIn = (Button) findViewById(R.id.bChooseLogin);
        bUp = (Button) findViewById(R.id.bChooseRegister);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        bIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), Logging.class);
                startActivity(loginIntent);
            }
        });

        bUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(getApplicationContext(), Register.class);
                startActivity(regIntent);
            }
        });
    }
}
