package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.bombtest.R;
import com.example.bombtest.adapter.ScripAdapter;
import com.example.bombtest.bean.PaperMessage;
import com.example.bombtest.bean.PaperMessageUser;
import com.example.bombtest.bean.User;
import com.example.bombtest.constant.Constant;
import com.example.bombtest.util.HD;
import com.example.bombtest.util.view.SwpipeListViewOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Discover extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

    private double range = 500;
    private double lat;
    private double lng;
    private Context ctx;
    private Intent intent;

    private List<PaperMessageUser> scrips;
    private ScripAdapter adapter;
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
        scrips = new ArrayList<PaperMessageUser>();
        adapter = new ScripAdapter(this, scrips);
        initView();
        initLocation();
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.discover_swip);
        listView = (ListView) findViewById(R.id.list_scrip);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PaperMessageUser p = scrips.get(i);
                intent = new Intent(ctx, Scrip.class);
                intent.putExtra("target_userId", p.getUser_id());
                intent.putExtra("objectid", p.getObjectId());
                intent.putExtra("target_userIcon", p.getUserIcon());
                if (p.getSend_img_message() == null) {
                    intent.putExtra("imgurl", "");
                } else {
                    intent.putExtra("imgurl", p.getSend_img_message().getFileUrl());
                }
                if (p.getSend_text_message().isEmpty()) {
                    intent.putExtra("text", "");
                } else {
                    intent.putExtra("text", p.getSend_text_message());
                }
                if (p.getSend_audio() == null) {
                    intent.putExtra("audio", "");
                } else {
                    intent.putExtra("audio", p.getSend_audio().getFileUrl());
                }

                intent.putExtra("target_gender", p.getGender());

                startActivity(intent);
            }
        });
        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                HD.TLOG("长按删除");
                return false;
            }
        });
        listView.setOnScrollListener(new SwpipeListViewOnScrollListener(swipeRefreshLayout, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        }));


        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_green_light));
        swipeRefreshLayout.setOnRefreshListener(this);
        //一进入这个Activity就自动刷新
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //这行代码并不会触发onRefresh
                swipeRefreshLayout.setRefreshing(true);
                //必须手动调用
                onRefresh();
            }
        });
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

    @Override
    public void onRefresh() {
        scrips.clear();
        BmobQuery query = new BmobQuery("PaperMessage");
        double a = range / 1000;
        query.addWhereWithinKilometers("gpsAdd", new BmobGeoPoint(lng, lat), a);
//                query.addWhereNear("gpsAdd",new BmobGeoPoint(lng,lat));
        Log.i("LHD", "D发现的经纬度： " + "\n" +
                "D经度：" + lng + "\n" +
                "D维度：" + lat);
        //最多展示附近的10条数据
        query.setLimit(10);
        query.findObjects(new FindListener<PaperMessage>() {
            @Override
            public void done(List<PaperMessage> list, BmobException e) {
                if (e == null) {
                    HD.LOG("查询成功：共" + list.size() + "条数据。");
                    for (final PaperMessage m : list) {
                        HD.LOG("the message: " + m.getSend_text_message() + " | " + m.getGender());
                        HD.LOG("====" + (!m.getUser_id().equals(Constant.Cur_userId) && !m.getGender().equals(Constant.usergender)));
                        //将不是本人的、异性的纸片加入到scrips中
                        if (!m.getUser_id().equals(Constant.Cur_userId) && !m.getGender().equals(Constant.usergender)) {
                            BmobQuery<User> query = new BmobQuery<User>("User");
                            query.addWhereEqualTo("user_id", m.getUser_id());
                            query.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> list, BmobException e) {
                                    if (e == null) {
                                        HD.LOG("展示用户的头像 findObjects: " + list.get(0).getUser_name() + "  " + list.get(0).getUser_icon().getFileUrl());
                                        PaperMessageUser p = new PaperMessageUser();
                                        p.setGender(m.getGender());
                                        p.setGpsAdd(m.getGpsAdd());
                                        p.setSend_audio(m.getSend_audio());
                                        p.setSend_img_message(m.getSend_img_message());
                                        p.setSend_text_message(m.getSend_text_message());
                                        p.setType(m.getType());
                                        p.setUser_id(m.getUser_id());
                                        p.setUserIcon(list.get(0).getUser_icon().getFileUrl());
                                        p.setCreateTime(m.getCreatedAt());
                                        p.setObjectId(m.getObjectId());
                                        scrips.add(p);
                                        adapter.notifyDataSetChanged();
                                        HD.TLOG("发现的纸片： " + m.getUser_id() + "  " + m.getGender() + "  " + scrips.size());
                                    } else {
                                        HD.LOG("失敗：" + e.getMessage() + ", " + e.getErrorCode());
                                    }
                                }
                            });
                            //如果发现了10条数据就终止循环（最多展示10条）
                            if (scrips.size() >= 10) {
                                break;
                            }
                        }
                    }
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            //必须这样关闭
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    HD.TLOG("discover查询失败：" + e.getMessage());
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            //必须这样关闭
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }


}
