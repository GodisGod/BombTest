package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.bombtest.R;
import com.example.bombtest.bean.PaperMessage;
import com.example.bombtest.constant.Constant;
import com.example.bombtest.util.HD;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Discover extends AppCompatActivity implements View.OnClickListener {
    private Button btn_discover;
    private EditText edit_text;

    private double range = 100;
    private double lat;
    private double lng;
    private Context ctx;
    //高德定位
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    lng = amapLocation.getLongitude();
                    lat = amapLocation.getLatitude();
                    HD.LOG("Dlat: " + lat + "   Dlng: " + lng);
                    HD.TOS("Dlat: " + lat + "   Dlng: " + lng + "\n" + "位置： " + amapLocation.getAddress());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("LHD", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    HD.TOS("location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        ctx = this;
        initView();
        initLocation();
    }

    private void initView() {
        edit_text = (EditText) findViewById(R.id.range_et);
        btn_discover = (Button) findViewById(R.id.btn_discover);
        btn_discover.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_discover:
                BmobQuery query = new BmobQuery("PaperMessage");
                String s = edit_text.getText().toString().trim();

                if (s.isEmpty()) {
                    range = 50;
                } else {
                    range = Double.parseDouble(edit_text.getText().toString());
                }
                double a = range / 1000;
                HD.TOS("D搜索范围： " + range + " Dlat: " + lat + " ,Dlng: " + lng);
                query.addWhereWithinKilometers("gpsAdd", new BmobGeoPoint(lng, lat), a);
//                query.addWhereNear("gpsAdd",new BmobGeoPoint(lng,lat));
                Log.i("LHD", "D发现的经纬度： " + "\n" +
                        "D经度：" + lng + "\n" +
                        "D维度：" + lat);
                query.setLimit(3);
                query.findObjects(new FindListener<PaperMessage>() {
                    @Override
                    public void done(List<PaperMessage> list, BmobException e) {
                        if (e == null) {
                            Log.i("LHD", "查询成功：共" + list.size() + "条数据。");
                            HD.TOS("查询成功：共" + list.size() + "条数据。");
                            StringBuilder sb = new StringBuilder();
                            for (PaperMessage m : list) {
                                Log.i("LHD", "message: " + m.getSend_text_message());
                                sb.append(m.getSend_text_message() + "  D上传的图片：" + m.getSend_img_message().getFileUrl() + "\n");
                                if (m.getUser_id() != Constant.userId) {
                                    Intent intent = new Intent(ctx, ChooseScrip.class);
                                    intent.putExtra("userId", m.getUser_id());
                                    intent.putExtra("userName", m.getUser_name());
                                    intent.putExtra("userIcon", m.getUser_icon().getFileUrl());
                                    intent.putExtra("objectid", m.getObjectId());
                                    intent.putExtra("imgurl", m.getSend_img_message().getFileUrl());
                                    intent.putExtra("text", m.getSend_text_message());
                                    intent.putExtra("audio", m.getSend_audio());
                                    startActivity(intent);
                                }
                            }


                        } else {
                            Log.i("LHD", "查询失败：" + e.getMessage());
                        }
                    }
                });
                break;
        }
    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);

        // 仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息。
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);

//       获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
}
