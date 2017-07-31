package com.yanxiu.gphone.student.exercise.adapter;

import com.yanxiu.gphone.student.exercise.bean.KnowledgePointBean;

import java.util.List;

/**
 * Created by sp on 17-7-31.
 */

public class KnowledgePointAdapter extends ExerciseExpandableAdapter<KnowledgePointBean> {
    public KnowledgePointAdapter(List<KnowledgePointBean> data) {
        super(data);
    }

    @Override
    public void onBindViewHolder(ExpandableViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.text.setText(mData.get(position).getName());
    }
}
