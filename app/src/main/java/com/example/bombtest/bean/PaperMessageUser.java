package com.example.bombtest.bean;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by 李鸿达 on 2016/12/19.
 */

public class PaperMessageUser {
    private String user_id;//用户ID
    private String send_text_message;//文字
    private BmobFile send_audio;//语音
    private BmobFile send_img_message;//图片
    private BmobGeoPoint gpsAdd;//地理位置
    private String gender;//纸片主人性别
    private int type;//纸片的类型 1、文字消息，2、语音消息,3、图片消息，4、图文消息
    private String userIcon;
    private String createTime;
    private String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
