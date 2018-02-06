package com.yanxiu.gphone.student.learning.adapter;

import android.support.v7.widget.RecyclerView;

import com.yanxiu.gphone.student.exercise.adapter.ExerciseExpandableAdapter;
import com.yanxiu.gphone.student.exercise.bean.KnowledgePointBean;

import java.util.List;

/**
 * Created by lufengqing on 2018/1/16.
 */

public class SpecialAdapter extends ExerciseExpandableAdapter<KnowledgePointBean> {
    public SpecialAdapter(List<KnowledgePointBean> data) {
        super(data);
    }

    @Override
    public void replaceData(List<KnowledgePointBean> data) {
        super.replaceData(data);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((ExpandableViewHolder)holder).text.setText(mData.get(position).getName());
    }
}
