package com.example.notetaking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends Activity {
    private EditText tempPass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        tempPass = findViewById(R.id.edit_registration_tempPass);
        Button btRegister = findViewById(R.id.button4);

        Intent intent = getIntent();
        String newEmail = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
        String newPass = intent.getStringExtra(MainActivity.EXTRA_PASSWORD);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempPassword = tempPass.getText().toString();
                if (!tempPassword.isEmpty()) {
                    finalizeRegistration(tempPassword, newPass, newEmail);
                } else {
                    tempPass.setError("Password cannot be empty");
                }

            }


        });

        Button btBack = findViewById(R.id.bt_register_back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });




    }

    public void goBack(){
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }

    public void finalizeRegistration(String tempP, String newP, String newE) {
        // Here, send the new password to the server
        String url = "http://10.0.0.186:12345";
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("method", "registerAccount");
            jsonObject.put("password", newP);
            jsonObject.put("temp_password", tempP);
            jsonObject.put("email", newE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Registration.this, "Registered Successfully", Toast.LENGTH_LONG).show();
//            requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Registration.this, "Something is wrong" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("Content-Type", "application/json");

                return stringMap;
            }
        };

        //requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }






}
