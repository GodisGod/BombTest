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
import com.example.bombtest.bean.PaperMessage;
import com.example.bombtest.bean.User;
import com.example.bombtest.util.HD;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by HONGDA on 2016/12/18.
 */
public class ScripAdapter extends BaseAdapter {
    private Context context;
    private List<PaperMessage> paperMessages;
    private LayoutInflater inflater;
    private String[] gender_types_f = {"清纯萝莉", "霸气御姐", "知性淑女"};
    private String[] gender_types_m = {"小小正太", "魅力少年", "成熟暖男"};
    private Random random;
    private ViewHolder holder = null;

    public ScripAdapter(Context mcontext, List<PaperMessage> paperMessages) {
        this.context = mcontext;
        this.paperMessages = paperMessages;
        inflater = LayoutInflater.from(context);
        random = new Random();
    }

    @Override
    public int getCount() {
        return paperMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return paperMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        PaperMessage paperMessage = paperMessages.get(i);
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
        BmobQuery<User> query = new BmobQuery<User>("User");
        query.addWhereEqualTo("user_id", paperMessage.getUser_id());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    HD.TLOG("findObjects: " + list.get(0).getUser_name() + "  " + list.size());
                    Glide.with(context).load(list.get(0).getUser_icon().getFileUrl())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.mipmap.ic_launcher)
                            .centerCrop()  //转换宽高比
                            .into(holder.item_scrip_icon);
                } else {
                    HD.LOG("失敗：" + e.getMessage() + ", " + e.getErrorCode());
                }
            }
        });

        Glide.with(context).load(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()  //转换宽高比
                .into(holder.item_scrip_icon);
        holder.item_scrip_time.setText(paperMessage.getCreatedAt());
        if (paperMessage.getType() == 1) {
            holder.item_scrip_content.setText(paperMessage.getSend_text_message());
        } else if (paperMessage.getType() == 2) {
            holder.item_scrip_content.setText("这是一条奇妙的语音消息");
        } else if (paperMessage.getType() == 3 || paperMessage.getType() == 4) {
            holder.item_scrip_content.setText("包含了一张神奇图片的消息");
        }

        if (paperMessage.getGender().equals("f")) {
            holder.item_scrip_gender_type.setText(gender_types_f[random.nextInt(2)]);
        } else if (paperMessage.getGender().equals("m")) {
            holder.item_scrip_gender_type.setText(gender_types_m[random.nextInt(2)]);
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
