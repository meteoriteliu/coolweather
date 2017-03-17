package com.meteo.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by meteo on 2017/3/17.
 */

public class WeatherForcast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }
}
