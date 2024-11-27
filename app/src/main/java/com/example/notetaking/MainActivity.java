package com.example.notetaking;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "csc.244.note.email";
    public static final String EXTRA_PASSWORD = "csc.244.note.password";

    public static final String EXTRA_EXIST_EMAIL = "csc.244.note.exit.email";
     static final String FILE_NAME = "token.txt";

    private EditText edtit_email, edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtit_email = findViewById(R.id.text_user);
        edit_password = findViewById(R.id.text_pass);

//        String email = edtit_email.getText().toString();
//        Log.d("print", email);
//        String password = edit_password.getText().toString();

        Button signIn = findViewById(R.id.button2);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onSignIn();

            }
        });

        Button btForget = findViewById(R.id.button);
        btForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onForgetButton();
            }
        });


    }

    public void onForgetButton(){
        String email = edtit_email.getText().toString();
        Intent intent = new Intent(this, ForgetPassword.class);
        intent.putExtra(EXTRA_EXIST_EMAIL, email);
        startActivity(intent);
    }



    public void onSignIn(){
        String email = edtit_email.getText().toString();
        String password = edit_password.getText().toString();

        String url = "http://10.0.0.186:12345";
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("password", password);
            jsonObject.put("time_span", 100);
            jsonObject.put("method", "authenticate");
            jsonObject.put("time_unit", "MINUTES");
            jsonObject.put("email", email);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String jsonResponse = null;
                try {
                    jsonResponse = response.getString("token");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                saveText(jsonResponse);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something is wrong" , Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent(this, Document.class);
        startActivity(intent);

    }

    public void saveText(String text){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
            Log.d("LoadText", "Save text: ");
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

    public void onNewUser(View view){
        Intent intent = new Intent(this, NewUser.class);
        startActivity(intent);
    }
}