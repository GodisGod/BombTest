package com.example.bombtest.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by 鸿达 on 2016/11/17.
 */
public class PaperMessage extends BmobObject {
    private String text_message;
    private BmobFile icon;
    private BmobGeoPoint gpsAdd;

    public PaperMessage() {
    }

    public PaperMessage(String text_message, BmobFile file, BmobGeoPoint gpsAdd) {
        this.text_message = text_message;
        this.icon = file;
        this.gpsAdd = gpsAdd;
    }

    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }

    public BmobGeoPoint getGpsAdd() {
        return gpsAdd;
    }

    public void setText_message(String text_message) {
        this.text_message = text_message;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public BmobFile getIcon() {

        return icon;
    }

    public String getText_message() {

        return text_message;
    }
}
