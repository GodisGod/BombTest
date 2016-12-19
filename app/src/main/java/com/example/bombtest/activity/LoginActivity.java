package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static com.example.bombtest.constant.Constant.userIcon;
import static com.example.bombtest.constant.Constant.userId;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {

    private Context ctx;
    private Button user1_123;
    private Button user1_222;
    private Button user1_001;
    private Button pre_user;
    private Button login_QQ;
    private Button login_Weixin;
    private Button logic_cloud;
    private EditText user_account;
    private EditText user_password;
    private Button user_login;
    private ImageView choose_user_icon;
    private String token;
    private String account;
    private String password;
    private String QQpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        initView();
    }


    private void initView() {
        user1_001 = (Button) findViewById(R.id.choose_user1);
        user1_123 = (Button) findViewById(R.id.choose_user2);
        user1_222 = (Button) findViewById(R.id.choose_user3);
        pre_user = (Button) findViewById(R.id.choose_preuser);
        login_QQ = (Button) findViewById(R.id.login_by_QQ);
        login_Weixin = (Button) findViewById(R.id.login_by_weixin);
        logic_cloud = (Button) findViewById(R.id.logic_cloud);
        user_account = (EditText) findViewById(R.id.user_account);
        user_password = (EditText) findViewById(R.id.user_password);
        user_login = (Button) findViewById(R.id.user_login);
        choose_user_icon = (ImageView) findViewById(R.id.chooser_user_icon);
        user1_001.setOnClickListener(this);
        user1_123.setOnClickListener(this);
        user1_222.setOnClickListener(this);
        pre_user.setOnClickListener(this);
        login_QQ.setOnClickListener(this);
        login_Weixin.setOnClickListener(this);
        choose_user_icon.setOnClickListener(this);
        logic_cloud.setOnClickListener(this);
        user_login.setOnClickListener(this);
    }

    private void uploadUserinfo(String id, String password, String icon, String name, String gender, final String sign) {
        final User userinfo = new User();
        //上传用户信息
        userinfo.setUser_id(id);  //添加用户ID
        userinfo.setUser_password(password);//添加用户密码
        userinfo.setUser_name(name);//添加用户名
        userinfo.setUser_gender(gender);//添加用户性别
        SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
        edit.putString(userId, Constant.curtoken);
        edit.putString("USER_TOKEN", Constant.curtoken);
        HD.LOG("保存token: " + Constant.curtoken);
        edit.apply();
        //添加图片
        if (!userIcon.isEmpty()) {
            final File file = new File(icon);
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    userinfo.setUser_icon(bmobFile);//添加用户头像
                    userinfo.setUser_sign(sign);//添加用户签名
                    userinfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            HD.TLOG("用户信息保存到数据库成功: " + s);
                            HD.TLOG("userinfo: " + userinfo.getUser_id() + " " + userinfo.getUser_name() + "  " + userinfo.getUser_sign());
//                            RongIM.connect(Constant.curtoken, new RongIMClient.ConnectCallback() {
//
//                                /**
//                                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
//                                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
//                                 */
//                                @Override
//                                public void onTokenIncorrect() {
//                                    Log.i("LHD", "--onTokenIncorrect");
//                                }
//
//                                /**
//                                 * 连接融云成功
//                                 * @param userid 当前 token 对应的用户 id
//                                 */
//                                @Override
//                                public void onSuccess(String userid) {
//                                    Log.i("LHD", "--onSuccess" + userid);
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                    finish();
//                                }
//
//                                /**
//                                 * 连接融云失败
//                                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
//                                 */
//                                @Override
//                                public void onError(RongIMClient.ErrorCode errorCode) {
//                                    Log.i("LHD", "onError: " + errorCode);
//                                }
//                            });
                            getTokenFromCloud(Constant.userId, Constant.userName, Constant.userIcon);
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
                userId = "001";
                Constant.userName = "大娃出山啦";
                Constant.usergender = "f";
                Constant.userPassword = "123";
                Constant.curtoken = Constant.token1;
                Constant.sign = "测试用户1";
                uploadUserinfo(userId, Constant.userPassword, userIcon, Constant.userName, Constant.usergender, Constant.sign);
                break;
            case R.id.choose_user2:
                userId = "n002";
                Constant.userPassword = "123";
                Constant.userName = "新用户2";
                Constant.usergender = "f";
                Constant.curtoken = Constant.token2;
                Constant.sign = "测试用户2";
                uploadUserinfo(userId, Constant.userPassword, userIcon, Constant.userName, Constant.usergender, Constant.sign);
                break;
            case R.id.choose_user3:
                userId = "003";
                Constant.userPassword = "123";
                Constant.userName = "测试用户3";
                Constant.usergender = "m";
                Constant.curtoken = Constant.token3;
                Constant.sign = "测试用户3";
                uploadUserinfo(userId, Constant.userPassword, userIcon, Constant.userName, Constant.usergender, Constant.sign);
                break;
            case R.id.chooser_user_icon:
                startActivityForResult(new Intent(ctx, PhotosWall.class), 2);
                break;
            case R.id.choose_preuser:
                if (DemoContext.getInstance() != null) {
                    token = DemoContext.getInstance().getSharedPreferences().getString("USER_TOKEN", "default");
                    Constant.curtoken = token;
                }
                //todo 这些信息应该保存在文件里
                uploadUserinfo(userId, Constant.userPassword, userIcon, Constant.userName, Constant.usergender, Constant.sign);
                break;
            case R.id.login_by_QQ:
                loginWithQQorWechat(QQ.NAME);
                break;
            case R.id.login_by_weixin:
                loginWithQQorWechat(Wechat.NAME);
                break;
            case R.id.logic_cloud:
//                getTokenFromCloud(Constant.RuserId,Constant.RuserName,Constant.RuserIcon);
                startActivity(new Intent(ctx, RegisteActivity.class));
                break;
            case R.id.user_login:
                account = user_account.getText().toString();
                password = user_password.getText().toString();
                BmobQuery<User> query = new BmobQuery<User>("User");
                query.addWhereEqualTo("user_id", account);
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        User user = list.get(0);
                        String account_remote = user.getUser_id();
                        String password_remote = user.getUser_password();
                        HD.LOG("服务器账号： " + account_remote + " 密码： " + password_remote);
                        if (account.equals(account_remote) && password.equals(password_remote)) {
                            //先从本地的缓存文件获取，如果没有就访问服务器获取,如果有就直接登录
                            token = DemoContext.getInstance().getSharedPreferences().getString(account, "no");
                            Constant.curtoken = token;
                            Constant.sign = user.getUser_sign();
                            Constant.usergender = user.getUser_gender();
                            Constant.userName = user.getUser_name();
                            Constant.userId = user.getUser_id();
                            Constant.userIcon = user.getUser_icon().getFileUrl();
                            Constant.userPassword = user.getUser_password();
                            if (token.equals("no")) {
                                getTokenFromCloud(account, user.getUser_name(), user.getUser_icon().getFileUrl());
                            } else {
                                startActivity(new Intent(ctx, MainActivity.class));
                            }
                        } else {
                            HD.TLOG("账号或密码错误");
                        }
                    }
                });
                break;
        }
    }

    private void loginWithQQorWechat(final String platform_name) {
        final Platform platform = ShareSDK.getPlatform(this, platform_name);
        platform.setPlatformActionListener(this);
        HD.TLOG("platform.isValid(): " + platform.isValid());
        if (platform.isValid()) {
            final String uname = platform.getDb().getUserName();
            final String usericon = platform.getDb().getUserIcon();
            password = platform.getDb().getUserId() + "qq_login";
            //下载图片到本地,开启子线程
            downloadIcon(userIcon, new downloadIconCallback() {
                @Override
                public void onFinish() {
                    String userGender = platform.getDb().getUserGender();
                    String userid = platform.getDb().getUserId();
                    HD.LOG(platform_name + "user: " + uname + " " + userGender + " " + userId + " " + userIcon);
                    Constant.userId = userid;
                    Constant.userPassword = password;
                    Constant.userIcon = usericon;
                    Constant.userName = uname;
                    Constant.usergender = userGender;
                    Constant.sign = "QQ登录";
                    HD.LOG(userId + " " + Constant.userPassword + " " + userIcon + " " + Constant.userName + " " + Constant.usergender);
                    uploadUserinfo(userId, Constant.userPassword, userIcon, Constant.userName, Constant.usergender, Constant.sign);
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
                    userIcon = file.getAbsolutePath();
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

    private void getTokenFromCloud(String userId, String userName, String userIcon) {
        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
//第一个参数是上下文对象，第二个参数是云端逻辑的方法名称，第三个参数是上传到云端逻辑的参数列表（JSONObject cloudCodeParams），第四个参数是回调类
        JSONObject cloudCodeParams = new JSONObject();
        try {
            cloudCodeParams.put("userId", userId);
            cloudCodeParams.put("name", userName);
            cloudCodeParams.put("portraitUri", userIcon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ace.callEndpoint("getToken", cloudCodeParams, new CloudCodeListener() {
            @Override
            public void done(Object object, BmobException e) {
                if (e == null) {
                    String result1 = object.toString().replace("\\", "");
                    String result = result1.substring(1, result1.length() - 1);
                    HD.TLOG("云端逻辑返回值：" + result);
                    //解析token
                    analyResultToGetToken(result);
                } else {
                    HD.LOG(" " + e.getMessage());
                }
            }
        });
    }

    private void analyResultToGetToken(String json) {
        try {
            JSONObject object = new JSONObject(json);
            token = object.getString("token");
            Constant.curtoken = token;
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
                    startActivity(new Intent(ctx, MainActivity.class));
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
            SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
            edit.putString(userId, Constant.curtoken);
            HD.LOG("保存token: " + Constant.curtoken);
            edit.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                userIcon = data.getStringExtra("imgurl");
                Glide.with(ctx).load(userIcon)
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
