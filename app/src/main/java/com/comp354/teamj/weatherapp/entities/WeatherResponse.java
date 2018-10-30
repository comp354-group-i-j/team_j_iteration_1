package com.comp354.teamj.weatherapp.entities;

import android.util.Log;

import java.util.Date;

public class WeatherResponse {
    private Date dateTime;
    private float temperature;

    private String message;

    public WeatherResponse() {
        this.message = "";
    }

    public WeatherResponse(String message) {
        this.message = message.trim();
    }

    public void setDateTime(String dateTime) {
        Date dateValue = new Date();
        dateTime = dateTime.replaceAll("-", "/");
        try {
            dateValue = new Date(Date.parse(dateTime));
        } catch (IllegalArgumentException e) {
            Log.d("WeatherResponse", String.format("Unable to parse date and time '%s'", dateTime));
        }

        this.dateTime = dateValue;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setTemperature(String temp) {
        float tempValue = 0;

        try {
            tempValue = Float.parseFloat(temp);
        } catch (NumberFormatException e) {
            Log.d("WeatherResponse", String.format("Unable to parse temperature '%s'", temp));
        }

        this.temperature = tempValue;
    }

    public float getTemperature() {
        return temperature;
    }
    public String getTemperatureString() {
        return temperature + " °C";
    }

    @Override public String toString() {
        if (this.message.compareToIgnoreCase("") == 0) {
            return "WeatherResponse(dateTime=" + this.dateTime + ", temperature=" + this.temperature + ")";
        } else {
            return this.message;
        }
    }
}
