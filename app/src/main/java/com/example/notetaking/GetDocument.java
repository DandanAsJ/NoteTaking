package com.example.notetaking;

import android.app.Activity;
import android.content.Context;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GetDocument extends Activity {
    Button btLoad;
    EditText edit_body_text;
    String body, token, id, title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_document);

        edit_body_text = findViewById(R.id.editTextTextMultiLine2);

        try {
            JSONObject mydocument = new JSONObject(loadText(Document.FILE_NAME1));
            id = mydocument.getString("id");
            title = mydocument.getString("title");
            body = mydocument.getString("text");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        //token = loadText(MainActivity.FILE_NAME);
//        try {
//            JSONObject mydocument = new JSONObject(loadText(Document.FILE_NAME1));
//            id = mydocument.getString("id");
//
//
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }

        btLoad = findViewById(R.id.button_load);
        btLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetchDocument(token, id);

                String textBody = "ID: "+id+"\nTitle: "+title+"\nText: " +body;
                edit_body_text.setText(textBody);
            }
        });


        Button btBack = findViewById(R.id.button6);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack2();
            }
        });


    }

    public void goBack2(){
        Intent intent2 = new Intent(this, Document.class);
        startActivity(intent2);
    }

//    public void fetchDocument(String token, String id) {
//        String url = "http://10.0.0.186:12345";
//        JSONObject jsonObject = new JSONObject();
//        try{
//            jsonObject.put("method", "getDocument");
//            jsonObject.put("document_id", id);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    body = response.getString("text");
//                    title = response.getString("title");
//
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(GetDocument.this, "Something is wrong" , Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> stringMap = new HashMap<>();
//                stringMap.put("autho_token", token);
//
//                return stringMap;
//            }
//        };
//
//        MySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
//
//
//    }
    public String loadText(String filename){
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try{
            fis = openFileInput(filename);
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
