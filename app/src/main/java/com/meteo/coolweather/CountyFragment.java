package com.meteo.coolweather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meteo.coolweather.db.City;
import com.meteo.coolweather.db.County;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meteo on 2017/3/15.
 */

public class CountyFragment extends Fragment {

    CountyAdapter ca;
    List<County> countyList = new ArrayList<>();
    private static final String TAG = "CountyFragment";
    int cityId;

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_list, container, false);
        Log.d(TAG, "onCreateView: 33333");

        ca = new CountyAdapter(countyList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(ca);
        }
        return view;
    }

    public void refresh() {
        countyList.clear();
        countyList.addAll(DataSupport.where("cityId = ?", String.valueOf(cityId)).find(County.class));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ca.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            cityId = getArguments().getInt("cityId");
        }
    }

    public static CountyFragment newInstance(int cityId) {
        CountyFragment fragment = new CountyFragment();
        Bundle args = new Bundle();
        args.putInt("cityId", cityId);
        fragment.setArguments(args);
        return fragment;
    }
}
