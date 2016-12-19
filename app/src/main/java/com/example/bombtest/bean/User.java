package com.example.bombtest.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 鸿达 on 2016/12/13.
 */
public class User extends BmobObject {
    private String user_id;  //用户ID
    private String user_name;//用户名
    private BmobFile user_icon;//用户头像
    private String user_sign;//用户签名
    private String user_gender;//用户性别
    private String user_password;//用户密码
//    private String user_level;//用户等级
//    private String user_city;//用户城市

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_sign() {
        return user_sign;
    }

    public void setUser_sign(String user_sign) {
        this.user_sign = user_sign;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
