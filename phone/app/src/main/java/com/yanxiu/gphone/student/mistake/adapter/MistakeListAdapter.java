package com.yanxiu.gphone.student.mistake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.mistake.response.MistakeListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 9:25.
 * Function :
 */
public class MistakeListAdapter extends RecyclerView.Adapter<MistakeListAdapter.MistakeListViewHolder> {

    public interface onItemClickListener{
        void onItemClick(View view,MistakeListResponse.Data data,int position);
    }

    private LayoutInflater mInflater;
    private List<MistakeListResponse.Data> mData=new ArrayList<>();
    private onItemClickListener mItemClickListener;

    public MistakeListAdapter(Context context){
        this.mInflater=LayoutInflater.from(context);
    }

    public void setData(List<MistakeListResponse.Data> datas){
        if (datas==null){
            return;
        }
        this.mData.clear();
        this.mData.addAll(datas);
        this.notifyDataSetChanged();
    }

    public List<MistakeListResponse.Data> getData(){
        return mData;
    }

    public void addClickListener(onItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;
    }

    @Override
    public MistakeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.adapter_mistakelist,parent,false);
        return new MistakeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MistakeListViewHolder holder, int position) {
        MistakeListResponse.Data data=mData.get(position);
        holder.mMistakeNameView.setText(data.name);
        holder.mMistakeCountView.setText(String.valueOf(data.data.wrongNum));
    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size():0;
    }

    class MistakeListViewHolder extends RecyclerView.ViewHolder{

        TextView mMistakeNameView;
        TextView mMistakeCountView;

        MistakeListViewHolder(final View itemView) {
            super(itemView);
            mMistakeNameView= (TextView) itemView.findViewById(R.id.tv_mistake_name);
            mMistakeCountView= (TextView) itemView.findViewById(R.id.tv_mistake_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener!=null) {
                        mItemClickListener.onItemClick(itemView, mData.get(getLayoutPosition()), getLayoutPosition());
                    }
                }
            });
        }
    }
}
