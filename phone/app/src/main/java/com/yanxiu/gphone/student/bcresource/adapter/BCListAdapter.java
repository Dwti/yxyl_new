package com.yanxiu.gphone.student.bcresource.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bcresource.bean.BCBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class BCListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BCBean> mData;
    private OnItemClickListener mListener;

    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    public BCListAdapter(List<BCBean> mData, OnItemClickListener listener) {
        this.mData = mData;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_PARENT){
            return new ParentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bc_parent,parent,false));
        }else if(viewType == TYPE_CHILD){
            return new ChildHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bc_child,parent,false));
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ParentHolder){
            ((ParentHolder)holder).name.setText(mData.get(position).getName());
        }else if(holder instanceof ChildHolder){
            ((ChildHolder)holder).name.setText(mData.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).getType() == TYPE_PARENT){
            return TYPE_PARENT;
        }else if(mData.get(position).getType() == TYPE_CHILD){
            return TYPE_CHILD;
        }else {
            return TYPE_PARENT;
        }
    }

    public void replaceData(List<BCBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    class ParentHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public ParentHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    class ChildHolder extends RecyclerView.ViewHolder{

        private TextView name;

        public ChildHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);

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

    public interface OnItemClickListener{
        void onClick(BCBean bean,int position);
    }
}
