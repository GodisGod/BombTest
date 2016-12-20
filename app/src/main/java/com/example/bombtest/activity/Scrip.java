package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bombtest.R;
import com.example.bombtest.bean.User;
import com.example.bombtest.util.GlideCircleTransform;
import com.example.bombtest.util.HD;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.rong.imkit.RongIM;

public class Scrip extends AppCompatActivity implements View.OnClickListener {

    private Context ctx;
    private ImageView scrip_img;
    private TextView scrip_content;
    private Intent intent;
    private String img_url;
    private String text;
    private String objectId;
    private String target_userId;
    private String target_userIcon;
    private String userName;

    private ImageView target_img;
    private Button huifu;
    private Button gone_with_wind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrip);
        ctx = this;

        intent = getIntent();
        img_url = intent.getStringExtra("imgurl");
        text = intent.getStringExtra("text");
        objectId = intent.getStringExtra("objectid");
        target_userId = intent.getStringExtra("target_userId");
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("user_id",target_userId);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                userName = list.get(0).getUser_name();
            }
        });
        target_userIcon = intent.getStringExtra("target_userIcon");
        HD.LOG("imgurl: " + img_url);
        HD.LOG("text: " + text);
        HD.LOG("objectId: " + objectId);

        initview();

        initData();

    }

    private void initview() {
        scrip_img = (ImageView) findViewById(R.id.scrip_image);
        scrip_content = (TextView) findViewById(R.id.scrip_text);
        target_img = (ImageView) findViewById(R.id.target_user_icon);
        huifu = (Button) findViewById(R.id.huifu);
        gone_with_wind = (Button) findViewById(R.id.gone_with_wind);

        huifu.setOnClickListener(this);
        gone_with_wind.setOnClickListener(this);
        Glide.with(ctx).load(target_userIcon)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new GlideCircleTransform(ctx))
                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()  //转换宽高比
                .into(target_img);
    }

    private void initData() {
        //UI显示
        scrip_content.setText(text);
        Glide.with(ctx).load(img_url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()  //转换宽高比
                .into(scrip_img);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.huifu:
                //todo 开启私聊页面
                if (RongIM.getInstance() != null) {
                    HD.LOG("开启私聊页面  "+target_userId);
                    RongIM.getInstance().startPrivateChat(ctx, target_userId, userName);
                    //todo 接收预置消息
                    finish();
                }
                break;
            case R.id.gone_with_wind:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
