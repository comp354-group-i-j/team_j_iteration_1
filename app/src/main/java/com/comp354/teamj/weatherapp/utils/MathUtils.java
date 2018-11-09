package com.comp354.teamj.weatherapp.utils;

import com.comp354.teamj.weatherapp.entities.WeatherKeys;
import com.comp354.teamj.weatherapp.entities.WeatherResponse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple math library with useful functions
 * related to Weather and Fitness data
 */
public class MathUtils {

    /**
     * Calculate a moving average from given data along a time range
     * @param data Data points
     * @param timeRange Number of days to considered for each p
     * @return List of moving averages
     */
    public static List<WeatherResponse> movingAverageOfTemp(List<WeatherResponse> data, int timeRange) {
        LinkedList<WeatherResponse> results = new LinkedList<>();
        if (timeRange < 0) {

            for (int i = 0; i < data.size() - timeRange; ++i) {
                WeatherResponse currentAverage = new WeatherResponse(data.get(i));
                float averageTemp = 0;
                for (int j = i; j < i + timeRange; ++j) {
                    averageTemp += data.get(j).getTemperature();
                }
                currentAverage.setTemperature(averageTemp / timeRange);
                results.add(currentAverage);
            }
        }
        return results;
    }

    /**
     * Calculate the average temperature and windspeed for all data
     * Index 0 = temperature,
     * Index 1 = Windspeed,
     * In
     * @param data Data points
     * @return HashMap Map of the keys and values.
     */
    public static HashMap<WeatherKeys, Double> getSummaryValues (List<WeatherResponse> data){
        HashMap<WeatherKeys, Double> averageMap = new HashMap<>(4);
        double[] average = new double[4];

        if(data != null && data.size() > 0){
            //get the first value for the lowest temperature
            average[3] = data.get(0).getTemperature();
            for(WeatherResponse response : data){
                average[0] += response.getTemperature();
                average[1] += response.getWindSpeed();
                if(average[2] < response.getTemperature())
                    average[2] = response.getTemperature();
                if(average[3] > response.getTemperature())
                    average[3] = response.getTemperature();
            }
            //Compute average temperature and windspeed
            average[0] = average[0] / (double) data.size();
            average[1] = average[1] / (double) data.size();
        }
        averageMap.put(WeatherKeys.AVERAGETEMP, average[0]);
        averageMap.put(WeatherKeys.AVERAGEWIND, average[1]);
        averageMap.put(WeatherKeys.HOTDAY, average[2]);
        averageMap.put(WeatherKeys.COLDDAY, average[3]);

        return averageMap;
    }

}
