package com.meteo.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by meteo on 2017/3/17.
 */

public class WeatherNow {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
