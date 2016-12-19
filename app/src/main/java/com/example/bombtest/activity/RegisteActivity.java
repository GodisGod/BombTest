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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bombtest.DemoContext;
import com.example.bombtest.R;
import com.example.bombtest.bean.User;
import com.example.bombtest.constant.Constant;
import com.example.bombtest.util.HD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class RegisteActivity extends AppCompatActivity implements View.OnClickListener {
    //ID NAME Sign gender icon
    private EditText user_id;
    private EditText user_password;
    private EditText user_name;
    private ImageView user_icon;
    private Button btn_reg;
    private Context ctx;
    //    private String[] gender = {"f", "m", "no"};
    private String gender = "m";
    private Random random;
    private String userId;
    private String userPassword;
    private String userName;
    private String userIcon;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        ctx = this;
        random = new Random();
        initView();

    }

    private void initView() {
        user_id = (EditText) findViewById(R.id.reg_et_user_id);
        user_password = (EditText) findViewById(R.id.user_password);
        user_name = (EditText) findViewById(R.id.reg_et_user_name);
        user_icon = (ImageView) findViewById(R.id.reg_img_user_icon);
        btn_reg = (Button) findViewById(R.id.reg_btn_reg);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioButton.getId() == R.id.radioMale) {
                    gender = "m";
                } else if (radioButton.getId() == R.id.radioFemale) {
                    gender = "f";
                } else {
                    gender = "no";
                }
            }
        });
        user_icon.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg_img_user_icon:
                startActivityForResult(new Intent(ctx, PhotosWall.class), 3);
                break;
            case R.id.reg_btn_reg:
                userId = user_id.getText().toString();//ID
                userPassword = user_password.getText().toString();
                userName = user_name.getText().toString();//Name
                if (userId.isEmpty() | userName.isEmpty() | userIcon.isEmpty()|userPassword.isEmpty()) {
                    HD.TLOG("信息不完整");
                    return;
                }
                Constant.userId = userId;
                Constant.userPassword = userPassword;
                Constant.userName = userName;
                if (gender.isEmpty()) {
                    HD.TLOG("请选择性别");
                    return;
                }
                Constant.usergender = gender;
                uploadUserinfo();
                break;
        }
    }

    private void uploadUserinfo() {
        final User userinfo = new User();
        //上传用户信息
        userinfo.setUser_id(Constant.userId);  //添加用户ID
        userinfo.setUser_password(Constant.userPassword);
        userinfo.setUser_name(Constant.userName);//添加用户名
        userinfo.setUser_gender(Constant.usergender);//添加用户性别
        userinfo.setUser_sign("鸿达的新签名");//添加用户签名
        //添加头像
        if (!Constant.userIcon.isEmpty()) {
            final File file = new File(userIcon);
            BmobFile bmobFile = new BmobFile(file);
            userinfo.setUser_icon(bmobFile);//添加用户头像
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    userinfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            HD.TLOG("用户信息保存到数据库成功: " + s);
                            //查询头像url
                            BmobQuery<User> query = new BmobQuery<User>("User");
                            query.addWhereEqualTo("user_id", userId);
                            query.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> list, BmobException e) {
                                    if (e == null) {
                                        userIcon = list.get(0).getUser_icon().getFileUrl();
                                        HD.TLOG("findObjects: " + list.get(0).getUser_name() + "  " + list.get(0).getUser_icon().getFileUrl());
                                        //将新用户数据添加到数据库User表中
                                        getTokenFromCloud(userId, userName, userIcon);
                                    } else {
                                        HD.LOG("失敗：" + e.getMessage() + ", " + e.getErrorCode());
                                    }
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
            HD.TLOG("解析token: " + userId + "  " + token);
            SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
            edit.putString("USER_TOKEN", Constant.curtoken);
            HD.LOG("保存token: " + Constant.curtoken);
            edit.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                Constant.userIcon = data.getStringExtra("imgurl");//更新头像
                userIcon = Constant.userIcon;
                Glide.with(ctx).load(Constant.userIcon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .centerCrop()  //转换宽高比
                        .into(user_icon);
            }
        }
    }
}
