package com.yanxiu.gphone.student.learning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bcresource.bean.TopicBean;
import com.yanxiu.gphone.student.learning.GlideRoundTransform;
import com.yanxiu.gphone.student.learning.bean.VideoDataBean;
import com.yanxiu.gphone.student.learning.response.GetResourceListDataResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.util.DataFetcher;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufengqing on 2018/1/29.
 */

public class VideoListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<VideoDataBean> mData = new ArrayList<>();
    private OnItemClickListener mListener;

    public VideoListAdapter(Context context, List<VideoDataBean> mData, OnItemClickListener listener) {
        mContext = context;
        this.mData = mData;
        this.mListener = listener;
    }


    public void replaceData(List<VideoDataBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void clearData(){
        if(mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mData == null) {
            return 0;
        }
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
//        if(mData == null) {
//            return TextView();
//        }
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_video,parent,false);
        }
        ImageView video_cover = (ImageView) convertView.findViewById(R.id.video_cover);
        TextView video_name = (TextView) convertView.findViewById(R.id.video_name);
        TextView video_play_time = (TextView) convertView.findViewById(R.id.play_times);
        video_name.setText(mData.get(position).getTitle());
        video_play_time.setText(mContext.getResources().getString(R.string.play_times,mData.get(position).getViewnum()));
//        Paper mPaper = DataFetcher.getInstance().getPaper("413596");
//        String url = "http://www.yixueyilian.com/static/task/widget/comm/menu/img/logo.png";
//        Glide.with(mContext).load(url).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(mContext, 6)).placeholder(R.drawable.video_cover_default).crossFade().error(R.drawable.video_cover_default).into(video_cover);
        Glide.with(mContext).load(mData.get(position).getRes_thumb()).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(mContext, 6)).placeholder(R.drawable.video_cover_default).crossFade().error(R.drawable.video_cover_default).into(video_cover);
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

    public void updatePlayTimes(int position) {
        mData.get(position).setViewnum(mData.get(position).getViewnum()+1);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onClick(VideoDataBean bean, int position);
    }
}


