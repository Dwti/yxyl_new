package com.yanxiu.gphone.student.exercise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.exercise.bean.SubjectBean;

import java.util.List;

/**
 * Created by sp on 17-8-28.
 */

public class SelectSubjectAdapter extends BaseAdapter {

    private List<SubjectBean> subjects;

    public SelectSubjectAdapter(List<SubjectBean> subjects) {
        this.subjects = subjects;
    }

    public void replaceData(List<SubjectBean> data){
        if(data != null){
            subjects = data;
            notifyDataSetChanged();
        }
    }

    public void clearData(){
        if(subjects !=null && subjects.size() > 0){
            subjects.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_subject,parent,false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView edition = (TextView) convertView.findViewById(R.id.tv_edition);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        name.setText(subjects.get(position).getName());
        setIcon(subjects.get(position).getId(),icon);
        if(subjects.get(position).getData() != null){
            edition.setText(subjects.get(position).getData().getEditionName());
            edition.setTextColor(parent.getContext().getResources().getColor(R.color.color_89e00d));
        }else {
            edition.setText("未选择");
            edition.setTextColor(parent.getContext().getResources().getColor(R.color.color_999999));
        }
        return convertView;
    }

    private void setIcon(String id, ImageView imageView) {
        switch (id){
            case "1102":
                imageView.setImageResource(R.drawable.yuwen1);
                break;
            case "1103":
                imageView.setImageResource(R.drawable.shuxue1);
                break;
            case "1104":
                imageView.setImageResource(R.drawable.yingyu1);
                break;
            case "1105":
                imageView.setImageResource(R.drawable.wuli1);
                break;
            case "1106":
                imageView.setImageResource(R.drawable.huaxue1);
                break;
            case "1107":
                imageView.setImageResource(R.drawable.shengwu1);
                break;
            case "1108":
                imageView.setImageResource(R.drawable.dili1);
                break;
            case "1109":
                imageView.setImageResource(R.drawable.zhengzhi1);
                break;
            case "1110":
                imageView.setImageResource(R.drawable.lishi1);
                break;
            default:
                break;
        }
    }
}
