package com.example.bombtest.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by 鸿达 on 2016/11/17.
 */
public class PaperMessage extends BmobObject {

    private String user_id;//用户ID
    private String user_name;//用户名字
    private BmobFile user_icon;//用户头像
    private String send_text_message;//文字
    private BmobFile send_audio;//语音
    private BmobFile send_img_message;//图片
    private BmobGeoPoint gpsAdd;//地理位置

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public BmobFile getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(BmobFile user_icon) {
        this.user_icon = user_icon;
    }

    public String getSend_text_message() {
        return send_text_message;
    }

    public void setSend_text_message(String send_text_message) {
        this.send_text_message = send_text_message;
    }

    public BmobFile getSend_audio() {
        return send_audio;
    }

    public void setSend_audio(BmobFile send_audio) {
        this.send_audio = send_audio;
    }

    public BmobFile getSend_img_message() {
        return send_img_message;
    }

    public void setSend_img_message(BmobFile send_img_message) {
        this.send_img_message = send_img_message;
    }

    public BmobGeoPoint getGpsAdd() {
        return gpsAdd;
    }

    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }
}
