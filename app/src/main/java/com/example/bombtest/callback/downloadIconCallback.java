package com.example.bombtest.callback;

/**
 * Created by HONGDA on 2016/12/18.
 */
public interface downloadIconCallback {
    //访问成功的回调接口
    void onFinish();
    //访问失败的回调接口
    void onError(Exception e);
}
