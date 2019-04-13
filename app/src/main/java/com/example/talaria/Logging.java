package com.example.talaria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Logging extends AppCompatActivity {

    Button bLogin;
    EditText etLogin, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPass = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://51.38.134.31:3501/v1/user/auth/login";
                JSONObject json = new JSONObject();
                try {
                    json.put("email", etLogin.getText().toString()).put("password", etPass.getText().toString());
                    Log.d("JSON", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Display the first 500 characters of the response string.
                                Toast.makeText(getApplicationContext(), "Response is: "+ response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register doesnt work :(", Toast.LENGTH_SHORT).show();
                        if (error == null || error.networkResponse == null) {
                            return;
                        }

                        String body;
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                            Log.d("ERROR", body);
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);

        }
    });
}
}
