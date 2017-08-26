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
 * Created by sp on 17-7-26.
 */

public class SubjectsAdapter extends BaseAdapter {
    private List<SubjectBean> mSubjects;

    public SubjectsAdapter(List<SubjectBean> subjects) {
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
            case "1102":
                imageView.setImageResource(R.drawable.yuwen);
                break;
            case "1103":
                imageView.setImageResource(R.drawable.shuxue);
                break;
            case "1104":
                imageView.setImageResource(R.drawable.yingyu);
                break;
            case "1105":
                imageView.setImageResource(R.drawable.wuli);
                break;
            case "1106":
                imageView.setImageResource(R.drawable.huaxue);
                break;
            case "1107":
                imageView.setImageResource(R.drawable.shengwu);
                break;
            case "1108":
                imageView.setImageResource(R.drawable.dili);
                break;
            case "1109":
                imageView.setImageResource(R.drawable.zhengzhi);
                break;
            case "1110":
                imageView.setImageResource(R.drawable.lishi);
                break;
            default:
                break;
        }
    }
    public void replaceData(List<SubjectBean> data){
        mSubjects = data;
        notifyDataSetChanged();
    }

    public void clearData(){
        mSubjects.clear();
        notifyDataSetChanged();
    }
}
