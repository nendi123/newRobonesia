package com.example.newrobonesia;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

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
    TextView txtIzin, txtSakit, txtAlfa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtAlfa = (TextView) findViewById(R.id.txtAlfa);
        txtIzin = (TextView) findViewById(R.id.txtIjin);
        txtSakit = (TextView) findViewById(R.id.txtSakit);

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
        onLoadApp();
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
    public void onLoadApp(){
        Constants constants = new Constants();
        OkHttpClient client  = new OkHttpClient();
        Request request = new Request.Builder()
                .url(constants.BASE_URL+ "monitoring")
                .addHeader("Authorization", "Bearer " + "5|BraINP3tF1mUDSIDBQc55OoXXmJ0WNFP3OhuTAcT")
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