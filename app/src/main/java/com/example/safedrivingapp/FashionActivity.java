package com.example.safedrivingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class FashionActivity<stringRequest> extends AppCompatActivity {

    Button amber;
    String data1;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion);

        amber = (Button)findViewById(R.id.button2);

        amber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSheets(context,"Amber" );
            }
        });


    }


    public boolean sendToSheets(Context context, String data1)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/home/projects/14lLc6P-SchxWDNYecbIPjPj0NnvWdG7rDBg45dT4l7A8Ywk4jKnwisIH/edit", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {  }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {  }
    })
    {
        @Override
        protected Map<String, String> getParams()
        {
            // contains the data to send to the sheet
            Map<String, String> params = new HashMap<>();

            params.put("action", "addItem");
//            params.put("time", currentTime);

            // add more data here if you need.
            // must be formed of pairs of Strings.

            return params;
        }
    };

    int socketTimeout = 50000; // 50 secs

    RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    stringRequest.setRetryPolicy(retryPolicy);

    RequestQueue queue = Volley.newRequestQueue(context);

    queue.add(stringRequest);

    return true;
    }

}