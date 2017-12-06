package com.yanxiu.gphone.student.mistakeredo.adapter;

import android.support.v7.widget.RecyclerView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.exercise.adapter.ExerciseExpandableAdapter;
import com.yanxiu.gphone.student.mistakeredo.bean.WrongQPointBean;

import java.util.List;

/**
 * Created by sp on 17-11-21.
 */

public class WrongQPointAdapter extends ExerciseExpandableAdapter<WrongQPointBean> {

    public WrongQPointAdapter(List<WrongQPointBean> data) {
        super(data);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String title = String.format(holder.itemView.getContext().getString(R.string.point_title),mData.get(position).getName(),mData.get(position).getQuestion_num());
//        ((ExpandableViewHolder)holder).text.setText(mData.get(position).getName());
        ((ExpandableViewHolder)holder).text.setText(title);
    }
}
