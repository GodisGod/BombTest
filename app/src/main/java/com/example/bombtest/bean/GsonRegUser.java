package com.example.bombtest.bean;

/**
 * Created by HONGDA on 2016/12/19.
 */
public class GsonRegUser {

    /**
     * code : 200
     * userId : 005
     * token : scCwUbxtcmatZ9wi0MLxZ/gexAdqvb9hcKGVQbyaqfMu63Jjhjvpefm2mutjY84KWH0Fsx2iRuI7mA+Ioh8twA==
     */

    private int code;
    private String userId;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "GsonRegUser{" +
                "code=" + code +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
