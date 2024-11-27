package com.example.notetaking;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Document extends AppCompatActivity {
//    final String MY_TOKEN = "c2FnYXJAa2FydHBheS5jb206cnMwM...";
static final String FILE_NAME1 = "document.txt";
    EditText edit_title, edit_body;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document);
        setTitle(R.string.app_title);

        edit_title = findViewById(R.id.edit_title);
        edit_body = findViewById(R.id.editTextTextMultiLine);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.document_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = edit_title.getText().toString();
        String body = edit_body.getText().toString();
        String uuid = UUID.randomUUID().toString();

        int id = item.getItemId();
            if(id == R.id.save){

                sendData(title, body, uuid);
                return true;
            } else if (id == R.id.delete) {
                Log.d("print", "delete");
                return true;
            } else if (id == R.id.share) {
                Intent intent = new Intent(this, ShareDocument.class);
                //intent.putExtra("UUID", uuid);
                startActivity(intent);
                return true;
            } else if (id == R.id.upload) {
                Intent intent = new Intent(this, GetDocument.class);
                startActivity(intent);
                return true;
            }

            return super.onOptionsItemSelected(item);

    }

    public void sendData(String t, String b, String id) {
        JSONObject document = new JSONObject();

        try{
            document.put("id", id);
            document.put("text", b);
            document.put("creation_date", System.currentTimeMillis());
            document.put("title", t);

            saveText(String.valueOf(document));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("method", "setDocument");
            jsonObject.put("document", document);

            sendJsonToServer(this, jsonObject);

        }catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void sendJsonToServer(Context context, JSONObject jsonObject){
        String token = loadText();
        String url = "http://10.0.0.186:12345";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Document.this, "Saved document", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Document.this, "Something is wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("autho_token", token);

                return stringMap;
            }
        };

        MySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
    }

    public void saveText(String text){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME1, MODE_PRIVATE);
            fos.write(text.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
