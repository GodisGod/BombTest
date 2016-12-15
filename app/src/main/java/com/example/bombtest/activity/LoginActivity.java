package com.example.bombtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bombtest.R;
import com.example.bombtest.bean.User;
import com.example.bombtest.constant.Constant;
import com.example.bombtest.provider.UserInfoProvider;
import com.example.bombtest.util.HD;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


public class LoginActivity extends AppCompatActivity {

    private UserInfoProvider userInfoProvider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        uploadUserinfo();

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

    private void uploadUserinfo() {
        final User userinfo = new User();
        //TODO 模拟添加用户id
        userinfo.setUser_id(Constant.userId);  //添加用户ID
        userinfo.setUser_name(Constant.userName);//添加用户名

        HD.LOG("1");
        //添加图片
        if (!Constant.userIcon.isEmpty()) {
            final File file = new File(Constant.userIcon);
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    userinfo.setUser_icon(bmobFile);
                    userinfo.setUser_sign("鸿达的签名");
                    userinfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            HD.TLOG("用户信息上成功: " + s);
                            HD.TLOG("userinfo: " + userinfo.getUser_id() + " " + userinfo.getUser_name() + "  " + userinfo.getUser_sign());
                        }
                    });
                }

                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);
                }
            });

        }
    }

}
