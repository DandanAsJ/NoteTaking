package com.example.notetaking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewUser extends Activity {
    private EditText email, fname, lname, newPass;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);


        email = findViewById(R.id.edit_new_email);
        fname = findViewById(R.id.edit_new_fname);
        lname = findViewById(R.id.edit_new_lname);
        newPass = findViewById(R.id.edit_new_pass);



    }

    public void sendData(View view) {
        String emailData, fnameData, lnameData, passData;
        emailData = email.getText().toString();
        fnameData = fname.getText().toString();
        lnameData = lname.getText().toString();
        passData = newPass.getText().toString();

        String url = "http://10.0.0.186:12345";
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("method", "createAccount");
            jsonObject.put("last_name", lnameData);
            jsonObject.put("first_name", fnameData);
            jsonObject.put("email", emailData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
//        Network network = new BasicNetwork(new HurlStack());
//        RequestQueue requestQueue = new RequestQueue(cache, network);
//        requestQueue.start();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(NewUser.this, "Create new user", Toast.LENGTH_LONG).show();
//            requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewUser.this, "Something is wrong" , Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(view.getContext()).getRequestQueue().add(jsonObjectRequest);

        Intent intent = new Intent(this, Registration.class);
        intent.putExtra(MainActivity.EXTRA_EMAIL, emailData);
        intent.putExtra(MainActivity.EXTRA_PASSWORD, passData);

        startActivity(intent);
    }




    }














