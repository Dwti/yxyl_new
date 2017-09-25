package com.yanxiu.gphone.student.exercise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.exercise.bean.ExerciseBean;
import com.yanxiu.gphone.student.util.TimeUtils;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by sp on 17-5-22.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static int TYPE_NORMAL = 0;
    private static int TYPE_FOOTER = 1;
    private List<ExerciseBean> mData;
    private Context mContext;
    private boolean mIsLoadingMore = false;

    private ExerciseItemClickListener mItemListener;

    public ExerciseAdapter(List<ExerciseBean> data, ExerciseItemClickListener mItemListener) {
        this.mData = data;
        this.mItemListener = mItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_NORMAL){
            return new ExerciseViewHolder(inflater.inflate(R.layout.item_exercise_history,parent,false));
        }else if(viewType == TYPE_FOOTER){
            return new FooterViewHolder(inflater.inflate(R.layout.footer_tips,parent,false));
        }else {
            return new ExerciseViewHolder(inflater.inflate(R.layout.item_exercise_history,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExerciseBean bean = null;
        if(position < mData.size()){
            bean = mData.get(position);
        }
        if(holder instanceof ExerciseViewHolder){
            ExerciseViewHolder viewHolder = (ExerciseViewHolder)holder;
            String correctRate = NumberFormat.getPercentInstance().format(bean.getCorrectNum() / (float)bean.getQuestionNum() + 0.005);
            viewHolder.name.setText(bean.getName());
            if(bean.getStatus() == 2){
                String buildTime = TimeUtils.getExerciseDate(bean.getBuildTime());
                viewHolder.endTime.setText(String.format(viewHolder.endTime.getResources().getString(R.string.exercise_end_time),buildTime));
                viewHolder.correctRate.setText(String.format(viewHolder.correctRate.getResources().getString(R.string.exercise_correct_rate),correctRate));
            }else {
                viewHolder.endTime.setText(viewHolder.endTime.getResources().getString(R.string.exercise_wait_to_finish));
                viewHolder.correctRate.setText("");
            }

        }else if(holder instanceof FooterViewHolder){
            FooterViewHolder viewHolder = (FooterViewHolder) holder;
            if(mIsLoadingMore){
                viewHolder.mItemVIew.setVisibility(View.VISIBLE);
                viewHolder.mProgressBar.setVisibility(View.VISIBLE);
                viewHolder.mLoadingText.setText(((FooterViewHolder) holder).mLoadingText.getContext().getResources().getString(R.string.text_loading));
            }else {
                viewHolder.mItemVIew.setVisibility(View.GONE);
                viewHolder.mProgressBar.setVisibility(View.GONE);
                viewHolder.mLoadingText.setText(((FooterViewHolder) holder).mLoadingText.getContext().getResources().getString(R.string.text_click_to_load_more));
            }
        }
    }

    @Override
    public int getItemCount() {
//        return mData.size() > 0? mData.size() +1 :0;
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_NORMAL;
//        if(position == getItemCount() -1){
//            return TYPE_FOOTER;
//        }else {
//            return TYPE_NORMAL;
//        }
    }

    public ExerciseBean getItem(int position){
        if(position < mData.size()){
            return  mData.get(position);
        }else {
            return null;
        }
    }
    public void replaceData(List<ExerciseBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void clearData(){
        if(mData != null && mData.size() > 0){
            mData.clear();
            notifyDataSetChanged();
        }
    }

    public void addFooterView(){
        mIsLoadingMore = true;
        notifyDataSetChanged();
    }

    public void removeFooterView(){
        mIsLoadingMore = false;
        notifyDataSetChanged();
    }

    public boolean isLoadingMore(){
        return mIsLoadingMore;
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView name,endTime,correctRate;
        public ExerciseViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            endTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            correctRate = (TextView) itemView.findViewById(R.id.tv_correct_rate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemListener != null){
                        mItemListener.onHomeworkClick(mData.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder{

        ProgressBar mProgressBar;
        TextView mLoadingText;
        View mItemVIew;
        public FooterViewHolder(View itemView) {
            super(itemView);
            mItemVIew = itemView;
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            mLoadingText = (TextView) itemView.findViewById(R.id.loadingText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemListener != null){
                        mItemListener.onLoadMoreClick();
                    }
                }
            });
        }
    }

    public interface ExerciseItemClickListener {
        void onHomeworkClick(ExerciseBean bean);

        void onLoadMoreClick();
    }
}
