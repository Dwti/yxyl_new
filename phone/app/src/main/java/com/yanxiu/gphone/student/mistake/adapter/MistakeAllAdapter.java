package com.yanxiu.gphone.student.mistake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/17 10:22.
 * Function :
 */
public class MistakeAllAdapter extends RecyclerView.Adapter<MistakeAllAdapter.MistakeCompleteViewHolder> {

    public interface onItemClickListener {
        void onItemClick(View view, List<PaperTestBean> paperTestBeanList, int position);
    }

    private LayoutInflater mInflater;
    private List<PaperTestBean> mData = new ArrayList<>();
    private onItemClickListener mItemClickListener;

    public MistakeAllAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<PaperTestBean> data) {
        if (data == null) {
            return;
        }
        mData.clear();
        mData.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addData(List<PaperTestBean> data){
        if (data == null) {
            return;
        }
        mData.addAll(data);
        this.notifyDataSetChanged();
    }

    public String getLastItemWqid(){
        if (mData.size()>0){
            return String.valueOf(mData.get(mData.size()-1).getWqid());
        }
        return "0";
    }

    public void deleteItem(int position,int id){
        if (position<mData.size()){
            if (mData.get(position).getId().equals(String.valueOf(id))){
                this.mData.remove(position);
                this.notifyDataSetChanged();
            }
        }
    }

    public void addItemClickListener(onItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public MistakeCompleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_mistakeall, parent, false);
        return new MistakeCompleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MistakeCompleteViewHolder holder, int position) {
        holder.mNameView.setText(mData.get(position).getQuestions().getStem());
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    class MistakeCompleteViewHolder extends RecyclerView.ViewHolder {

        TextView mNameView;

        MistakeCompleteViewHolder(final View itemView) {
            super(itemView);
            mNameView= (TextView) itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(itemView, mData, getLayoutPosition());
                    }
                }
            });
        }
    }
}
