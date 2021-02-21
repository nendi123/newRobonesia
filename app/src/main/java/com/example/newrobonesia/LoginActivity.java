package com.example.newrobonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin,btnRegister;
    EditText editEmail, editPassword;
    TextView txtAlert;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.getToken() != ""){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        editEmail = (EditText) findViewById(R.id.txtEmail);
        editPassword = (EditText) findViewById(R.id.txtPassword);
        txtAlert = (TextView) findViewById(R.id.textView3);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAlert.setVisibility(View.INVISIBLE);
                MediaType MEDIA_TYPE = MediaType.parse("application/json");
                Constants constants = new Constants();
                OkHttpClient client = new OkHttpClient();
                JSONObject postData = new JSONObject();

                try {
                    postData.put("email", editEmail.getText().toString());
                    postData.put("password", editPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MEDIA_TYPE, postData.toString());
                Request request = new Request.Builder()
                        .url(constants.BASE_URL + "login")
                        .post(requestBody)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        String mMessage = e.getMessage().toString();
                        Log.e("failure Response", mMessage);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.code() == 200){
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.setToken(jsonObject.getString("token"));

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtAlert.setVisibility(View.VISIBLE);
                                }
                            });
                        }

                     }
                });


            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}