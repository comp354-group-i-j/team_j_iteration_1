package com.comp354.teamj.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        entries.clear();
        labels.clear();

        for (int i = 0; i < MainActivity.weatherResponseList.size(); i++) {
            entries.add(new Entry(i, MainActivity.weatherResponseList.get(i).getTemperature()));
            windSpeedEntries.add(new Entry(i, MainActivity.weatherResponseList.get(i).getWindSpeed()));
            labels.add(new String(MainActivity.weatherResponseList.get(i).getDateTime().toString()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Temperature (°C)");
        dataSet.setColors(Color.parseColor("#f44141"));

        LineDataSet windSpeedDataSet = new LineDataSet(windSpeedEntries, "Wind Speed (km/h)");
        windSpeedDataSet.setColors(Color.parseColor("#4146f4"));

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date date = new Date(labels.get((int) value));

                if (chart.getVisibleXRange() > 24 * 30) {
                    return new SimpleDateFormat("MMM yyyy").format(date);
                } else {
                    return new SimpleDateFormat("dd MMM yyyy").format(date);
                }
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
