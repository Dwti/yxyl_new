package com.yanxiu.gphone.student.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/12 14:25.
 * Function :
 */
public class BaseRetractRecycleViewHolder extends RecyclerView.ViewHolder {
    private LinearLayout mRetractLayout;

    public BaseRetractRecycleViewHolder(View itemView) {
        super(itemView);
        mRetractLayout= (LinearLayout) itemView.findViewById(R.id.retract_view);
    }
}
