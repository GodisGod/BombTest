package com.example.bombtest.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.bombtest.R;
import com.example.bombtest.adapter.PhotoAdapter;
import com.example.bombtest.util.HD;

import java.util.ArrayList;

public class PhotosWall extends AppCompatActivity {

    private ArrayList<String> img_paths;

    private GridView gridView;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_wall);
        gridView = (GridView) findViewById(R.id.photos_grid);
        img_paths = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        //遍历相册
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            //将图片路径添加到集合
            img_paths.add(path);
        }
        cursor.close();
        initlist();
    }

    private void initlist() {
        adapter = new PhotoAdapter(this, img_paths);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HD.LOG("i: " + i + " long: " + l + "  imgurl: " + img_paths.get(i));
                Intent intent = new Intent(PhotosWall.this, MainActivity.class);
                intent.putExtra("imgurl", img_paths.get(i));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
