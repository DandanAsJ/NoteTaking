package com.example.notetaking;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ForgetPassword extends Activity {
    private EditText edit_email, edit_Pass, newPass;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);


        edit_email = findViewById(R.id.edit_email1);
        edit_Pass = findViewById(R.id.edit_temppass1);
        newPass = findViewById(R.id.edit_newpass1);

        Intent intent = getIntent();
        String email = intent.getStringExtra(MainActivity.EXTRA_EXIST_EMAIL);
//        if(email != null) {
//            Log.d("print", email);
//        }


        edit_email.setText(email);

        onForget(email);

        Button setPass = findViewById(R.id.button_setPass);
        setPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registration();

            }

        });

        Button btBack = findViewById(R.id.button5);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });


    }

    public void registration() {
        String tempPassword = edit_Pass.getText().toString();
        String newPassword = newPass.getText().toString();
        String newEmail = edit_email.getText().toString();
        // Here, send the new password to the server
        String url = "http://10.0.0.186:12345";
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("method", "registerAccount");
            jsonObject.put("password", newPassword);
            jsonObject.put("temp_password", tempPassword);
            jsonObject.put("email", newEmail);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ForgetPassword.this, "Reset Password", Toast.LENGTH_LONG).show();
//            requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForgetPassword.this, "Something is wrong" , Toast.LENGTH_SHORT).show();
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

    public void onForget(String e){
        String url = "http://10.0.0.186:12345";
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("method", "forgotPassword");
            jsonObject.put("email", e);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForgetPassword.this, "Something is wrong" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("Content-Type", "application/json");

                return stringMap;
            }
        };
        MySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }

    public void goBack(){
        Intent intent3 = new Intent(this, MainActivity.class);
        startActivity(intent3);
    }
}
