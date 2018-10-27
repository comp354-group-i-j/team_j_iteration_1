package com.comp354.teamj.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.comp354.teamj.weatherapp.entities.WeatherResponse;
import com.comp354.teamj.weatherapp.utils.Parser;
import com.comp354.teamj.weatherapp.views.WeatherDataListView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Generate the API URL for the GET request.
     * @param format Data format: csv or xml
     * @param timeframe 1 for hourly, 2 for daily, 3 for monthly
     * @param year The year <xxxx>
     * @param month The month <1-12>
     * @return The API URL
     */
    private String weatherURL(String format, int timeframe, int year, int month) {
        String baseUrl = "http://climate.weather.gc.ca/climate_data/bulk_data_e.html";

        int stationID = 51157; // MONTREAL INTL A Station ID
        int day = 1; // Not used and can be an arbitrary value
        String submit = "Download+Data";

        return String.format("%s?format=%s&stationID=%d&Year=%d&Month=%d&Day=%d&timeframe=%d&submit=%s",
                baseUrl, format, stationID, year, month, day, timeframe, submit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.weather_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new WeatherDataListView(Collections.singletonList(new WeatherResponse("That didn't work!")));


        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Setup the API URL
        String url = weatherURL("csv", 2, 2018, 1);
        Log.d("main", "url: " + url);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // parse csv data here
                        try {
                            List<WeatherResponse> list = Parser.stringToItems(response);
                            mAdapter = new WeatherDataListView(list);
                            mRecyclerView.setAdapter(mAdapter);
                        } catch (IOException e) {
                            mAdapter = new WeatherDataListView(Collections.singletonList(new WeatherResponse("That didn't work!")));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mAdapter = new WeatherDataListView(Collections.singletonList(new WeatherResponse("That didn't work")));
                    }
                }
        );

        // Add the request to the RequestQueue
        mRecyclerView.setAdapter(this.mAdapter);
        queue.add(stringRequest);
    }
}
