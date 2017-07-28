package com.yanxiu.gphone.student.exercise.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private List<ChapterBean> mSourceData;
    private List<ChapterBean> mBindData;
    private int mIndentation;  //缩进

    public ExpandChapterAdapter(List<ChapterBean> data) {
        initLevel(data,-1);
        this.mSourceData = data;
        mBindData = getBindData(mSourceData,0);
    }

    public void replaceData(List<ChapterBean> data){
        initLevel(data,-1);
        this.mSourceData = data;
        mBindData = getBindData(mSourceData,0);
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
        holder.text.setText(mSourceData.get(position).getName());
        if(mSourceData.get(position).isHasChildren()){
            holder.ll_indicator.setVisibility(View.VISIBLE);
        }else {
            holder.ll_indicator.setVisibility(View.INVISIBLE);
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.leftMargin = mIndentation * mSourceData.get(position).getLevel();
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mBindData.size();
    }

    private void expand(int position){
        List<ChapterBean> dataToInsert = mSourceData.get(position).getChildren();
        mSourceData.addAll(position+1,dataToInsert);
//        notifyItemRangeInserted(position,dataToInsert.size());
        notifyDataSetChanged();
    }

    private void collapse(int position){
        List<ChapterBean> dataToRemove = mSourceData.get(position).getChildren();
        mSourceData.removeAll(dataToRemove);
//        notifyItemRangeRemoved(position+1,dataToRemove.size());
        notifyDataSetChanged();
    }

    private List<ChapterBean> getBindData(List<ChapterBean> data,int defaultExpandLevel){
        List<ChapterBean> result = new ArrayList<>();
        for(ChapterBean bean : data){
            result.add(bean);
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

        public ChapterViewHolder(View itemView) {
            super(itemView);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            text = (TextView) itemView.findViewById(R.id.text);
            ll_indicator = itemView.findViewById(R.id.ll_indicator);

            ll_indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mSourceData.get(getLayoutPosition()).isExpanded()){
                        collapse(getLayoutPosition());
                    }else {
                        expand(getLayoutPosition());
                    }
                }
            });
        }
    }
}
