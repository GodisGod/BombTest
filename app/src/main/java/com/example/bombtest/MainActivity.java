package com.example.bombtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "9c025fdf83b8cb9dcded051b04f741dc");

        send_content = (EditText) findViewById(R.id.ed_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_discover = (Button) findViewById(R.id.btn_discover);
        tv_discover = (TextView) findViewById(R.id.tv_discover);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = send_content.getText().toString();
                if (!content.isEmpty()) {
                    PaperMessage message = new PaperMessage();
                    message.setText_message(content);
                    message.setGpsAdd(new BmobGeoPoint(116.39727786183357, 39.913768382429105));
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
                BmobQuery query = new BmobQuery("PaperMessage");
//                query.addWhereEqualTo("gpsAdd", new BmobGeoPoint(116.398, 39.914));
                query.addWhereWithinKilometers("gpsAdd", new BmobGeoPoint(116.398, 39.914),100);
                query.setLimit(10);
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
}
