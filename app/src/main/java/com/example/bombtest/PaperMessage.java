package com.example.bombtest;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by 鸿达 on 2016/11/17.
 */
public class PaperMessage extends BmobObject {
    private String text_message;

    private BmobGeoPoint gpsAdd;

    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }

    public BmobGeoPoint getGpsAdd() {

        return gpsAdd;
    }

    public void setText_message(String text_message) {
        this.text_message = text_message;
    }

    public String getText_message() {

        return text_message;
    }
}
