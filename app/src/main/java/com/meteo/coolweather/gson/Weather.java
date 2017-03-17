package com.meteo.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by meteo on 2017/3/17.
 */

public class Weather {
    public String status;

    @SerializedName("basic")
    public WeatherBasic basic;

    @SerializedName("aqi")
    public WeatherAQI aqi;

    @SerializedName("now")
    public WeatherNow now;

    @SerializedName("suggestion")
    public WeatherSuggestion suggestion;

    @SerializedName("daily_forecast")
    public List<WeatherForcast> forcastList;
}
