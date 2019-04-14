package com.example.talaria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Register extends AppCompatActivity {

    Button bReg;
    EditText etLogin, etPass1, etPass2, etNick;
    HttpRequestsCommander http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etLogin = (EditText) findViewById(R.id.etRegLogin);
        etPass1 = (EditText) findViewById(R.id.etRegPassword);
        etPass2 = (EditText) findViewById(R.id.etRegPassword2);
        bReg = (Button) findViewById(R.id.bRegister);
        etNick = (EditText) findViewById(R.id.etNick);

        http = new HttpRequestsCommander();

        bReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPass1.getText().toString().length() > 6 && etPass1.getText().toString().equals(etPass2.getText().toString()) && !etNick.getText().toString().isEmpty()){
                    try {
                        String resp = http.post("http://51.38.134.31:3501/v1/user/auth/register", "{\"email\":\"" + etLogin.getText().toString() + "\",\"password\":\"" + etPass1.getText().toString() + "\",\"name\":\"" + etNick.getText().toString() + "\"}");
                        Log.d("CONN", resp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent registerIntent = new Intent(getApplicationContext(), Logging.class);
                    startActivity(registerIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "ERROR IN DATA", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
