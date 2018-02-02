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
import com.yanxiu.gphone.student.learning.GlideRoundTransform;
import com.yanxiu.gphone.student.learning.bean.VideoDataBean;
import com.yanxiu.gphone.student.learning.response.GetRelatedCourseResponse;



import java.util.List;

/**
 * Created by lufengqing on 2018/1/17.
 */

public class RelatedVideoAdapter extends BaseAdapter {

    private final Context mContext;
    private List<VideoDataBean> mData;
    private OnItemClickListener mListener;

    public RelatedVideoAdapter(Context context, List<VideoDataBean> mData, OnItemClickListener listener) {
        mContext = context;
        this.mData = mData;
        this.mListener = listener;
    }


    public void replaceData(List<VideoDataBean> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void clearData(){
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mData.size() > 6) {
            return 6;
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
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related_video,parent,false);
        }
        ImageView video_cover = (ImageView) convertView.findViewById(R.id.video_cover);
        TextView video_name = (TextView) convertView.findViewById(R.id.video_name);
        video_name.setText(mData.get(position).getTitle());
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

    public interface OnItemClickListener{
        void onClick(VideoDataBean bean, int position);
    }
}

