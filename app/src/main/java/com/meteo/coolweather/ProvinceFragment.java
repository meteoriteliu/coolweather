package com.meteo.coolweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.meteo.coolweather.db.City;
import com.meteo.coolweather.db.County;
import com.meteo.coolweather.db.Province;
import com.meteo.coolweather.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * 
 * interface.
 */
public class ProvinceFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    List<Province> provinceList;
    ProvinceAdapter pa;
    OnListFragmentInteractionListener listener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProvinceFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProvinceFragment newInstance(int columnCount) {
        ProvinceFragment fragment = new ProvinceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_list, container, false);

        provinceList = new ArrayList<>();
        pa = new ProvinceAdapter(provinceList, listener);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(pa);
        }

        initArea();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void initArea() {
        List<Province> pList = DataSupport.findAll(Province.class);
        if (pList != null && !pList.isEmpty()) {
            provinceList.clear();
            provinceList.addAll(pList);
            pa.notifyDataSetChanged();
            return;
        }
        showProgressDialog();
        DataSupport.deleteAll(City.class);
        DataSupport.deleteAll(County.class);
        DataSupport.deleteAll(Province.class);

        HttpUtil.sendHttpRequest("http://guolin.tech/api/china", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getActivity(), "load failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                JsonArray provinceArray = gson.fromJson(response.body().string(), JsonArray.class);
                for (JsonElement provinceObj : provinceArray) {
                    JsonObject jo = provinceObj.getAsJsonObject();
                    Province p = new Province();
                    p.setProvinceCode(Integer.parseInt(jo.get("id").getAsString()));
                    p.setProvinceName(jo.get("name").getAsString());
                    p.save();
                    loadCity(p.getProvinceCode());
                    provinceList.add(p);


                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pa.notifyDataSetChanged();
                        closeProgressDialog();
                    }
                });
            }
        });
    }

    ProgressDialog progressDialog = null;

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void loadCity(final int provinceCode) {
        HttpUtil.sendHttpRequest("http://guolin.tech/api/china/" + provinceCode, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                JsonArray cityArray = gson.fromJson(response.body().string(), JsonArray.class);
                for (JsonElement cityObj : cityArray) {
                    JsonObject jo = cityObj.getAsJsonObject();
                    City c = new City();
                    c.setCityCode(Integer.parseInt(jo.get("id").getAsString()));
                    c.setCityName(jo.get("name").getAsString());
                    c.setProvinceId(provinceCode);
                    c.save();
                    loadCounty(provinceCode, c.getCityCode());
                }
            }
        });
    }

    private void loadCounty(int provinceCode, final int cityCode) {
        HttpUtil.sendHttpRequest("http://guolin.tech/api/china/" + provinceCode + "/" + cityCode, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                JsonArray countyArray = gson.fromJson(response.body().string(), JsonArray.class);
                for (JsonElement countyObj : countyArray) {
                    JsonObject jo = countyObj.getAsJsonObject();
                    County c = new County();
                    c.setCityId(cityCode);
                    c.setCountyName(jo.get("name").getAsString());
                    c.setCountyId(Integer.parseInt(jo.get("id").getAsString()));
                    c.setWeatherId(jo.get("weather_id").getAsString());
                    c.save();
                }
            }
        });
    }

}
