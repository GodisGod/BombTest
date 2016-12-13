package com.example.bombtest.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by 鸿达 on 2016/11/17.
 */
public class PaperMessage extends BmobObject {

    private String user_id;//用户ID
    private String text_message;//文字
    private BmobFile audio;//语音
    private BmobFile icon;//图片
    private BmobGeoPoint gpsAdd;//地理位置

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public BmobFile getAudio() {
        return audio;
    }

    public void setAudio(BmobFile audio) {
        this.audio = audio;
    }

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
