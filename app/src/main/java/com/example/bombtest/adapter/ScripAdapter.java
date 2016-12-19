package com.example.bombtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bombtest.R;
import com.example.bombtest.bean.PaperMessageUser;
import com.example.bombtest.util.GlideCircleTransform;
import com.example.bombtest.util.HD;

import java.util.List;
import java.util.Random;

/**
 * Created by HONGDA on 2016/12/18.
 */
public class ScripAdapter extends BaseAdapter {
    private Context context;
    private List<PaperMessageUser> paperMessageUsers;
    private LayoutInflater inflater;
    private String[] gender_types_f = {"清纯萝莉", "霸气御姐", "知性淑女", "神秘女性"};
    private String[] gender_types_m = {"小小正太", "魅力少年", "成熟暖男", "神秘男性"};
    private Random random;
    private ViewHolder holder = null;
//    private GlideCircleTransform transform;
    public ScripAdapter(Context mcontext, List<PaperMessageUser> paperMessageUsers) {
        this.context = mcontext;
        this.paperMessageUsers = paperMessageUsers;
        inflater = LayoutInflater.from(context);
        random = new Random();
//        transform = new GlideCircleTransform(context);
    }

    @Override
    public int getCount() {
        return paperMessageUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return paperMessageUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        PaperMessageUser paperMessageUser = paperMessageUsers.get(i);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_scrips, null);
            holder = new ViewHolder();
            holder.item_scrip_icon = (ImageView) convertView.findViewById(R.id.item_scrip_icon);
            holder.item_scrip_gender_type = (TextView) convertView.findViewById(R.id.item_scrip_gender_type);
            holder.item_scrip_content = (TextView) convertView.findViewById(R.id.item_scrip_text);
            holder.item_scrip_time = (TextView) convertView.findViewById(R.id.item_scrip_create_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //todo 展示用户的头像
        HD.LOG("展示用户的头像");
        Glide.with(context).load(paperMessageUser.getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new GlideCircleTransform(context))
                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()  //转换宽高比  去掉这一行才能使用transform
                .into(holder.item_scrip_icon);

        holder.item_scrip_time.setText(paperMessageUser.getCreateTime());
        if (paperMessageUser.getType() == 1) {
            holder.item_scrip_content.setText(paperMessageUser.getSend_text_message());
        } else if (paperMessageUser.getType() == 2) {
            holder.item_scrip_content.setText("这是一条奇妙的语音消息");
        } else if (paperMessageUser.getType() == 3 || paperMessageUser.getType() == 4) {
            holder.item_scrip_content.setText("包含了一张神奇图片的消息");
        }

        if (paperMessageUser.getGender().equals("f")) {
            holder.item_scrip_gender_type.setText(gender_types_f[random.nextInt(3)]);
        } else if (paperMessageUser.getGender().equals("m")) {
            holder.item_scrip_gender_type.setText(gender_types_m[random.nextInt(3)]);
        } else {
            holder.item_scrip_gender_type.setText("神秘人物");
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView item_scrip_icon;
        TextView item_scrip_gender_type;
        TextView item_scrip_content;
        TextView item_scrip_time;
    }
}
