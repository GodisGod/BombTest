package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.bombtest.R;
import com.example.bombtest.util.HD;

public class ChooseScrip extends AppCompatActivity implements View.OnClickListener {
    private Context ctx;
    private ImageView choose1;
    private ImageView choose2;
    private ImageView choose3;
    private Intent intent;
    private String img_url;
    private String text;
    private String objectId;
    private String userId;
    private String userName;
    private String userIcon;
    private String userGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_scrip);
        HD.LOG("onCreate ChooseScrip");
        ctx = this;
        initView();
        intent = getIntent();
        img_url = intent.getStringExtra("imgurl");
        text = intent.getStringExtra("text");
        objectId = intent.getStringExtra("objectid");
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userIcon = intent.getStringExtra("Cur_userIcon");
        userGender = intent.getStringExtra("gender");
    }

    private void initView() {
        choose1 = (ImageView) findViewById(R.id.scrip1);
        choose2 = (ImageView) findViewById(R.id.scrip2);
        choose3 = (ImageView) findViewById(R.id.scrip3);

        choose1.setOnClickListener(this);
        choose2.setOnClickListener(this);
        choose3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ctx, Scrip.class);
        intent.putExtra("imgurl", img_url);
        intent.putExtra("text", text);
        intent.putExtra("objectid", objectId);
        intent.putExtra("userId", userId);
        intent.putExtra("gender", userGender);
        startActivity(intent);
        finish();
    }

}
