package com.yanxiu.gphone.student.mistake.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
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

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MistakeListResponse.Data> mData=new ArrayList<>();
    private onItemClickListener mItemClickListener;

    public MistakeListAdapter(Context context){
        this.mContext=context;
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
        setSubjectImg(data.id,holder.mMistakeIconView);
    }

    private void setSubjectImg(int subjectId,ImageView imageView){
        switch (subjectId){
            case Constants.SubjectId.CHINESE:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.chinese));
                break;
            case Constants.SubjectId.MATH:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.math));
                break;
            case Constants.SubjectId.ENGLISH:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.english));
                break;
            case Constants.SubjectId.BIOLOGY:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.biology));
                break;
            case Constants.SubjectId.CHEMICAL:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.chemistry));
                break;
            case Constants.SubjectId.GEOGRAPHIC:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.geographic));
                break;
            case Constants.SubjectId.HISTORY:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.history));
                break;
            case Constants.SubjectId.PHYSICAL:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.physical));
                break;
            case Constants.SubjectId.POLITICAL:
                imageView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.political));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size():0;
    }

    class MistakeListViewHolder extends RecyclerView.ViewHolder{

        ImageView mMistakeIconView;
        TextView mMistakeNameView;
        TextView mMistakeCountView;

        MistakeListViewHolder(final View itemView) {
            super(itemView);
            mMistakeIconView= (ImageView) itemView.findViewById(R.id.iv_mistake_icon);
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
