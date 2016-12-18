package com.example.bombtest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.bombtest.bean.PaperMessage;

import java.util.List;

/**
 * Created by HONGDA on 2016/12/18.
 */
public class ScripAdapter extends BaseAdapter {
    private Context context;
    private List<PaperMessage> paperMessages;

    public ScripAdapter(Context context, List<PaperMessage> paperMessages) {
        this.context = context;
        this.paperMessages = paperMessages;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
