package com.jikexueyuan.secret.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jikexueyuan.secret.bean.Message;
import com.jikexueyuan.secret.secret.R;

import java.util.ArrayList;
import java.util.List;

/**
 * activity adapter 是将这个的layout内容放置到调用它的那个地方
 * Created by 13058 on 2016/3/3.
 */
public class TimeLineActivityAdapter extends BaseAdapter{
    private Context context;
    private List<Message> data = new ArrayList<Message>();
    public void addAll(List<Message> messages){
        data.addAll(messages);
        notifyDataSetChanged();
    }

    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }
    public TimeLineActivityAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Message getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_timeline_list_cell,null);
            convertView.setTag(convertView.findViewById(R.id.tvCellLable));
        }
        TextView tv = (TextView)convertView.getTag();
        Message message = getItem(position);
        tv.setText(message.getMsg());
        return convertView;
    }

    public Context getContext() {
        return context;
    }
}
