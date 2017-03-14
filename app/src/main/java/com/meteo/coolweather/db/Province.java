package com.meteo.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by meteo on 2017/3/14.
 */

public class Province extends DataSupport {
    int id;
    String provinceName;
    int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
