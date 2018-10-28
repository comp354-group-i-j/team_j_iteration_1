package com.comp354.teamj.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.nio.charset.Charset;
import java.util.Locale;

public class WeatherDataFetcher {

    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    /**
     * Generate the API URL for the GET request.
     * @param year The year <xxxx>
     * @param month The month <1-12>
     * @return The API URL
     */
    private static String weatherURL(int year, int month) {
        String baseUrl = "http://climate.weather.gc.ca/climate_data/bulk_data_e.html";

        String format = "csv"; // "csv" or "xml"
        int stationID = 51157; // MONTREAL INTL A Station ID
        int day = 1; // Not used and can be an arbitrary value
        int timeframe = 1; // 1 for hourly, 2 for daily, 3 for monthly
        String submit = "Download+Data";

        return String.format(Locale.US, "%s?format=%s&stationID=%d&Year=%d&Month=%d&Day=%d&timeframe=%d&submit=%s",
                baseUrl, format, stationID, year, month, day, timeframe, submit);
    }

    private static String weatherDataKey(int year, int month) {
        return String.format(Locale.US, "%d-%d", year, month);
    }

    public WeatherDataFetcher(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void getWeatherData(int year, int month, final Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        final String weatherDataKey = weatherDataKey(year, month);
        String weatherDataBase64 = sharedPreferences.getString(weatherDataKey, null);

        if (weatherDataBase64 != null) {
            Log.d("main", "weather data found locally");
            byte[] weatherDataBytes = Base64.decode(weatherDataBase64, Base64.DEFAULT);
            String weatherData = new String(weatherDataBytes, Charset.forName("ISO-8859-1"));
            listener.onResponse(weatherData);
        } else {
            Log.d("main", "weather data not found locally");
            getWeatherDataNetwork(year, month,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        byte[] responseBytes = response.getBytes(Charset.forName("ISO-8859-1"));
                        String responseBase64 = Base64.encodeToString(responseBytes, Base64.DEFAULT);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(weatherDataKey, responseBase64);
                        sharedPreferencesEditor.apply();
                        listener.onResponse(response);
                    }
                }, errorListener);
        }
    }

    private void getWeatherDataNetwork(int year, int month, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        // Setup the API URL
        String url = weatherURL(year, month);
        Log.d("main", "url: " + url);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }
}
