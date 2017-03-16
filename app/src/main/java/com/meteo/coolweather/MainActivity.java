package com.meteo.coolweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.meteo.coolweather.db.City;
import com.meteo.coolweather.db.Province;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
