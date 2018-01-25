package com.yanxiu.gphone.student.learning.adapter;

import android.support.v7.widget.RecyclerView;

import com.yanxiu.gphone.student.exercise.adapter.ExerciseExpandableAdapter;
import com.yanxiu.gphone.student.exercise.bean.ChapterBean;

import java.util.List;

/**
 * Created by lufengqing on 2018/1/16.
 */

public class SyncAdapter extends ExerciseExpandableAdapter<ChapterBean> {
    public SyncAdapter(List<ChapterBean> data) {
        super(data);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((ExpandableViewHolder)holder).text.setText(mData.get(position).getName());
    }
}

