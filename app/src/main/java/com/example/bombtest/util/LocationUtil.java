package com.example.bombtest.util;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by 李鸿达 on 2016/12/8.
 */

public class LocationUtil {
    private static LocationManager instance = null;
    private Context context;

    public static LocationManager getlocation(Context context) {
        if (instance == null) {
            synchronized (LocationUtil.class) {
                if (instance == null) {
                    instance = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                }
            }
        }
        return instance;
    }

}
