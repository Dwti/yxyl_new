package com.yanxiu.gphone.student.exercise.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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
    private OnItemClickListener mOnItemClickListener;

    public ExpandChapterAdapter(List<ChapterBean> data) {
        initLevel(data, -1);
        mData = data;
    }

    public void replaceData(List<ChapterBean> data) {
        initLevel(data, -1);
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mIndentation == 0)
            mIndentation = ScreenUtils.dpToPxInt(parent.getContext(), 50);
        return new ChapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        holder.text.setText(mData.get(position).getName());
        if (mData.get(position).isExpanded()) {
            holder.indicator.setImageResource(R.drawable.expand_normal);
        } else {
            holder.indicator.setImageResource(R.drawable.collapse_normal);
        }
        if (mData.get(position).hasChildren()) {
            holder.ll_indicator.setVisibility(View.VISIBLE);
        } else {
            holder.ll_indicator.setVisibility(View.INVISIBLE);
        }
        setBackgroundByLevel(holder.itemView,mData.get(position).getLevel());
        setTextSizeByLevel(holder.text,mData.get(position).getLevel());
        holder.ll_above.setPadding(mIndentation * mData.get(position).getLevel(),0,0,0);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void expand(int position, boolean animation) {
        mData.get(position).setExpanded(true);
        List<ChapterBean> dataToInsert = mData.get(position).getChildren();
        mData.addAll(position + 1, dataToInsert);
        if (animation) {
            notifyItemRangeInserted(position + 1, dataToInsert.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public void collapse(int position, boolean animation) {
        mData.get(position).setExpanded(false);
        List<ChapterBean> dataToRemove;
        if (animation) {
            dataToRemove = getAllNodesWithChildrenExpanded(mData.get(position).getChildren());
        } else {
            dataToRemove = getAllNodes(mData.get(position).getChildren());
        }
        //收起的时候，需要连子节点一块收起，设置展开状态为false，否则下次刷新，子节点的indicator状态显示不正确
        setExpand(dataToRemove, false);
        mData.removeAll(dataToRemove);
        if (animation) {
            notifyItemRangeRemoved(position + 1, dataToRemove.size());
        } else {
            notifyDataSetChanged();
        }
    }

    private void setExpand(List<ChapterBean> data, boolean expand) {
        if (data == null)
            return;
        for (ChapterBean bean : data) {
            bean.setExpanded(expand);
        }
    }

    public List<ChapterBean> getAllNodes(List<ChapterBean> data) {
        List<ChapterBean> result = new ArrayList<>();
        for (ChapterBean bean : data) {
            result.add(bean);
            if (bean.getChildren() != null && bean.getChildren().size() > 0)
                result.addAll(getAllNodes(bean.getChildren()));
        }
        return result;
    }

    public List<ChapterBean> getAllNodesWithChildrenExpanded(List<ChapterBean> data) {
        List<ChapterBean> result = new ArrayList<>();
        for (ChapterBean bean : data) {
            result.add(bean);
            if (bean.getChildren() != null && bean.getChildren().size() > 0 && bean.isExpanded())
                result.addAll(getAllNodes(bean.getChildren()));
        }
        return result;
    }

    private void initLevel(List<ChapterBean> data, int parentLevel) {
        for (ChapterBean bean : data) {
            bean.setLevel(parentLevel + 1);
            if (bean.getChildren() != null && bean.getChildren().size() > 0) {
                initLevel(bean.getChildren(), bean.getLevel());
            }
        }
    }

    private void setBackgroundByLevel(View itemView,int level){
        switch (level){
            case 0:
                itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 1:
                itemView.setBackgroundColor(Color.parseColor("#fcfcfc"));
                break;
            case 2:
                itemView.setBackgroundColor(Color.parseColor("#f9f9f9"));
                break;
            case 3:
                itemView.setBackgroundColor(Color.parseColor("#f6f6f6"));
                break;
            default:
                itemView.setBackgroundColor(Color.parseColor("#f6f6f6"));
                break;
        }
    }

    private void setTextSizeByLevel(TextView textView,int level){
        switch (level){
            case 0:
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                break;
            case 1:
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                break;
            case 2:
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                break;
            case 3:
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
                break;
            default:
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
                break;
        }
    }
    class ChapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView indicator;
        public TextView text;
        public View ll_indicator,ll_content,ll_above;

        public ChapterViewHolder(final View itemView) {
            super(itemView);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            text = (TextView) itemView.findViewById(R.id.text);
            ll_indicator = itemView.findViewById(R.id.ll_indicator);
            ll_content = itemView.findViewById(R.id.ll_content);
            ll_above = itemView.findViewById(R.id.ll_above);

            ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(itemView,getLayoutPosition(),mData.get(getLayoutPosition()));
                    }
                }
            });

            ll_indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mData.get(getLayoutPosition()).isExpanded()) {
                        indicator.setImageResource(R.drawable.collapse_normal);
                        collapse(getLayoutPosition(),true);
                    } else {
                        indicator.setImageResource(R.drawable.expand_normal);
                        expand(getLayoutPosition(),true);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View itemView,int position,ChapterBean bean);
    }
}
