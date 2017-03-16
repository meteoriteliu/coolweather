package com.meteo.coolweather;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meteo.coolweather.db.Province;

import java.util.List;

/**
 * Created by meteo on 2017/3/14.
 */

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {

    List<Province> provinceList;
    OnListFragmentInteractionListener listener;

    public ProvinceAdapter(List<Province> provinceList, OnListFragmentInteractionListener listener) {
        this.provinceList = provinceList;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;

        public ViewHolder(View itemView) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.area_name);
        }
    }

    @Override
    public ProvinceAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = vh.getAdapterPosition();

                if (listener != null) {
                    listener.onListFragmentInteraction(provinceList.get(pos));
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ProvinceAdapter.ViewHolder holder, int position) {
        Province p = provinceList.get(position);
        holder.itemText.setText(p.getProvinceName());
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }
}
