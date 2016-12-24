package com.example.bombtest.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bombtest.R;
import com.example.bombtest.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.rong.imlib.model.UserInfo;

public class MeActivity extends AppCompatActivity {

    private Context ctx;
    private ImageView Me_User_Icon;
    private TextView Me_User_Sign;
    private TextView Me_User_Name;

    private String user_sign;
    private Uri user_icon;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ctx = this;
        initView();

        Bundle bundle = getIntent().getBundleExtra("userinfo");
        UserInfo userInfo = bundle.getParcelable("userinfo");
        user_icon = userInfo.getPortraitUri();
        user_name = userInfo.getName();
        Me_User_Name.setText(user_name);
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("user_id", userInfo.getUserId());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                user_sign = list.get(0).getUser_sign();
                Glide.with(ctx).load(user_icon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .centerCrop()  //转换宽高比
                        .into(Me_User_Icon);
                Me_User_Sign.setText(user_sign);
            }
        });

    }

    private void initView() {
        Me_User_Icon = (ImageView) findViewById(R.id.ME_User_Icon);
        Me_User_Sign = (TextView) findViewById(R.id.ME_User_Sign);
        Me_User_Name = (TextView) findViewById(R.id.ME_User_Name);
    }
}
