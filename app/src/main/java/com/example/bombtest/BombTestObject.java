package com.example.bombtest;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by 鸿达 on 2016/11/17.
 */
public class BombTestObject extends BmobObject {
    private String name;
    private String address;
    private BmobGeoPoint gpsAdd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }

    public BmobGeoPoint getGpsAdd() {

        return gpsAdd;
    }
}
