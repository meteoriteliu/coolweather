package com.meteo.coolweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.meteo.coolweather.db.County;
import com.meteo.coolweather.gson.Weather;
import com.meteo.coolweather.gson.WeatherForcast;
import com.meteo.coolweather.util.HttpUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    County county;
    private static final String TAG = "WeatherActivity";

    TextView title_county;
    TextView title_time;

    TextView now_degree;
    TextView now_info;

    LinearLayout forcast_list_layout;

    TextView aqi_text;
    TextView pm25_text;

    TextView suggestion_comfort;
    TextView suggestion_carWash;
    TextView suggestion_sport;

    ImageView bing_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        county = (County) getIntent().getSerializableExtra("county");
        if (county == null) {
            Log.e(TAG, "onCreate: empty county...");
            finish();
        }

        title_county = (TextView) findViewById(R.id.title_county);
        title_time = (TextView) findViewById(R.id.title_time);

        title_county.setText(county.getCountyName());

        now_degree = (TextView) findViewById(R.id.degree_text);
        now_info = (TextView) findViewById(R.id.info_text);

        forcast_list_layout = (LinearLayout) findViewById(R.id.forcast_layout);

        aqi_text = (TextView) findViewById(R.id.aqi_text);
        pm25_text = (TextView) findViewById(R.id.pm25_text);

        suggestion_comfort = (TextView) findViewById(R.id.comfort_text);
        suggestion_carWash = (TextView) findViewById(R.id.car_wash_text);
        suggestion_sport = (TextView) findViewById(R.id.sport_text);

        getWeather(county.getWeatherId());

        bing_img = (ImageView) findViewById(R.id.bing_pic_img);
        String bingUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("bingUrl_" + new SimpleDateFormat("yyyyMMdd").format(new Date()), "");
        if (!bingUrl.equals("")) {
            Glide.with(this).load(bingUrl).into(bing_img);
        } else {
            loadBingPic();
        }
    }

    private void loadBingPic() {
        HttpUtil.sendHttpRequest("http://guolin.tech/api/bing_pic", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingUrl = response.body().string();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("bingUrl_" + new SimpleDateFormat("yyyyMMdd").format(new Date()), bingUrl);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingUrl).into(bing_img);
                    }
                });

            }
        });
    }

    private void getWeather(String weatherId) {
        String url = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=4d1912e2f8ba4fe5b7d972a9c9ef196d";
        Log.d(TAG, "getWeather: url=" + url);
        findViewById(R.id.weather_layout).setVisibility(View.INVISIBLE);
        HttpUtil.sendHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final Weather weather = gson.fromJson(jsonObject.getJSONArray("HeWeather").getJSONObject(0).toString(), Weather.class);
                    Log.d(TAG, "onResponse: weather:" + weather.status);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title_time.setText(weather.basic.update.getUpdateTime());

                            now_degree.setText(weather.now.temperature + "℃");
                            now_info.setText(weather.now.more.info);

                            for (WeatherForcast forcast : weather.forcastList) {
                                View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forcast_item, forcast_list_layout, false);
                                TextView item_date = (TextView) view.findViewById(R.id.item_date);
                                TextView item_info = (TextView) view.findViewById(R.id.item_info);
                                TextView item_max = (TextView) view.findViewById(R.id.item_max);
                                TextView item_min = (TextView) view.findViewById(R.id.item_min);

                                item_date.setText(forcast.date);
                                item_info.setText(forcast.more.info);
                                item_max.setText(forcast.temperature.max);
                                item_min.setText(forcast.temperature.min);
                                forcast_list_layout.addView(view);
                            }
                            if (weather.aqi != null) {
                                aqi_text.setText(weather.aqi.city.aqi);
                                pm25_text.setText(weather.aqi.city.pm25);

                            } else {
                                WeatherActivity.this.findViewById(R.id.aqi_section).setVisibility(View.GONE);
                            }

                            suggestion_sport.setText("运动建议："+weather.suggestion.sport.info);
                            suggestion_comfort.setText("舒适度："+weather.suggestion.comfort.info);
                            suggestion_carWash.setText("洗车指数："+weather.suggestion.carWash.info);

                            findViewById(R.id.weather_layout).setVisibility(View.VISIBLE);
                            loadBingPic();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void startActivity(Context context, County county) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra("county", county);
        context.startActivity(intent);
    }
}
