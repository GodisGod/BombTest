package com.example.bombtest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.WindowManager;

import com.example.bombtest.listener.ScripConnectStatusListener;
import com.example.bombtest.listener.ScripConversationBehaviorListener;
import com.example.bombtest.listener.ScripConversationListBehaviorListener;
import com.example.bombtest.listener.ScripReceiveMessageListener;
import com.example.bombtest.listener.ScripSendMessageListener;
import com.example.bombtest.provider.UserInfoProvider;

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
    private ScripSendMessageListener listener;
    private ScripReceiveMessageListener receiveMessageListener;
    private ScripConnectStatusListener connectStatusListener;
    private ScripConversationListBehaviorListener conversationListBehaviorListener;
    private ScripConversationBehaviorListener conversationBehaviorListener;
    private UserInfoProvider userInfoProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initListener();
        initsdk();
    }

    private void initListener() {
        listener = new ScripSendMessageListener();
        receiveMessageListener = new ScripReceiveMessageListener();
        connectStatusListener = new ScripConnectStatusListener();
        conversationListBehaviorListener = new ScripConversationListBehaviorListener();
        conversationBehaviorListener = new ScripConversationBehaviorListener();
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
            RongIM.getInstance().setSendMessageListener(listener);
//            RongIM.setOnReceiveMessageListener(receiveMessageListener);
            RongIM.setConnectionStatusListener(connectStatusListener);
            RongIM.setConversationListBehaviorListener(conversationListBehaviorListener);
            RongIM.setConversationBehaviorListener(conversationBehaviorListener);
            initUserProvider();
            Log.i("LHD", " RongIM.init(this);");
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

                DemoContext.init(this);
            }
        }
    }

    private void initUserProvider() {
        //todo 保存用户信息到Bmob
        userInfoProvider = new UserInfoProvider();
        /**
         * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
         *
         *  UserInfoProvider 用户信息提供者。
         *  isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
         *                         如果 App 提供的 UserInfoProvider
         *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
         *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
         * @see UserInfoProvider
         */
        RongIM.setUserInfoProvider(userInfoProvider, true);
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
