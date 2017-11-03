package com.yanxiu.gphone.student.bcresource.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bcresource.bean.TopicBean;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class TopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TopicBean> mData;
    private OnItemClickListener mListener;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    public TopicListAdapter(List<TopicBean> mData, OnItemClickListener listener) {
        this.mData = mData;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_NORMAL){
            return new TopicHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic,parent,false));
        }else {
            return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_tips,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TopicHolder){
            ((TopicHolder)holder).name.setText(mData.get(position).getName());
            ((TopicHolder)holder).popValue.setText(String.format(holder.itemView.getContext().getString(R.string.topic_pop_value),mData.get(position).getViewnum()));
            if(mData.get(position).getPaperStatus() != null){
                if(mData.get(position).getPaperStatus().getStatus() == 2){
                    ((TopicHolder)holder).status.setTextColor(Color.parseColor("#666666"));
                    ((TopicHolder)holder).status.setText(String.format(holder.itemView.getContext().getString(R.string.topic_correct_rate),NumberFormat.getPercentInstance().format(mData.get(position).getPaperStatus().getScoreRate())));
                }
            }else {
                ((TopicHolder)holder).status.setTextColor(Color.parseColor("#89e00d"));
                if(SaveAnswerDBHelper.isTopicPaperAnswered(mData.get(position).getId())){
                    ((TopicHolder)holder).status.setText("作答中...");
                }else {
                    ((TopicHolder)holder).status.setText("");
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(TextUtils.isEmpty(mData.get(position).getId())){
            return TYPE_FOOTER;
        }else {
            return TYPE_NORMAL;
        }
    }

    public boolean isFooterExist(){
        if(getItemViewType(getItemCount() - 1) == TYPE_FOOTER){
            return true;
        }else {
            return false;
        }
    }

    public void replaceData(List<TopicBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void clearData(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void addFooterView(){
        mData.add(new TopicBean());
        notifyItemInserted(mData.size() - 1);
    }

    public void remove(int position){
        if(mData != null && position < mData.size()){
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setPaperStatus(int position, TopicBean.PaperStatusBean paperStatusBean){
        if(mData != null && position < mData.size()){
            mData.get(position).setPaperStatus(paperStatusBean);
        }
    }

    class TopicHolder extends RecyclerView.ViewHolder{

        private TextView name, popValue, status;

        public TopicHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            popValue = (TextView) itemView.findViewById(R.id.tv_pop_value);
            status = (TextView) itemView.findViewById(R.id.tv_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onClick(mData.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder{

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }


    public interface OnItemClickListener{
        void onClick(TopicBean bean, int position);
    }
}
