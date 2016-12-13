package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bombtest.R;
import com.example.bombtest.bean.PaperMessage;
import com.example.bombtest.util.HD;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Scrip extends AppCompatActivity {

    private Context ctx;
    private ImageView scrip_img;
    private TextView scrip_content;
    private Intent intent;
    private String img_url;
    private String text;
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrip);
        ctx = this;
        intent = getIntent();
        img_url = intent.getStringExtra("imgurl");
        text = intent.getStringExtra("text");
        objectId = intent.getStringExtra("objectid");
        HD.LOG("imgurl: " + img_url);
        HD.LOG("text: " + text);
        HD.LOG("objectId: " + objectId);
        final PaperMessage paperMessage = new PaperMessage();
        paperMessage.setObjectId(objectId);
        paperMessage.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("LHD", "刪除成功" + paperMessage.getUpdatedAt());
                } else {
                    Log.i("LHD", "刪除失败" + e.getMessage());
                }
            }
        });
        initview();

        initData();

    }

    private void initview() {
        scrip_img = (ImageView) findViewById(R.id.scrip_image);
        scrip_content = (TextView) findViewById(R.id.scrip_text);
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
}
