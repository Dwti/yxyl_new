package com.yanxiu.gphone.student.login.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.login.response.ChooseSchoolResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/25 11:37.
 * Function :
 */
public class ChooseSchoolAdapter extends RecyclerView.Adapter<ChooseSchoolAdapter.ChooseSchoolViewHolder> {

    private Context mContext;
    private List<ChooseSchoolResponse.School> mDatas=new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, ChooseSchoolResponse.School school,int position);
    }

    public ChooseSchoolAdapter(Context context){
        this.mContext=context;
    }

    public void setDatas(List<ChooseSchoolResponse.School> datas){
        this.mDatas.clear();
        if (datas!=null) {
            this.mDatas.addAll(datas);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public ChooseSchoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_chooseschool_item,parent,false);
        return new ChooseSchoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseSchoolViewHolder holder, int position) {
        holder.mSchoolNameView.setText(mDatas.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }

    class ChooseSchoolViewHolder extends RecyclerView.ViewHolder{
        TextView mSchoolNameView;
        ChooseSchoolViewHolder(final View itemView) {
            super(itemView);
            mSchoolNameView= (TextView) itemView.findViewById(R.id.tv_school_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener!=null){
                        int position=getLayoutPosition();
                        mOnItemClickListener.onItemClick(itemView,mDatas.get(position),position);
                    }
                }
            });
        }
    }
}
