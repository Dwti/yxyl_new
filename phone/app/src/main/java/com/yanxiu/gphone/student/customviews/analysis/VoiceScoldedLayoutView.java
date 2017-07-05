package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.MediaPlayerUtil;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/4 15:57.
 * Function :
 */
public class VoiceScoldedLayoutView extends RelativeLayout implements MediaPlayerUtil.MediaPlayerCallBack {

    private static final int DEFAULT_WIDTH=80;
    private static final int DEFAULT_HEIGHT=45;
    private static final int DEFAULT_LINE_HEIGHT=10;

    private Context mContext;
    private LinearLayout mGroupLayout;
    private MediaPlayerUtil mMediaPlayerUtil;
    private int mPlayingPosition=-1;
    private boolean mIsStartPlay=false;
    private boolean mIsPause = false;
    private int mHeight;

    public VoiceScoldedLayoutView(Context context) {
        this(context,null);
    }

    public VoiceScoldedLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VoiceScoldedLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext=context;
        mHeight=ScreenUtils.dpToPxInt(mContext,DEFAULT_HEIGHT);
        mMediaPlayerUtil = MediaPlayerUtil.create();
        LayoutInflater.from(mContext).inflate(R.layout.layout_voicescolded,this);
        mGroupLayout= (LinearLayout) findViewById(R.id.ll_group);
        mMediaPlayerUtil.addMediaPlayerCallBack(VoiceScoldedLayoutView.this);
    }

    public void setData(List<ScoldedMessage> list){
        if (list==null||list.size()==0){
            return;
        }
        for (int i=0;i<list.size();i++){
            final View itemView=LayoutInflater.from(mContext).inflate(R.layout.item_voicescolded,this,false);
            ViewHolder viewHolder=new ViewHolder();
            int time=list.get(i).time;
            viewHolder.mItemLayout= (LinearLayout) itemView.findViewById(R.id.ll_item);
            viewHolder.mItemLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClick(itemView);
                }
            });
            int width=ScreenUtils.dpToPxInt(mContext,time+DEFAULT_WIDTH);
            LayoutParams params=new LayoutParams(width, mHeight);
            if (i!=0){
                params.topMargin=ScreenUtils.dpToPxInt(mContext,DEFAULT_LINE_HEIGHT);
            }
            viewHolder.mItemLayout.setLayoutParams(params);
            viewHolder.mAnimView= (ImageView) itemView.findViewById(R.id.iv_anim);
            viewHolder.mTimeView= (TextView) itemView.findViewById(R.id.tv_time);
            viewHolder.mTimeView.setText(getTimeFormat(time));
            viewHolder.url=list.get(i).url;
            viewHolder.position=i;
            itemView.setTag(viewHolder);
            mGroupLayout.addView(itemView);
        }
    }

    public void setPause() {
        if (mMediaPlayerUtil.isPlaying()) {
            mMediaPlayerUtil.pause();
            mIsPause = true;
        }
    }

    public void setDestory(){
        this.mMediaPlayerUtil.destory();
        this.mPlayingPosition=-1;
        this.mIsStartPlay=false;
        this.mIsPause=false;
    }

    public void setResume() {
        if (mIsPause) {
            mMediaPlayerUtil.resume();
            mIsPause = false;
        }
    }

    private void OnClick(View view){
        ViewHolder holder= (ViewHolder) view.getTag();
        if (this.mPlayingPosition!=-1&&mPlayingPosition==holder.position){
            if (mIsStartPlay) {
                if (mMediaPlayerUtil.isPlaying()) {
                    mMediaPlayerUtil.pause();
                    setAnim(mPlayingPosition,false);
                } else {
                    mMediaPlayerUtil.resume();
                    setAnim(mPlayingPosition,true);
                }
            }
            return;
        }
        if (mMediaPlayerUtil.isPlaying()){
            mMediaPlayerUtil.playFinish();
            setAnim(mPlayingPosition,false);
        }
        mIsStartPlay=false;
        this.mPlayingPosition=holder.position;
        mMediaPlayerUtil.start(holder.url);
    }

    private String getTimeFormat(int time){
        int min=time/60;
        int sec=time%60;
        return min==0?sec+"''":min+"'"+sec+"''";
    }

    private void setAnim(int index,boolean isCanShow){
        if (index<mGroupLayout.getChildCount()){
            ViewHolder holder= (ViewHolder) mGroupLayout.getChildAt(index).getTag();
            if (isCanShow) {
                try {
                    holder.mAnimView.setImageResource(R.drawable.anim_voice_scolded);
                    AnimationDrawable animationDrawable = (AnimationDrawable) holder.mAnimView.getDrawable();
                    animationDrawable.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                holder.mAnimView.setImageResource(R.drawable.voice_anim_icon);
            }
        }
    }

    @Override
    public void onStart(MediaPlayerUtil mpu, int duration) {
        mIsStartPlay=true;
        setAnim(mPlayingPosition,true);
    }

    @Override
    public void onProgress(MediaPlayerUtil mu, int progress) {

    }

    @Override
    public void onCompletion(MediaPlayerUtil mu) {
        setAnim(mPlayingPosition,false);
        playNext();
    }

    @Override
    public void onError(MediaPlayerUtil mu) {
        ToastManager.showMsg(R.string.voice_url_error);
        setAnim(mPlayingPosition,false);
        playNext();
    }

    private void playNext(){
        if (mPlayingPosition<mGroupLayout.getChildCount()-1){
            View view=mGroupLayout.getChildAt(mPlayingPosition+1);
            OnClick(view);
        }
    }

    public static class ScoldedMessage{
        public String url;
        public int time;

        public static ScoldedMessage obtain(){
            return new ScoldedMessage();
        }
    }

    private class ViewHolder{
        int position;
        String url;
        LinearLayout mItemLayout;
        ImageView mAnimView;
        TextView mTimeView;
    }
}
