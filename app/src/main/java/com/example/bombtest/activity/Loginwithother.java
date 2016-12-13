package com.example.bombtest.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.bombtest.R;
import com.example.bombtest.util.ShareUtil;

public class Loginwithother extends AppCompatActivity implements View.OnClickListener {

    private Context ctx;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginwithother);
        ctx = this;
        initView();
    }

    private void initView() {
        login = (Button) findViewById(R.id.login_btn);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                ShareUtil.showShare(ctx);
                break;
        }
    }
}
