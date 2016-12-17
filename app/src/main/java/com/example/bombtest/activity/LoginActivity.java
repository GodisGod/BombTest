package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.example.bombtest.DemoContext;
import com.example.bombtest.R;
import com.example.bombtest.bean.User;
import com.example.bombtest.callback.downloadIconCallback;
import com.example.bombtest.constant.Constant;
import com.example.bombtest.util.HD;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {

    private Context ctx;
    private Button user1_123;
    private Button user1_222;
    private Button user1_001;
    private Button pre_user;
    private Button login_QQ;
    private Button login_Weixin;
    private ImageView choose_user_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        initView();
    }

    private void startIM(String token) {
        uploadUserinfo();
    }

    private void initView() {
        user1_001 = (Button) findViewById(R.id.choose_user1);
        user1_123 = (Button) findViewById(R.id.choose_user2);
        user1_222 = (Button) findViewById(R.id.choose_user3);
        pre_user = (Button) findViewById(R.id.choose_preuser);
        login_QQ = (Button) findViewById(R.id.login_by_QQ);
        login_Weixin = (Button) findViewById(R.id.login_by_weixin);

        choose_user_icon = (ImageView) findViewById(R.id.chooser_user_icon);
        user1_001.setOnClickListener(this);
        user1_123.setOnClickListener(this);
        user1_222.setOnClickListener(this);
        pre_user.setOnClickListener(this);
        login_QQ.setOnClickListener(this);
        login_Weixin.setOnClickListener(this);
        choose_user_icon.setOnClickListener(this);

    }

    private void uploadUserinfo() {
        final User userinfo = new User();
        //上传用户信息
        userinfo.setUser_id(Constant.userId);  //添加用户ID
        userinfo.setUser_name(Constant.userName);//添加用户名
        userinfo.setUser_gender(Constant.usergender);//添加用户性别
        SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
        edit.putString("USER_TOKEN", Constant.curtoken);
        HD.LOG("保存token: " + Constant.curtoken);
        edit.apply();
        //添加图片
        if (!Constant.userIcon.isEmpty()) {
            final File file = new File(Constant.userIcon);
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    userinfo.setUser_icon(bmobFile);//添加用户头像
                    userinfo.setUser_sign("鸿达的签名");//添加用户签名
                    userinfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            HD.TLOG("用户信息保存到数据库成功: " + s);
                            HD.TLOG("userinfo: " + userinfo.getUser_id() + " " + userinfo.getUser_name() + "  " + userinfo.getUser_sign());
                            RongIM.connect(Constant.curtoken, new RongIMClient.ConnectCallback() {

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
                    });
                }

                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);
                    HD.TLOG("用户头像上传进度： " + value);
                }

            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_user1:
                Constant.userId = "001";
                Constant.userName = "测试用户1";
                Constant.usergender = "f";
                HD.TLOG("选择成功： " + Constant.userId + "  " + Constant.userName);
                Constant.curtoken = Constant.token1;
                startIM(Constant.token1);
                break;
            case R.id.choose_user2:
                Constant.userId = "n002";
                Constant.userName = "新用户2";
                Constant.usergender = "f";
                HD.TLOG("选择成功： " + Constant.userId + "  " + Constant.userName);
                Constant.curtoken = Constant.token2;
                startIM(Constant.token2);
                break;
            case R.id.choose_user3:
                Constant.userId = "003";
                Constant.userName = "测试用户3";
                Constant.usergender = "m";
                HD.TLOG("选择成功： " + Constant.userId + "  " + Constant.userName);
                Constant.curtoken = Constant.token3;
                startIM(Constant.token3);
                break;
            case R.id.chooser_user_icon:
                startActivityForResult(new Intent(ctx, PhotosWall.class), 2);
                break;
            case R.id.choose_preuser:
                String token = null;

                if (DemoContext.getInstance() != null) {

                    token = DemoContext.getInstance().getSharedPreferences().getString("USER_TOKEN", "default");
                }
                startIM(token);
                break;
            case R.id.login_by_QQ:
                loginWithQQorWechat(QQ.NAME);
                break;
            case R.id.login_by_weixin:
                loginWithQQorWechat(Wechat.NAME);
                break;

        }
    }

    private void loginWithQQorWechat(final String platform_name) {
        final Platform platform = ShareSDK.getPlatform(this, platform_name);
        platform.setPlatformActionListener(this);
        HD.TLOG("platform.isValid(): " + platform.isValid());
        if (platform.isValid()) {
            final String uname = platform.getDb().getUserName();
            final String userIcon = platform.getDb().getUserIcon();
            //下载图片到本地,开启子线程
            downloadIcon(userIcon, new downloadIconCallback() {
                @Override
                public void onFinish() {
                    String userGender = platform.getDb().getUserGender();
                    String userId = platform.getDb().getUserId();
                    HD.LOG(platform_name + "user: " + uname + " " + userGender + " " + userId + " " + userIcon);
                    Constant.userId = userId;
                    Constant.userName = uname;
                    Constant.usergender = userGender;
                    Constant.curtoken = Constant.QQtoken1;
                    startIM(Constant.QQtoken1);
                }

                @Override
                public void onError(Exception e) {
                    HD.LOG("下载头像失败: " + e.getMessage());
                }
            });


        } else {
            platform.showUser(null);
        }
    }

    private void downloadIcon(final String icon, final downloadIconCallback listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = Glide.with(ctx).load(icon).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    HD.LOG("QQ头像本地路径： " + file.getAbsolutePath());
                    Constant.userIcon = file.getAbsolutePath();
                    if (listener != null) {
                        listener.onFinish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();
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


    //PlatformActionListener
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        HD.TLOG("onComplete: " + platform.getDb().getUserName());
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        HD.TLOG("onError: " + platform.getName());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        String uname = platform.getDb().getUserName();
        HD.TLOG("onCancel: " + uname);
    }
}
