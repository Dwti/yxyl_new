package com.yanxiu.gphone.student.bcresource.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.bcresource.bean.TopicBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicHolder> {

    private List<TopicBean> mData;
    private OnItemClickListener listener;

    public TopicListAdapter(List<TopicBean> mData, OnItemClickListener listener) {
        this.mData = mData;
        this.listener = listener;
    }

    @Override
    public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TopicHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void replaceData(List<TopicBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addFooterView(){
        mData.add(new TopicBean());
        notifyItemInserted(mData.size() - 1);
    }

    class TopicHolder extends RecyclerView.ViewHolder{

        public TopicHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener{
        void onClick(TopicBean bean, int position);
    }
}
