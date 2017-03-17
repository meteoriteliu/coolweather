package com.meteo.coolweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meteo.coolweather.db.County;

import java.util.List;

/**
 * Created by meteo on 2017/3/15.
 */

public class CountyAdapter extends RecyclerView.Adapter<CountyAdapter.ViewHolder> {

    List<County> countyList;
    OnListFragmentInteractionListener listener;

    public CountyAdapter(List<County> countyList, OnListFragmentInteractionListener listener) {
        this.countyList = countyList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView areaName;

        public ViewHolder(View itemView) {
            super(itemView);
            areaName = (TextView) itemView.findViewById(R.id.area_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    County county = countyList.get(vh.getAdapterPosition());
                    listener.onListFragmentInteraction(county);
                }
            });
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        County county = countyList.get(position);
        holder.areaName.setText(county.getCountyName());
    }

    @Override
    public int getItemCount() {
        return countyList.size();
    }
}
