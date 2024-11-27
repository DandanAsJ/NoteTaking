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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ShareDocument extends Activity {
    private EditText edit_email, edit_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_document);

        edit_email = findViewById(R.id.share_email);
        edit_id = findViewById(R.id.share_id);

        //Intent intent = new Intent();
        //String myUUID = intent.getStringExtra("UUID");

        Button btShare = findViewById(R.id.button_share);
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDocument();
            }
        });

        Button btBack = findViewById(R.id.button7);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack3();
            }
        });

    }

    public void goBack3(){
        Intent intent2 = new Intent(this, Document.class);
        startActivity(intent2);
    }

    public void shareDocument(){
        String url = "http://10.0.0.186:12345";
        String email = edit_email.getText().toString();
        JSONArray accessor = new JSONArray();
        accessor.put(email);
//        JSONObject accessor = new JSONObject();
        String id = edit_id.getText().toString();
        String token = loadText();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("method", "setDocumentAccessors");
            jsonObject.put("document_id", id);

            jsonObject.put("accessors", accessor);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ShareDocument.this, "Shared Successfully", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShareDocument.this, "Something is wrong" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("autho_token", token);

                return stringMap;
            }
        };

        //requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }
    public String loadText(){
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try{
            fis = openFileInput(MainActivity.FILE_NAME);
            InputStreamReader irs = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(irs);
            String text;
            while((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }
            fis.close();


        } catch (IOException e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
