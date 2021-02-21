package com.example.newrobonesia;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    PieChart pieChart;
    Button btnProfile,btnMoitoring;
    TextView txtIzin, txtSakit, txtAlfa, txtName, txtNis;
    Profile profile = new Profile();
    ImageView imageView;

    StatistikKehadiran statistikKehadiran = new StatistikKehadiran();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtAlfa = (TextView) findViewById(R.id.txtAlfa);
        txtIzin = (TextView) findViewById(R.id.txtIjin);
        txtSakit = (TextView) findViewById(R.id.txtSakit);

        txtName = (TextView) findViewById(R.id.txtName);
        txtNis = (TextView) findViewById(R.id.txtNIS);
        imageView = (ImageView) findViewById(R.id.imgFoto);

        onLoadApp();
        getProfile();

        btnProfile = (Button) findViewById(R.id.goProfile);
        btnMoitoring = (Button) findViewById(R.id.goMonitoring);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        btnMoitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, MonitoringActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtName.setText(profile.getName());
                            txtNis.setText(profile.getNis() + " - " + profile.getSekolah());
                            imageView.setImageURI(Uri.parse(profile.getFotoUrl()));
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void onLoadApp(){
        Constants constants = new Constants();
        OkHttpClient client  = new OkHttpClient();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        Request request = new Request.Builder()
                .url(constants.BASE_URL+ "monitoring")
                .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonData = response.body().string();

                JSONObject Jobject = null;
                try {
                    Jobject = new JSONObject(jsonData);
                    JSONObject nilaiObject = new JSONObject(Jobject.getString("nilai"));
                    JSONObject kehadiranObject = new JSONObject(Jobject.getString("kehadiran"));
                    JSONObject statistikObject = new JSONObject(kehadiranObject.getString("statistik"));

                    statistikKehadiran.setHadir(statistikObject.getString("hadir"));
                    statistikKehadiran.setAlfa(statistikObject.getString("tanpa keterangan"));
                    statistikKehadiran.setIzin(statistikObject.getString("izin"));
                    statistikKehadiran.setSakit(statistikObject.getString("sakit"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtAlfa.setText("Alfa : "+statistikKehadiran.getAlfa());
                            txtSakit.setText("Sakit : "+statistikKehadiran.getSakit());
                            txtIzin.setText("Izin : "+statistikKehadiran.getIzin());
                        }
                    });


                    ArrayList<PieEntry> yValues = new ArrayList<>();
                    yValues.add(new PieEntry(Float.parseFloat(nilaiObject.getString("DIY")), "DIY"));
                    yValues.add(new PieEntry(Float.parseFloat(nilaiObject.getString("Lego")), "Lego"));
                    yValues.add(new PieEntry(Float.parseFloat(nilaiObject.getString("Animasi")), "Animasi"));

                    pieChart = (PieChart) findViewById(R.id.pieChart);
                    pieChart.setUsePercentValues(true);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(5,10,5,5);

                    pieChart.setDragDecelerationEnabled(true);

                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setHoleColor(Color.WHITE);
                    pieChart.setTransparentCircleRadius(61f);


                    PieDataSet dataSet = new PieDataSet(yValues,"Program");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.LIBERTY_COLORS);

                    PieData pieData = new PieData((dataSet));
                    pieData.setValueTextSize(10f);
                    pieData.setValueTextColor(Color.GREEN);

                    pieChart.setData(pieData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}