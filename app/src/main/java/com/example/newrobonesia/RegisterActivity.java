package com.example.newrobonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class RegisterActivity extends AppCompatActivity {
    Button btnSave;
    EditText personName, nis, sekolah, email, password, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        personName = (EditText) findViewById(R.id.editPersonName);
        nis = (EditText) findViewById(R.id.editNis);
        sekolah = (EditText) findViewById(R.id.editSchool);
        email = (EditText) findViewById(R.id.editEmails);
        password = (EditText) findViewById(R.id.editPasswords);
        phone = (EditText) findViewById(R.id.editPhone);

        btnSave = (Button) findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaType MEDIA_TYPE = MediaType.parse("application/json");
                Constants constants = new Constants();
                OkHttpClient client = new OkHttpClient();
                JSONObject postData = new JSONObject();

                String nisString= nis.getText().toString();
                int nisInt =Integer.parseInt(nisString);
                try {
                    postData.put("nama", personName.getText().toString());
                    postData.put("nis", nisInt);
                    postData.put("sekolah_id", sekolah.getText().toString());
                    postData.put("email", email.getText().toString());
                    postData.put("password", password.getText().toString());
                    postData.put("tel", phone.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody requestBody = RequestBody.create(MEDIA_TYPE, postData.toString());
                Request request = new Request.Builder()
                        .url(constants.BASE_URL + "register")
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
                        String mMessage = response.body().string();
                        Log.w("Sukses", mMessage);
                    }
                });

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}