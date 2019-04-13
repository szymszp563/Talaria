package com.example.talaria;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    Button bReg;
    EditText etLogin, etPass1, etPass2, etNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etLogin = (EditText) findViewById(R.id.etRegLogin);
        etPass1 = (EditText) findViewById(R.id.etRegPassword);
        etPass2 = (EditText) findViewById(R.id.etRegPassword2);
        bReg = (Button) findViewById(R.id.bRegister);
        etNick = (EditText) findViewById(R.id.etNick);

        bReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPass1.getText().toString().length() > 6 && etPass1.getText().toString().equals(etPass2.getText().toString()) && !etNick.getText().toString().isEmpty()){
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://51.38.134.31:3501/v1/user/auth/login";
                    JSONObject json = new JSONObject();
                    try {
                        json.put("email", etLogin.getText().toString()).put("password", etPass1.getText().toString()).put("name", etNick.getText().toString());
                        Log.d("JSON", json.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    Toast.makeText(getApplicationContext(), "Response is: "+ response.substring(0,500), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Register doesnt work :( " + error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("ERROR", error.toString());
                        }
                    });

// Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
                else{
                    Toast.makeText(getApplicationContext(), "ERROR IN DATA", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
