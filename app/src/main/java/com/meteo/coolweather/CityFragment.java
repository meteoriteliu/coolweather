package com.meteo.coolweather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.meteo.coolweather.db.City;
import com.meteo.coolweather.db.Province;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meteo on 2017/3/14.
 */

public class CityFragment extends Fragment {

    CityAdapter ca;
    List<City> cityList = new ArrayList<>();
    private static final String TAG = "CityFragment";
    int provinceId;
    Province province;

    OnListFragmentInteractionListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        }
    }

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

        ca = new CityAdapter(cityList, listener);
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(ca);

        TextView textView = (TextView) view.findViewById(R.id.title_text);
        textView.setText(province.getProvinceName());

        Button backButton = (Button) view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

    public void refresh() {
        cityList.clear();
        cityList.addAll(DataSupport.where("provinceId = ?", String.valueOf(provinceId)).find(City.class));
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
            provinceId = getArguments().getInt("provinceId");
            province = DataSupport.where("provinceCode = ?", String.valueOf(provinceId)).find(Province.class).get(0);
        }
    }

    public static CityFragment newInstance(int provinceId) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putInt("provinceId", provinceId);
        fragment.setArguments(args);
        return fragment;
    }

}
