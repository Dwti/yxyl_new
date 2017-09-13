package com.yanxiu.gphone.student.exercise.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.exercise.bean.EditionChildBean;

import java.util.List;

/**
 * Created by sp on 17-8-1.
 */

public class PopListAdapter extends BaseAdapter {
    private List<EditionChildBean> mData;

    public PopListAdapter(List<EditionChildBean> data) {
        mData = data;
    }

    public void replaceData(List<EditionChildBean> data){
        mData = data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_pop_list_item,parent,false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        textView.setText(mData.get(position).getName());
        textView.setSelected("1".equals(mData.get(position).getSelected()));
        if("1".equals(mData.get(position).getSelected())){
            icon.setVisibility(View.VISIBLE);
        }else {
            icon.setVisibility(View.GONE);
        }
        return convertView;
    }
}
