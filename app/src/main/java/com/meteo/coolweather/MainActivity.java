package com.meteo.coolweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meteo.coolweather.db.City;
import com.meteo.coolweather.db.County;
import com.meteo.coolweather.db.Province;

import org.litepal.crud.DataSupport;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getInt("countyId", -1) >= 0) {
            int countyId = preferences.getInt("countyId", -1);
            County county = DataSupport.find(County.class, countyId);
            if (county != null) {
                onListFragmentInteraction(county);
            }
        }
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ~~~~~~");
        ProvinceFragment fragment = new ProvinceFragment();
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        if (!fragment.getClass().equals(ProvinceFragment.class)) {
            transaction.addToBackStack(null);
        }
        
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Province province) {
        CityFragment fragment = CityFragment.newInstance(province.getProvinceCode());
        replaceFragment(fragment);
    }

    @Override
    public void onListFragmentInteraction(City item) {
        CountyFragment fragment = CountyFragment.newInstance(item.getCityCode());
        replaceFragment(fragment);
    }

    @Override
    public void onListFragmentInteraction(County item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("countyId", item.getId());
        editor.apply();
        WeatherActivity.startActivity(this, item);
    }
}
