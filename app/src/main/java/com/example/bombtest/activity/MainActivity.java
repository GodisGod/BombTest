package com.example.bombtest.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bombtest.R;
import com.example.bombtest.bean.PaperMessage;
import com.example.bombtest.util.HD;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.ProgressCallback;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context ctx;
    private EditText send_content;
    private Button btn_send;
    private Button jump;
    private Button jump_discover;
    private ImageView send_img_choose;

    private double lat;
    private double lng;

    private String img_url;
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
                    Log.i("LHD", "lat: " + lat + "   lng: " + lng);
                    HD.TOS("lat: " + lat + "   lng: " + lng + "\n" + "位置： " + amapLocation.getAddress());
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
        setContentView(R.layout.activity_main);

        ctx = this;
        initview();

        initLocation();

    }

    private void initview() {
        send_content = (EditText) findViewById(R.id.ed_content);

        send_img_choose = (ImageView) findViewById(R.id.img_send);

        jump = (Button) findViewById(R.id.jump_btn);
        jump_discover = (Button) findViewById(R.id.jump_to_discover);
        btn_send = (Button) findViewById(R.id.btn_send);


        btn_send.setOnClickListener(this);
        send_img_choose.setOnClickListener(this);
        jump.setOnClickListener(this);
        jump_discover.setOnClickListener(this);
    }


    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                final String content = send_content.getText().toString();
                final PaperMessage message = new PaperMessage();
                //TODO 模拟添加用户id
                message.setUser_id(Build.USER);

                if (!content.isEmpty()) {
                    //添加文字
                    message.setText_message(content);
                    //添加经纬度
                    message.setGpsAdd(new BmobGeoPoint(lng, lat));
                    HD.LOG("添加的经纬度： " + "\n" +
                            "经度：" + lng + "\n" +
                            "维度：" + lat);
                    HD.TOS("添加的经纬度： " + "\n" +
                            "经度：" + lng + "\n" +
                            "维度：" + lat);
                }
                HD.LOG("1");
                //添加图片
                if (!img_url.isEmpty()) {
                    final File file = new File(img_url);
                    final BmobFile bmobFile = new BmobFile(file);
                    HD.LOG("2 " + (bmobFile == null) + " imgulr: " + file.getAbsolutePath());

                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                HD.TOS("上传成功" + img_url);
                                Log.i("LHD", "成功：" + img_url);
                                message.setIcon(bmobFile);
                                //TODO 模拟添加语音
                                message.setAudio(bmobFile);
                                message.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        HD.LOG("纸片上传成功！");
                                        if (e == null) {
                                            HD.TOS("添加数据成功，返回objectId为： " + s);
                                            Log.i("LHD", "成功：" + s);
                                        } else {
                                            HD.TOS("添加数据失败" + e.getMessage());
                                            Log.i("LHD", "失败： " + e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                HD.TOS("纸片上传失败" + e.getMessage());
                                HD.LOG("失败： " + e.getMessage());
                            }
                        }

                        @Override
                        public void onProgress(Integer value) {
                            super.onProgress(value);
                            HD.LOG("progress: " + value);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            HD.LOG("onFinish");
                        }
                    });
                    bmobFile.uploadObservable(new ProgressCallback() {
                        @Override
                        public void onProgress(Integer integer, long l) {
                            HD.LOG("uploadObservable: " + l);
                        }
                    });
                }
                break;

            case R.id.img_send:
                startActivityForResult(new Intent(ctx, PhotosWall.class), 1);
                break;
            case R.id.jump_btn:
                startActivity(new Intent(ctx, Loginwithother.class));
                break;
            case R.id.jump_to_discover:
                startActivity(new Intent(ctx, Discover.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                img_url = data.getStringExtra("imgurl");

                Glide.with(ctx).load(img_url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .centerCrop()  //转换宽高比
                        .into(send_img_choose);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除监听器
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。

    }
}


//        BombTestObject p2 = new BombTestObject();
//        p2.setName("hd");
//        p2.setAddress("北京海淀");
//        p2.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e==null){
//                    Toast.makeText(ctx,"添加数据成功，返回objectId为： "+s,);
//                    Log.i("LHD","成功："+s);
//                }else {
//                    Toast.makeText(ctx,"添加数据失败"+e.getMessage(),);
//                    Log.i("LHD","失败： "+e.getMessage());
//                }
//            }
//        });
//
//        final BombTestObject p3 = new BombTestObject();
//        p3.setAddress("上海浦东");
//        p3.update("6c73ce462d", new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e==null){
//                    Log.i("LHD","更新成功"+p3.getUpdatedAt());
//                }else {
//                    Log.i("LHD","更新失败"+e.getMessage());
//                }
//            }
//        });
//
//        final BombTestObject p4 = new BombTestObject();
//        p4.setObjectId("6c73ce462d");
//        p4.delete(new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e==null){
//                    Log.i("LHD","刪除成功"+p4.getUpdatedAt());
//                }else {
//                    Log.i("LHD","刪除失败"+e.getMessage());
//                }
//            }
//        });
//
//        BmobQuery query = new BmobQuery("BombTestObject");
//        query.addWhereEqualTo("name","lucky");
////        query.setLimit(2);
//        query.order("createAt");
//        query.findObjectsByTable(new QueryListener<JSONArray>() {
//            @Override
//            public void done(JSONArray jsonArray, BmobException e) {
//               if (e==null){
//                   Log.i("LHD","查詢成功："+jsonArray.toString());
//               }   else {
//                   Log.i("LHD","失敗："+e.getMessage()+", "+e.getErrorCode());
//               }
//            }
//        });
