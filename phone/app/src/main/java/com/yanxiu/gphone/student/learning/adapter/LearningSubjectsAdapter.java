package com.yanxiu.gphone.student.learning.adapter;

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
 * Created by lufengqing on 2018/1/16.
 */

public class LearningSubjectsAdapter extends BaseAdapter {
    private List<SubjectBean> mSubjects;

    public LearningSubjectsAdapter(List<SubjectBean> subjects) {
        this.mSubjects = subjects;
    }

    @Override
    public int getCount() {
        return mSubjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_subject,parent,false);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView subjectName = (TextView) convertView.findViewById(R.id.tv_subject_name);
        TextView editionName = (TextView) convertView.findViewById(R.id.tv_edition_name);
        setIcon(icon,mSubjects.get(position).getId());
        subjectName.setText(mSubjects.get(position).getName());
        if(mSubjects.get(position).getData() != null){
            editionName.setText(mSubjects.get(position).getData().getEditionName());
        }else {
            editionName.setText("");
        }
        return convertView;
    }

    private void setIcon(ImageView imageView,String subjectId){
        switch (subjectId){
            case "1103":
                imageView.setImageResource(R.drawable.learning_maths);
                break;
            case "1104":
                imageView.setImageResource(R.drawable.learning_english);
                break;
            default:
                break;
        }
    }
    public void replaceData(List<SubjectBean> data){
        mSubjects = data;
        notifyDataSetChanged();
    }

    public void addData(SubjectBean bean){
        mSubjects.add(bean);
        notifyDataSetChanged();
    }

    public void addData(List<SubjectBean> data){
        mSubjects.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData(){
        mSubjects.clear();
        notifyDataSetChanged();
    }
}

