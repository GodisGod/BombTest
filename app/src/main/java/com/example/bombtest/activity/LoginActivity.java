package com.example.bombtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bombtest.constant.Constant;
import com.example.bombtest.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Log.i("LHD", "LoginActivity  " + Constant.token);
        RongIM.connect(Constant.token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.i("LHD", "--onTokenIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.i("LHD", "--onSuccess" + userid);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("LHD", "onError: " + errorCode);
            }
        });
    }

}
