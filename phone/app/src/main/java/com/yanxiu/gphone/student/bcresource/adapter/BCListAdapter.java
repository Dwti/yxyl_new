package com.yanxiu.gphone.student.bcresource.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.bcresource.bean.BCBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class BCListAdapter extends RecyclerView.Adapter<BCListAdapter.BCHolder> {

    private List<BCBean> mData;
    private OnItemClickListener listener;

    public BCListAdapter(List<BCBean> mData, OnItemClickListener listener) {
        this.mData = mData;
        this.listener = listener;
    }

    @Override
    public BCHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BCHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void replaceData(List<BCBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addFooterView(){
        mData.add(new BCBean());
        notifyItemInserted(mData.size() - 1);
    }

    class BCHolder extends RecyclerView.ViewHolder{

        public BCHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener{
        void onClick(BCBean bean,int position);
    }
}
