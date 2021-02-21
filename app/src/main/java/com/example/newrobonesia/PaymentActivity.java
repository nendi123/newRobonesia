package com.example.newrobonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentActivity extends AppCompatActivity {

    Button btnPayment;
    TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnPayment = (Button) findViewById(R.id.btnPayment);
        txtResponse = (TextView) findViewById(R.id.txtResponse);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\n  \"payment_type\": \"bank_transfer\",\n  \"bank_transfer\": {\n    \"bank\": \"permata\",\n\t\"va_number\": \"1234567890\"\n  },\n  \"transaction_details\": {\n    \"order_id\": \"order-101b-1613946574\",\n    \"gross_amount\": 44000\n  },\n    \"customer_details\": {\n        \"email\": \"noreply@example.com\",\n        \"first_name\": \"budi\",\n        \"last_name\": \"utomo\",\n        \"phone\": \"+6281 1234 1234\"\n    },\n    \"item_details\": [\n    {\n       \"id\": \"item01\",\n       \"price\": 21000,\n       \"quantity\": 1,\n       \"name\": \"Ayam Zozozo\"\n    },\n    {\n       \"id\": \"item02\",\n       \"price\": 23000,\n       \"quantity\": 1,\n       \"name\": \"Ayam Xoxoxo\"\n    }\n   ]\n}");
                Request request = new Request.Builder()
                        .url("https://api.sandbox.midtrans.com/v2/charge")
                        .method("POST", body)
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Basic U0ItTWlkLXNlcnZlci1ZanBCTVVPWUo3MmdYNC0wMUVFeU10THQ6")
                        .build();
                try {
                    final Response response = client.newCall(request).execute();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                txtResponse.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}