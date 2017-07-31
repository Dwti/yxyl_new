package com.yanxiu.gphone.student.exercise.adapter;

import android.support.v7.widget.RecyclerView;

import com.yanxiu.gphone.student.exercise.bean.ChapterBean;

import java.util.List;

/**
 * Created by sp on 17-7-31.
 */

public class ChapterAdapter extends ExerciseExpandableAdapter<ChapterBean> {
    public ChapterAdapter(List<ChapterBean> data) {
        super(data);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((ExpandableViewHolder)holder).text.setText(mData.get(position).getName());
    }
}
