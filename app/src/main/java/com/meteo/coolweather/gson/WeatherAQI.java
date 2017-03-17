package com.meteo.coolweather.gson;

/**
 * Created by meteo on 2017/3/17.
 */

public class WeatherAQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
