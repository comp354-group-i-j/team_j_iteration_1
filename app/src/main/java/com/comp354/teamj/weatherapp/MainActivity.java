package com.comp354.teamj.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.comp354.teamj.weatherapp.entities.WeatherResponse;
import com.comp354.teamj.weatherapp.utils.Parser;
import com.comp354.teamj.weatherapp.utils.WeatherDataFetcher;
import com.comp354.teamj.weatherapp.views.WeatherDataListView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int startYear = 2018;
    private static final int startMonth = 4;

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

        final List<WeatherResponse> weatherResponseList = new LinkedList<>();

        WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher(this);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public synchronized void onResponse(String response) {
                try {
                    List<WeatherResponse> list = Parser.stringToItems(response);
                    weatherResponseList.addAll(list);
                    Log.d("main", "entries added to list: " + list.size());
                    Log.d("main", "list size: " + weatherResponseList.size());
                    mAdapter = new WeatherDataListView(weatherResponseList);
                    mRecyclerView.setAdapter(mAdapter);
                } catch (IOException e) {
                    mAdapter = new WeatherDataListView(Collections.singletonList(new WeatherResponse("That didn't work!")));
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAdapter = new WeatherDataListView(Collections.singletonList(new WeatherResponse("That didn't work")));
            }
        };

        int year = startYear;
        int month = startMonth;
        int monthCounter = startMonth;

        Calendar calendar = Calendar.getInstance();
        int currentYear  = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        // iterate through all months and populate weather list
        while (year < currentYear || (year == currentYear && month <= currentMonth)) {
            Log.d("main", String.format("year: %d, month: %d", year, month));

            weatherDataFetcher.getWeatherData(year, month, listener, errorListener);

            monthCounter += 1;
            month = ((monthCounter - 1) % 12) + 1;
            year = startYear + ((monthCounter - 1) / 12);
        }

        mRecyclerView.setAdapter(this.mAdapter);
    }
}
