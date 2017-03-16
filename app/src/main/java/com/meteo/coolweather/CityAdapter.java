package com.meteo.coolweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meteo.coolweather.db.City;

import java.util.List;

/**
 * Created by meteo on 2017/3/14.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    List<City> cityList;
    OnListFragmentInteractionListener listener;

    public CityAdapter(List<City> cityList, OnListFragmentInteractionListener listener) {
        this.cityList = cityList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    City city = cityList.get(vh.getAdapterPosition());
                    listener.onListFragmentInteraction(city);
                }
            });
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.areaName.setText(city.getCityName());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView areaName;

        public ViewHolder(View itemView) {
            super(itemView);
            areaName = (TextView) itemView.findViewById(R.id.area_name);
        }
    }

}
