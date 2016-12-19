package com.example.bombtest.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bombtest.R;
import com.example.bombtest.bean.PaperMessage;
import com.example.bombtest.constant.Constant;
import com.example.bombtest.util.HD;

import org.json.JSONArray;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.rong.imkit.RongIM;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context ctx;
    private EditText send_content;
    private Button btn_send_text;
    private Button btn_send_audio;
    private Button btn_send_img;
    private Button btn_send_img_text;
    private Button jump;
    private Button jump_discover;
    private Button jump_conversation_list;
    private Button query_userinfo;
    private ImageView send_img_choose;
    private ImageView choose_user_icon;
    private TextView title;
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
        title = (TextView) findViewById(R.id.main_title);
        title.setText("当前用户： " + Constant.userId + "  " + Constant.userName);
        send_img_choose = (ImageView) findViewById(R.id.img_send);
        choose_user_icon = (ImageView) findViewById(R.id.img_user_icon);

        jump = (Button) findViewById(R.id.jump_btn);
        jump_discover = (Button) findViewById(R.id.jump_to_discover);
        btn_send_text = (Button) findViewById(R.id.btn_send_text);
        btn_send_audio = (Button) findViewById(R.id.btn_send_audio);
        btn_send_img = (Button) findViewById(R.id.btn_send_img);
        btn_send_img_text = (Button) findViewById(R.id.btn_send_img_text);
        jump_conversation_list = (Button) findViewById(R.id.jump_conversation_list);
        query_userinfo = (Button) findViewById(R.id.query_userinfo);
        btn_send_text.setOnClickListener(this);
        btn_send_audio.setOnClickListener(this);
        btn_send_img.setOnClickListener(this);
        btn_send_img_text.setOnClickListener(this);
        send_img_choose.setOnClickListener(this);
        choose_user_icon.setOnClickListener(this);
        jump.setOnClickListener(this);
        jump_discover.setOnClickListener(this);
        jump_conversation_list.setOnClickListener(this);
        query_userinfo.setOnClickListener(this);
        Glide.with(ctx).load(Constant.userIcon)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()  //转换宽高比
                .into(choose_user_icon);
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
            case R.id.btn_send_text:
                final String content = send_content.getText().toString();
                sendMessage(content, Constant.Paper_TEXT, null);
                break;
            case R.id.btn_send_audio:
                //todo 上传语音
                HD.TLOG("待开发...");
//                sendMessage(null, Constant.Paper_AUDIO, null);
                break;
            case R.id.btn_send_img:
                if (img_url.isEmpty()) {
                    HD.TOS("请选择图片");
                    return;
                }
                File file = new File(img_url);
                BmobFile bmobFile = new BmobFile(file);
                sendMessage("", Constant.Paper_IMG, bmobFile);
                break;
            case R.id.btn_send_img_text:
                String content2 = send_content.getText().toString();
                if (img_url.isEmpty()) {
                    HD.TOS("请选择图片");
                    return;
                }
                File file2 = new File(img_url);
                BmobFile bmobFile2 = new BmobFile(file2);
                if (content2.isEmpty()) {//如果文字为空,则上传的是图片消息而不是图文消息
                    sendMessage("", Constant.Paper_IMG, bmobFile2);
                } else {//如果文字不为空,则上传的是图文消息
                    sendMessage(content2, Constant.Paper_TEXT_IMG, bmobFile2);
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
            case R.id.jump_conversation_list:
                if (RongIM.getInstance() != null) {
                    HD.LOG("jump_conversation_list");
                    RongIM.getInstance().startConversationList(MainActivity.this);
                }
                break;
            case R.id.query_userinfo:
                BmobQuery query = new BmobQuery("User");
                query.addWhereEqualTo("user_id", Constant.userId);
//        query.setLimit(2);
                query.order("createAt");
                query.findObjectsByTable(new QueryListener<JSONArray>() {
                    @Override
                    public void done(JSONArray jsonArray, BmobException e) {
                        if (e == null) {
                            HD.TLOG("查詢成功：" + jsonArray.toString());
                        } else {
                            HD.TLOG("失敗：" + e.getMessage() + ", " + e.getErrorCode());
                        }
                    }
                });
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

    private void sendMessage(String text, int type, BmobFile bmobFile) {
        final PaperMessage message = new PaperMessage();
        if (!text.isEmpty()) {//添加文字
            message.setSend_text_message(text);
        } else {
            message.setSend_text_message(null);
        }
        if (type == 1) {//如果是文字纸片将    将图片和语音置空
            if (text.isEmpty()) {//添加文字
                HD.TLOG("请添加文字");
                return;
            }
            message.setType(1);
            message.setSend_img_message(null);
            message.setSend_audio(null);
        }
        if (type == 2) {//如果是语音纸片   将图片和文字置空
            message.setType(2);
            message.setSend_audio(bmobFile);
            message.setSend_text_message("");
            message.setSend_img_message(null);
        }
        if (type == 3) {//如果是图片纸片   将语音和文字置空
            message.setType(3);
            message.setSend_audio(null);
            message.setSend_text_message("");
            message.setSend_img_message(bmobFile);
        }
        if (type == 4) {//如果是图文纸片   将语音置空
            message.setType(4);
            message.setSend_audio(null);
            message.setSend_img_message(bmobFile);
        }
        message.setUser_id(Constant.userId);
        message.setGender(Constant.usergender);
        message.setGpsAdd(new BmobGeoPoint(lng, lat));
        //开始上传
        if (type != 1) {//只要不是纯文字消息，都需要上传bmobfile
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    message.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                HD.TLOG("添加成功" + s);
                            } else {
                                HD.TLOG("添加失败" + e.getMessage());
                            }
                        }
                    });
                }

                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);
                    HD.TLOG("上传进度: " + value);
                }
            });
        } else {//如果是纯文字纸片
            message.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        HD.TLOG("添加成功" + s);
                    } else {
                        HD.TLOG("添加失败" + e.getMessage());
                    }
                }
            });
        }
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
//if (!img_url.isEmpty()) {
//
////todo 批量长传文件
////详细示例可查看BmobExample工程中BmobFileActivity类
//final String[] filePaths = new String[2];
//        filePaths[0] = img_url;
//        filePaths[1] = Constant.userIcon;
//        HD.LOG("2 " + " imgulr: " + img_url + "  userIcon: " + Constant.userIcon);
//        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
//
//@Override
//public void onSuccess(List<BmobFile> files, List<String> urls) {
//        //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
//        //2、urls-上传文件的完整url地址
//        if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
//        //do something
//        HD.TOS("上传成功" + " 图片: " + urls.get(0) + " 头像: " + urls.get(1));
//        Log.i("LHD", "成功：" + " 图片: " + urls.get(0) + " 头像: " + urls.get(1));
//        message.setSend_img_message(files.get(0));//添加发送的图片
//        message.setSend_audio(files.get(0));//添加发送的音频
//        //TODO 模拟添加语音
//        message.save(new SaveListener<String>() {
//@Override
//public void done(String s, BmobException e) {
//        HD.LOG("纸片上传成功！");
//        if (e == null) {
//        HD.TOS("添加数据成功，返回objectId为： " + s);
//        Log.i("LHD", "成功：" + s);
//        } else {
//        HD.TOS("添加数据失败" + e.getMessage());
//        Log.i("LHD", "失败： " + e.getMessage());
//        }
//        }
//        });
//        }
//        }
//
//@Override
//public void onError(int statuscode, String errormsg) {
//        HD.LOG("错误码" + statuscode + ",错误描述：" + errormsg);
//        HD.TOS("纸片上传失败" + errormsg);
//        HD.LOG("失败： " + errormsg);
//        }
//
//@Override
//public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
//        //1、curIndex--表示当前第几个文件正在上传
//        //2、curPercent--表示当前上传文件的进度值（百分比）
//        //3、total--表示总的上传文件数
//        //4、totalPercent--表示总的上传进度（百分比）
//        HD.TOS("正在上传： " + curIndex + "  进度： " + curPercent);
//        HD.LOG("正在上传： " + curIndex + "  进度： " + curPercent);
//        }
//        });
//        }