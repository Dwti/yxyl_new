package com.yanxiu.gphone.student.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.util.AlbumUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/22 13:56.
 * Function :
 */
public class AlbumImageAdapter extends RecyclerView.Adapter<AlbumImageAdapter.AlbumImageViewHolder> {

    public interface onItemClickListener{
        void onItemClick(View view,AlbumUtils.PictureMessage message,int position);
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private List<AlbumUtils.PictureMessage> mData=new ArrayList<>();
    private onItemClickListener mItemClickListener;

    public AlbumImageAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
    }

    public void setData(List<AlbumUtils.PictureMessage> data){
        if (data==null){
            return;
        }
        this.mData.clear();
        this.mData.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addItemClickListener(onItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }

    @Override
    public AlbumImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.adapter_album_item,parent,false);
        return new AlbumImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumImageViewHolder holder, int position) {
        AlbumUtils.PictureMessage message=mData.get(position);
        Glide.with(mContext).load(message.path).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size():0;
    }

    class AlbumImageViewHolder extends RecyclerView.ViewHolder{

        WavesLayout mWavesView;
        ImageView mImageView;

        AlbumImageViewHolder(final View itemView) {
            super(itemView);
            mWavesView= (WavesLayout) itemView.findViewById(R.id.wl_conver);
            mImageView= (ImageView) itemView.findViewById(R.id.iv_image);

            mWavesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener!=null){
                        int position=getLayoutPosition();
                        AlbumUtils.PictureMessage message=mData.get(position);
                        mItemClickListener.onItemClick(itemView,message,position);
                    }
                }
            });
        }
    }
}
