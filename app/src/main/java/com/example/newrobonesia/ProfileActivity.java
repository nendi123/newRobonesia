package com.example.newrobonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    Profile profile  = new Profile();

    TextView txtName, txtNis, txtKelas, txtAlamat, txtProfName, txtProfNis;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = (TextView) findViewById(R.id.valNama);
        txtNis = (TextView) findViewById(R.id.valNis);
        txtKelas = (TextView) findViewById(R.id.valKelas);
        txtAlamat = (TextView) findViewById(R.id.valAlamat);
        txtProfName = (TextView) findViewById(R.id.txtProfName);
        txtProfNis = (TextView) findViewById(R.id.txtProfNis);

        getProfile();

        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.setToken("");
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getProfile(){
        Constants constants = new Constants();
        OkHttpClient client  = new OkHttpClient();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        Request request = new Request.Builder()
                .url(constants.BASE_URL+ "profil")
                .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String stringBody = response.body().string();
//                Log.e("Body : ", stringBody);
                try {
                    JSONObject jsonBody = new JSONObject(stringBody);
                    JSONObject jsonSiswa = new JSONObject(jsonBody.getString("siswa"));
                    JSONObject jsonSekolah = new JSONObject(jsonSiswa.getString("sekolah"));

                    profile.setName(jsonBody.getString("nama"));
                    String url_foto = jsonBody.getString("foto_url").replace(" ","%20");
                    profile.setFotoUrl(url_foto);
                    profile.setNis(jsonSiswa.getString("nis"));
                    profile.setSekolah(jsonSekolah.getString("nama"));
                    profile.setAlamat(jsonSiswa.getString("alamat"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtName.setText(profile.getName());
                            txtNis.setText(profile.getNis());
                            txtAlamat.setText(profile.getAlamat());
                            txtProfName.setText(profile.getName());
                            txtProfNis.setText(profile.getNis() + " - " + profile.getSekolah());
                            txtKelas.setText("-");
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}