package com.comp354.teamj.weatherapp.utils;

import com.comp354.teamj.weatherapp.entities.WeatherResponse;

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
}
