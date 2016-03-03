package com.jikexueyuan.secret.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jikexueyuan.secret.bean.Comment;
import com.jikexueyuan.secret.secret.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13058 on 2016/3/3.
 */
public class MessageActivityAdapter extends BaseAdapter{
    private Context context;
    private List<Comment> data = new ArrayList<Comment>();
    public void addAll(List<Comment> comments){
        data.addAll(comments);
        notifyDataSetChanged();
    }
    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }
    public MessageActivityAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Comment getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_comment_list_cell,null);
            convertView.setTag(convertView.findViewById(R.id.tvCommentCellLable));
        }
        TextView commentCell = (TextView)convertView.getTag();
        Comment comment = getItem(position);
        commentCell.setText(comment.getContent());
        return convertView;
    }

    public Context getContext(){
        return context;
    }
}
