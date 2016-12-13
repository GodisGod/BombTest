package com.example.bombtest.util.interfaces;

/**
 * Created by 鸿达 on 2016/12/13.
 */
public interface LocationCallbackListener {
    void onFinish(double mlng, double mlat);

    void onError(String e);
}
