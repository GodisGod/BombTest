package com.example.bombtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bombtest.R;

import java.util.ArrayList;

/**
 * Created by ${鸿达} on 2016/8/23.
 */
public class PhotoAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> img_Paths;

    public PhotoAdapter(Context mContext, ArrayList<String> img_Paths) {
        this.mContext = mContext;
        this.img_Paths = img_Paths;
    }

    @Override
    public int getCount() {
        return img_Paths.size();
    }

    @Override
    public Object getItem(int position) {
        return img_Paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.photoitem, parent, false);
            vh = new ViewHolder();
            vh.img = (ImageView) convertView.findViewById(R.id.photoitem_img);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        String path = img_Paths.get(position);
        //all:缓存源资源和转换后的资源
//        none:不作任何磁盘缓存
//        source:缓存源资源         次快
//        result：缓存转换后的资源  最快
        Glide.with(mContext).load(path)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()  //转换宽高比
                .into(vh.img);
        return convertView;
    }

    class ViewHolder {
        ImageView img;
    }
}
