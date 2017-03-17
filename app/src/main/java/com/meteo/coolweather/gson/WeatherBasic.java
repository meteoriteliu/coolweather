package com.meteo.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by meteo on 2017/3/17.
 */

public class WeatherBasic {
    @SerializedName("city")
    String cityName;

    @SerializedName("id")
    String weatherId;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public Update update;

    public class Update {
        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        @SerializedName("loc")
        String updateTime;
    }
}
