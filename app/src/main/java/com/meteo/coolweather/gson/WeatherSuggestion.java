package com.meteo.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by meteo on 2017/3/17.
 */

public class WeatherSuggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
