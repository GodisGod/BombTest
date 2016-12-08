package com.example.bombtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bombtest.util.LocationUtil;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private EditText send_content;
    private Button btn_send;
    private Button btn_discover;
    private TextView tv_discover;

    private LocationManager manager;
    private String locationProvider;
    private Button location_btn;
    private double lat;
    private double lng;
    private EditText editText;
    private double range = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "9c025fdf83b8cb9dcded051b04f741dc");
        editText = (EditText) findViewById(R.id.range_et);
        send_content = (EditText) findViewById(R.id.ed_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_discover = (Button) findViewById(R.id.btn_discover);
        tv_discover = (TextView) findViewById(R.id.tv_discover);
        location_btn = (Button) findViewById(R.id.btn_lacation);
        initlocation();
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initlocation();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = send_content.getText().toString();
                if (!content.isEmpty()) {
                    PaperMessage message = new PaperMessage();
                    message.setText_message(content);
                    message.setGpsAdd(new BmobGeoPoint(lng, lat));
                    Log.i("LHD", "添加的经纬度： " + "\n" +
                            "经度：" + lng + "\n" +
                            "维度：" + lat);
                    message.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "添加数据成功，返回objectId为： " + s, Toast.LENGTH_SHORT).show();
                                Log.i("LHD", "成功：" + s);
                            } else {
                                Toast.makeText(MainActivity.this, "添加数据失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("LHD", "失败： " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });

        btn_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_discover.setText("");
                BmobQuery query = new BmobQuery("PaperMessage");
//                query.addWhereEqualTo("gpsAdd", new BmobGeoPoint(116.398, 39.914));
                String s = editText.getText().toString().trim();

                if (s.isEmpty()) {
                    range = 100;
                } else {
                    range = Double.parseDouble(editText.getText().toString());
                }
                double a = range/1000;
                Toast.makeText(MainActivity.this, "搜索范围： " + range, Toast.LENGTH_SHORT).show();
                Log.i("LHD", "range= " + a + " is:" + s.isEmpty());
                query.addWhereWithinKilometers("gpsAdd", new BmobGeoPoint(lng, lat), a);
//                query.addWhereNear("gpsAdd",new BmobGeoPoint(lng,lat));
                Log.i("LHD", "发现的经纬度： " + "\n" +
                        "经度：" + lng + "\n" +
                        "维度：" + lat);
//                query.setLimit(10);
                query.findObjects(new FindListener<PaperMessage>() {
                    @Override
                    public void done(List<PaperMessage> list, BmobException e) {
                        if (e == null) {
                            Log.i("LHD", "查询成功：共" + list.size() + "条数据。");
                            StringBuilder sb = new StringBuilder();
                            for (PaperMessage m :
                                    list) {
                                Log.i("LHD", "message: " + m.getText_message());
                                sb.append(m.getText_message());
                            }
                            tv_discover.setText(sb.toString());
                        } else {
                            Log.i("LHD", "查询失败：" + e.getMessage());
                        }
                    }
                });


//                query.addWhereEqualTo("name", "lucky");
//        query.setLimit(2);
//                query.order("createAt");
//                query.findObjectsByTable(new QueryListener<JSONArray>() {
//                    @Override
//                    public void done(JSONArray jsonArray, BmobException e) {
//                        if (e == null) {
//                            Log.i("LHD", "查詢成功：" + jsonArray.toString());
//                            tv_discover.setText(jsonArray.toString());
//                        } else {
//                            Log.i("LHD", "失敗：" + e.getMessage() + ", " + e.getErrorCode());
//                        }
//                    }
//                });
            }
        });

//        BombTestObject p2 = new BombTestObject();
//        p2.setName("hd");
//        p2.setAddress("北京海淀");
//        p2.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e==null){
//                    Toast.makeText(MainActivity.this,"添加数据成功，返回objectId为： "+s,Toast.LENGTH_SHORT).show();
//                    Log.i("LHD","成功："+s);
//                }else {
//                    Toast.makeText(MainActivity.this,"添加数据失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
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
    }

    private void initlocation() {
        //获取地理位置管理器
        manager = LocationUtil.getlocation(MainActivity.this);
        //获取所有可用的位置提供器
        List<String> providers = manager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = manager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
        //监视地理位置变化
        manager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }

    /**
     * 显示地理位置经度和纬度信息
     *
     * @param location
     */
    private void showLocation(Location location) {
        String locationStr = "维度：" + location.getLatitude() + "\n"
                + "经度：" + location.getLongitude();
        Toast.makeText(MainActivity.this, "维度：" + location.getLatitude() + "\n"
                + "经度：" + location.getLongitude(), Toast.LENGTH_SHORT).show();
        lat = location.getLatitude();
        lng = location.getLongitude();
        Log.i("LHD", "location:  " + locationStr);

    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            showLocation(location);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            //移除监听器
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.removeUpdates(locationListener);
        }
    }
}
