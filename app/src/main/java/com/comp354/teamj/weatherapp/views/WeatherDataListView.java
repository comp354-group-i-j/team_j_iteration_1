package com.comp354.teamj.weatherapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comp354.teamj.weatherapp.R;
import com.comp354.teamj.weatherapp.entities.WeatherResponse;

import java.util.List;

public class WeatherDataListView extends RecyclerView.Adapter<WeatherDataListView.WeatherDataListViewHolder> {
    class WeatherDataListViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView tempTextView;
        public WeatherDataListViewHolder(View v) {
            super(v);
            dateTextView = (TextView) v.findViewById(R.id.date_text);
            tempTextView = (TextView) v.findViewById(R.id.temp_text);
        }
    }

    private List<WeatherResponse> mDataset;

    public WeatherDataListView(List<WeatherResponse> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public WeatherDataListView.WeatherDataListViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_item, parent, false);
        WeatherDataListViewHolder viewHolder = new WeatherDataListViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherDataListViewHolder holder, int position) {
        WeatherResponse response = mDataset.get(position);

        if (response.getDateTime() != null) {
            holder.dateTextView.setText(response.getDateTime().toString());
            holder.tempTextView.setText(String.valueOf(response.getTemperatureString()));
        } else {
            holder.dateTextView.setText(response.toString());
            holder.tempTextView.setText("SORRY...");
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}