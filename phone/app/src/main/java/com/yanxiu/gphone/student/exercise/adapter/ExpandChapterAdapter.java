package com.yanxiu.gphone.student.exercise.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.exercise.bean.ChapterBean;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class ExpandChapterAdapter extends RecyclerView.Adapter<ExpandChapterAdapter.ChapterViewHolder> {

    private List<ChapterBean> mData;
    private int mIndentation;  //缩进

    public ExpandChapterAdapter(List<ChapterBean> data) {
        initLevel(data,-1);
        mData = data;
//        mData = addChildren(data);
    }

    public void replaceData(List<ChapterBean> data){
        initLevel(data,-1);
        mData = data;
        notifyDataSetChanged();
    }
    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mIndentation == 0)
            mIndentation = ScreenUtils.dpToPxInt(parent.getContext(),50);
        return new ChapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter,parent,false));
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        holder.text.setText(mData.get(position).getName());
        if(mData.get(position).isHasChildren()){
            holder.ll_indicator.setVisibility(View.VISIBLE);
        }else {
            holder.ll_indicator.setVisibility(View.INVISIBLE);
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.leftMargin = mIndentation * mData.get(position).getLevel();
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void expand(int position){
        List<ChapterBean> dataToInsert = mData.get(position).getChildren();
        mData.addAll(position+1,dataToInsert);
        notifyItemRangeInserted(position + 1 ,dataToInsert.size());
//        notifyDataSetChanged();
    }

    private void collapse(int position){
        List<ChapterBean> dataToRemove = mData.get(position).getChildren();
        mData.removeAll(dataToRemove);
        notifyItemRangeRemoved(position+1,dataToRemove.size());
//        notifyDataSetChanged();
    }

    public List<ChapterBean> addChildren(List<ChapterBean> data){
        List<ChapterBean> result = new ArrayList<>();
        for(ChapterBean bean : data){
            bean.setExpanded(true);
            result.add(bean);
            if(bean.getChildren() != null && bean.getChildren().size() > 0)
               result.addAll(addChildren(bean.getChildren()));
        }
        return result;
    }

    private void initLevel(List<ChapterBean> data,int parentLevel){
        for(ChapterBean bean : data){
            bean.setLevel(parentLevel + 1);
            if(bean.getChildren() != null && bean.getChildren().size() > 0){
                initLevel(bean.getChildren(),bean.getLevel());
            }
        }
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView indicator;
        public TextView text;
        public View ll_indicator;

        public ChapterViewHolder(final View itemView) {
            super(itemView);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            text = (TextView) itemView.findViewById(R.id.text);
            ll_indicator = itemView.findViewById(R.id.ll_indicator);

            ll_indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mData.get(getLayoutPosition()).isExpanded()){
                        mData.get(getLayoutPosition()).setExpanded(false);
                        collapse(getLayoutPosition());
                        indicator.setImageResource(R.drawable.collapse_normal);
                    }else {
                        mData.get(getLayoutPosition()).setExpanded(true);
                        expand(getLayoutPosition());
                        indicator.setImageResource(R.drawable.expand_normal);
                    }
                }
            });
        }
    }
}
