package com.yanxiu.gphone.student.learning.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bcresource.bean.TopicBean;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.learning.GlideRoundTransform;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.util.DataFetcher;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by lufengqing on 2018/1/17.
 */

public class RelatedVideoAdapter extends BaseAdapter {

    private final Context mContext;
    private List<TopicBean> mData;
    private OnItemClickListener mListener;

    public RelatedVideoAdapter(Context context, List<TopicBean> mData, OnItemClickListener listener) {
        mContext = context;
        this.mData = mData;
        this.mListener = listener;
    }


    public void replaceData(List<TopicBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void clearData(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void setPaperStatus(int position, TopicBean.PaperStatusBean paperStatusBean){
        if(mData != null && position < mData.size()){
            mData.get(position).setPaperStatus(paperStatusBean);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related_video,parent,false);
        }
        ImageView video_cover = (ImageView) convertView.findViewById(R.id.video_cover);
        TextView video_name = (TextView) convertView.findViewById(R.id.video_name);
        video_name.setText(mData.get(position).getName());
        Paper mPaper = DataFetcher.getInstance().getPaper("413596");
        String url = "http://www.yixueyilian.com/static/task/widget/comm/menu/img/logo.png";
        Glide.with(mContext).load(url).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(mContext, 6)).placeholder(R.drawable.video_cover_default).crossFade().error(R.drawable.video_cover_default).into(video_cover);
//        Glide.with(mContext).load(mPaper.getCover()).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(mContext, 6)).placeholder(R.drawable.video_cover_default).crossFade().error(R.drawable.video_cover_default).into(video_cover);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(mData.get(position), position);
                }
            }
        });

        return convertView;
    }

    public interface OnItemClickListener{
        void onClick(TopicBean bean, int position);
    }
}

