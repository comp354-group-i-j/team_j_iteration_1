package com.comp354.teamj.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comp354.teamj.weatherapp.entities.WeatherResponse;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intentViewChart = getIntent();

        final LineChart chart = (LineChart) findViewById(R.id.chart);
        final List<Entry> entries = new ArrayList<Entry>();
        final List<Entry> windSpeedEntries = new ArrayList<Entry>();
        final List<String> labels = new ArrayList<String>();

        // sort weather list
        Collections.sort(MainActivity.weatherResponseList, new Comparator<WeatherResponse>() {
            public int compare(WeatherResponse w1, WeatherResponse w2) {
                return w1.getDateTime().compareTo(w2.getDateTime());
            }
        });

        float currentTemperatureAverage = 0;
        float currentWindSpeedAverage = 0;
        int currentCount = 0;
        String currentDay = null;

        for (int i = 0; i < MainActivity.weatherResponseList.size(); i++) {
            WeatherResponse currentWeatherResponse = MainActivity.weatherResponseList.get(i);
            String thisDay = new SimpleDateFormat("yyyy-MM-dd").format(currentWeatherResponse.getDateTime());

            if (currentDay == null || currentDay.equals(thisDay)) {
                currentDay = new String(thisDay);
                currentTemperatureAverage += currentWeatherResponse.getTemperature();
                currentWindSpeedAverage += currentWeatherResponse.getWindSpeed();
                currentCount += 1;
            } else {
                int currentIndex = entries.size() + 1;
                entries.add(new Entry(currentIndex, currentTemperatureAverage / currentCount));
                windSpeedEntries.add(new Entry(currentIndex, currentWindSpeedAverage / currentCount));
                labels.add(new String(currentDay));

                currentDay = new String(thisDay);
                currentTemperatureAverage = currentWeatherResponse.getTemperature();
                currentWindSpeedAverage = currentWeatherResponse.getWindSpeed();
                currentCount = 1;
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Temperature (°C)");
        dataSet.setColors(Color.parseColor("#f44141"));

        LineDataSet windSpeedDataSet = new LineDataSet(windSpeedEntries, "Wind Speed (km/h)");
        windSpeedDataSet.setColors(Color.parseColor("#4146f4"));

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet);
        dataSets.add(windSpeedDataSet);

        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate(); // refresh
    }
}
