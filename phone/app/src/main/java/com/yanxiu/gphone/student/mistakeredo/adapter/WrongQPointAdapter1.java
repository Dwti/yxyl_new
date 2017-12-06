package com.yanxiu.gphone.student.mistakeredo.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.exercise.adapter.BaseExpandableRecyclerAdapter;
import com.yanxiu.gphone.student.exercise.bean.Node;
import com.yanxiu.gphone.student.mistakeredo.bean.WrongQPointBean;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.List;

/**
 * Created by sp on 17-7-31.
 */

public class WrongQPointAdapter1 extends BaseExpandableRecyclerAdapter<WrongQPointBean> {
    private int mIndentation;  //缩进
    public WrongQPointAdapter1(List<WrongQPointBean> data) {
        super(data);
    }

    @Override
    public WrongQPointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mIndentation == 0)
            mIndentation = ScreenUtils.dpToPxInt(parent.getContext(), 50);
        return new WrongQPointViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wrongqpoint, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mData.get(position).isExpanded()) {
            ((WrongQPointViewHolder)holder).indicator.setImageResource(R.drawable.expand_normal);
        } else {
            ((WrongQPointViewHolder)holder).indicator.setImageResource(R.drawable.collapse_normal);
        }
        if (mData.get(position).hasChildren()) {
            ((WrongQPointViewHolder)holder).iv_arrow_in.setVisibility(View.INVISIBLE);
            ((WrongQPointViewHolder)holder).ll_indicator.setVisibility(View.VISIBLE);
        } else {
            ((WrongQPointViewHolder)holder).iv_arrow_in.setVisibility(View.VISIBLE);
            ((WrongQPointViewHolder)holder).ll_indicator.setVisibility(View.INVISIBLE);
        }
        setBackgroundByLevel(holder.itemView,mData.get(position).getLevel());
        setTextSizeByLevel(((WrongQPointViewHolder)holder).text,mData.get(position).getLevel());
        ((WrongQPointViewHolder)holder).ll_above.setPadding(mIndentation * mData.get(position).getLevel(),0,0,0);
        ((WrongQPointViewHolder)holder).text.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

    protected class WrongQPointViewHolder extends RecyclerView.ViewHolder {

        public ImageView indicator,iv_arrow_in;
        public TextView text;
        public View ll_indicator,ll_content,ll_above;

        public WrongQPointViewHolder(final View itemView) {
            super(itemView);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            text = (TextView) itemView.findViewById(R.id.text);
            ll_indicator = itemView.findViewById(R.id.ll_indicator);
            ll_content = itemView.findViewById(R.id.ll_content);
            ll_above = itemView.findViewById(R.id.ll_above);
            iv_arrow_in = (ImageView) itemView.findViewById(R.id.iv_arrow_in);
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

    private void setTitleText(String name,String count){

    }
}
