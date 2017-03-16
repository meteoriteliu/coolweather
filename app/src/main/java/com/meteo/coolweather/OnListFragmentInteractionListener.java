package com.meteo.coolweather;

import com.meteo.coolweather.db.City;
import com.meteo.coolweather.db.Province;

/**
 * Created by meteo on 2017/3/15.
 */

public interface OnListFragmentInteractionListener {
    void onListFragmentInteraction(Province item);
    void onListFragmentInteraction(City item);
}
