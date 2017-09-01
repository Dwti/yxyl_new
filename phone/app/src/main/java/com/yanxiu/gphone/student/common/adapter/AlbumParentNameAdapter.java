package com.yanxiu.gphone.student.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/23 10:41.
 * Function :
 */
public class AlbumParentNameAdapter extends RecyclerView.Adapter<AlbumParentNameAdapter.AlbumParentNameViewHolder> {

    public interface onItemClickListener {
        void onItemClick(View view, ParentImageMessage message, int position);
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ParentImageMessage> mData = new ArrayList<>();
    private onItemClickListener mItemClickListener;
    private int mSelectPosition = -1;

    public AlbumParentNameAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<ParentImageMessage> list) {
        if (list == null) {
            return;
        }
        this.mData.clear();
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public AlbumParentNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_album_item_parentname, parent, false);
        return new AlbumParentNameViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public void onBindViewHolder(AlbumParentNameViewHolder holder, int position) {
        ParentImageMessage message = mData.get(position);
        Glide.with(mContext).load(message.path).asBitmap().into(new CircleImageTarget(holder.mPictureView));
        holder.mParentNameView.setText(message.name);
        holder.mParentNumView.setText("(" + message.num + ")");
        if (mSelectPosition == position) {
            holder.mCheckedView.setVisibility(View.VISIBLE);
        } else {
            holder.mCheckedView.setVisibility(View.GONE);
        }
    }

    private class CircleImageTarget extends BitmapImageViewTarget {

        CircleImageTarget(ImageView view) {
            super(view);
        }

        @Override
        protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            view.setImageDrawable(circularBitmapDrawable);
        }
    }

    class AlbumParentNameViewHolder extends RecyclerView.ViewHolder {

        ImageView mPictureView;
        RelativeLayout mCheckedView;
        TextView mParentNameView;
        TextView mParentNumView;

        AlbumParentNameViewHolder(final View itemView) {
            super(itemView);
            mPictureView = (ImageView) itemView.findViewById(R.id.iv_picture);
            mCheckedView = (RelativeLayout) itemView.findViewById(R.id.iv_checked);
            mParentNameView = (TextView) itemView.findViewById(R.id.tv_name);
            mParentNumView = (TextView) itemView.findViewById(R.id.tv_number);
            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY,mParentNumView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    AlbumParentNameAdapter.this.mSelectPosition = position;
                    if (mItemClickListener != null) {
                        ParentImageMessage message = mData.get(position);
                        mItemClickListener.onItemClick(itemView, message, position);
                    }
                    AlbumParentNameAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    public class ParentImageMessage {
        public String name;
        public String path;
        public String num;
    }
}
