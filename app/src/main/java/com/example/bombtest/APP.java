package com.example.bombtest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.WindowManager;

import cn.bmob.v3.Bmob;
import cn.sharesdk.framework.ShareSDK;
import io.rong.imkit.RongIM;

/**
 * Created by 鸿达 on 2016/12/12.
 */
public class APP extends Application {

    private static Context sContext;
    private static APP sInstance;
    public static int SCREEN_WIDTH;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
        initsdk();
    }

    private void initsdk() {
        ShareSDK.initSDK(this);
        Bmob.initialize(this, "9c025fdf83b8cb9dcded051b04f741dc");
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            Log.i("LHD", " RongIM.init(this);");
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

                DemoContext.init(this);
            }
        }
    }

    private void init() {
        sContext = getApplicationContext();
        sInstance = this;
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point screen = new Point();
        SCREEN_WIDTH = Math.min(screen.x, screen.y);
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new NullPointerException("APP Context is Null");
        }
        return sContext;
    }

    public static APP getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("APP sInstance is Null");
        }
        return sInstance;
    }
    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
}
