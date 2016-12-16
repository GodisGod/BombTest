package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context ctx;
    private UserInfoProvider userInfoProvider = null;
    private Button user1_123;
    private Button user1_222;
    private Button user1_001;
    private ImageView choose_user_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        initView();
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

    private void startIM(String token) {
//        uploadUserinfo();
        Log.i("LHD", "LoginActivity  " + token);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

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

    private void initView() {
        user1_001 = (Button) findViewById(R.id.choose_user1);
        user1_123 = (Button) findViewById(R.id.choose_user2);
        user1_222 = (Button) findViewById(R.id.choose_user3);
        choose_user_icon = (ImageView) findViewById(R.id.chooser_user_icon);
        user1_001.setOnClickListener(this);
        user1_123.setOnClickListener(this);
        user1_222.setOnClickListener(this);
        choose_user_icon.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_user1:
                //大屏三星手机
//                Constant.token = "qNXAU4LNaV8cQbq4EbBEV6Mr9UMQ0pPPzGYJ5GDBQb4Km4VnXb8qNdXXY8W//unFvwTuLAht4H3jfBuueXrRZg==";
                //对应的用户ID为123
                Constant.userId = "001";
                Constant.userName = "测试用户1";
                HD.TLOG("选择成功： " + Constant.userId + "  " + Constant.userName);
                startIM(Constant.token1);
                break;
            case R.id.choose_user2:
//                Constant.token = "Le7e95ydLq3zAtF18Y75JPgexAdqvb9hcKGVQbyaqfOwg8rbHG9EEIA0njTEMpsGR9IFvu+l2dQ7mA+Ioh8twA==";
                //对应的用户ID为222
                Constant.userId = "002";
                Constant.userName = "测试用户2";
                HD.TLOG("选择成功： " + Constant.userId + "  " + Constant.userName);
                startIM(Constant.token2);
                break;
            case R.id.choose_user3:
//                Constant.token = "4cwt+PWBSJMuRqMHFaIszfgexAdqvb9hcKGVQbyaqfOwg8rbHG9EEDZyvJvNaqMNb7WR5CjwdC87mA+Ioh8twA==";
                //对应的用户ID为001
                Constant.userId = "003";
                Constant.userName = "测试用户3";
                HD.TLOG("选择成功： " + Constant.userId + "  " + Constant.userName);
                startIM(Constant.token3);
                break;
            case R.id.chooser_user_icon:
                startActivityForResult(new Intent(ctx, PhotosWall.class), 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Constant.userIcon = data.getStringExtra("imgurl");
                Glide.with(ctx).load(Constant.userIcon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .centerCrop()  //转换宽高比
                        .into(choose_user_icon);
            }
        }
    }
}
