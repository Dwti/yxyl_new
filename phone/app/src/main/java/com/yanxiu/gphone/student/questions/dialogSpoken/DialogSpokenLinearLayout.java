package com.yanxiu.gphone.student.questions.dialogSpoken;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.SpokenSpanTextView;
import com.yanxiu.gphone.student.questions.spoken.AudioTagHandler;
import com.yanxiu.gphone.student.questions.spoken.SpokenUtils;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.MediaPlayerUtil;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/30 17:40.
 * Function :
 */
public class DialogSpokenLinearLayout extends LinearLayout {

    public interface DialogSpokenStatusChangeCallback {
        //初始化完成
        void onDialogSpokenStart();
        //作答完成
        void onDialogSpokenEnd();
        //作答按钮可用
        void onButtonAble();
        //作答按钮不可用
        void onButtonUnable();
        //开始播放音频
        void onMediaPlayStart();
        //音频播放中
        void onMediaPlaying();
        //音频播放结束
        void onMediaPlayEnd();
        //开始录音
        void onRecordStart();
        //录音中，音量变化
        void onRecording(int volume);
        //已录音时间
        void onProgress(int progress);
        //录音达最大时长，需要终止录音
        void onFinished();
        //录音结束
        void onRecorEnd();
        //开始测评
        void onStartOralEval();
        //测评结束（是否是因为异常）
        void onStopOralEval(boolean isError);
        //测评结果返回
        void onResult(String result);
        //参与测评音频的网络地址(本地地址已废弃)
        void onResultUrl(String filePath, String url);
        //没有权限
        void onNoPermission();
        //播放异常
        void onError(String result);
        //设置出错
        void onFailed(String text);
    }

    private static final int DEFAULT_POSITION=0;

    private int mPosition=DEFAULT_POSITION;
    private DialogSpokenStatusChangeCallback mStatusChangeCallback;
    private MediaPlayerUtil mMediaPlayerUtil = MediaPlayerUtil.create();
    private SpokenUtils mSpokenUtils=SpokenUtils.create();

    public DialogSpokenLinearLayout(Context context) {
        this(context, null);
    }

    public DialogSpokenLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogSpokenLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMediaPlayerUtil.addMediaPlayerCallBack(mMediaPlayerCallBack);
    }

    public void setStatusChangeCallback(DialogSpokenStatusChangeCallback statusChangeCallback) {
        this.mStatusChangeCallback = statusChangeCallback;
    }

    public void setData(List<String> list) {
        if (list == null) {
            return;
        }
        for (String s : list) {
            s = s.replaceAll("<start>", "");
            s = s.replaceAll("</start>", "");
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_ds_view, this, false);
            final ViewHolder holder = new ViewHolder();
            holder.mQuestionView = (SpokenSpanTextView) view.findViewById(R.id.tv_question);
            holder.mHandLayout = (LinearLayout) view.findViewById(R.id.ll_hand);
            holder.mHandView1 = (ImageView) view.findViewById(R.id.iv_hand1);
            holder.mHandView2 = (ImageView) view.findViewById(R.id.iv_hand2);
            holder.mHandView3 = (ImageView) view.findViewById(R.id.iv_hand3);

            AudioTagHandler mAudioTagHandler = new AudioTagHandler(getContext(), holder.mQuestionView, new AudioTagHandler.UrlCallback() {
                @Override
                public void urlCallback(String url) {
                    holder.mUrl = url;
                }
            });
            Spanned string = Html.fromHtml(s, new HtmlImageGetter(holder.mQuestionView), mAudioTagHandler);
            holder.mQuestionView.setData(string);
            holder.mQuestionView.setHighlightColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            holder.mAudioTagHandler = mAudioTagHandler;
            view.setTag(holder);
            this.addView(view);
        }
        if (mStatusChangeCallback!=null){
            mStatusChangeCallback.onDialogSpokenStart();
        }
    }

    public void play(int position) {
        if (position < getChildCount()) {
            mPosition=position;
            View view = getChildAt(position);
            resetChildBg();
            view.setBackgroundResource(R.drawable.shape_dialogspoken_select_item_bg);
            ViewHolder holder = (ViewHolder) view.getTag();
            if (TextUtils.isEmpty(holder.mUrl)) {
                if (mStatusChangeCallback!=null){
                    mStatusChangeCallback.onButtonAble();
                }
            } else {
                if (mStatusChangeCallback!=null){
                    mStatusChangeCallback.onButtonUnable();
                }
                mMediaPlayerUtil.start(holder.mUrl);
            }
        }
    }

    private void resetChildBg(){
        int count=getChildCount();
        for (int i=0;i<count;i++){
            View view=getChildAt(i);
            view.setBackgroundResource(0);
        }
    }

    public void record(String answer){
        mSpokenUtils.start(getContext(), answer, mBaseOralEvalCallback, new SpokenUtils.onOralEvaProgressCallback() {
            @Override
            public void onProgress(int progress) {
                if (mStatusChangeCallback!=null){
                    mStatusChangeCallback.onProgress(progress);
                }
            }

            @Override
            public void onFinished() {
                if (mStatusChangeCallback!=null){
                    mStatusChangeCallback.onFinished();
                }
            }
        });
    }

    public void stopRecord(){
        mSpokenUtils.stop();
    }

    public void cancelRecord(){
        mSpokenUtils.cancel();
    }

    public void onDestory(){
        mSpokenUtils.cancel();
        mMediaPlayerUtil.destory();
    }

    private MediaPlayerUtil.MediaPlayerCallBack mMediaPlayerCallBack= new MediaPlayerUtil.MediaPlayerCallBack() {
        @Override
        public void onStart(MediaPlayerUtil mpu, int duration) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onMediaPlayStart();
            }
        }

        @Override
        public void onProgress(MediaPlayerUtil mu, int progress) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onMediaPlaying();
            }
        }

        @Override
        public void onCompletion(MediaPlayerUtil mu) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onMediaPlayEnd();
                mStatusChangeCallback.onButtonAble();
            }
        }

        @Override
        public void onError(MediaPlayerUtil mu) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onFailed("音频播放失败");
                mStatusChangeCallback.onButtonAble();
            }
        }
    };

    private SpokenUtils.onBaseOralEvalCallback mBaseOralEvalCallback=new SpokenUtils.onBaseOralEvalCallback() {
        @Override
        public void onStartRecord(String filePath) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onRecordStart();
            }
        }

        @Override
        public void onStopOralEval(boolean isError) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onStopOralEval(isError);
                if (!isError&&mPosition==getChildCount()-1){
                    mStatusChangeCallback.onDialogSpokenEnd();
                }
            }
        }

        @Override
        public void onVolume(int volume) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onRecording(volume);
            }
        }

        @Override
        public void onStartOralEval() {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onRecorEnd();
                mStatusChangeCallback.onStartOralEval();
            }
        }

        @Override
        public void onResult(String result) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onResult(result);
            }
        }

        @Override
        public void onResultUrl(String filePath, String url) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onResultUrl(filePath, url);
            }
        }

        @Override
        public void onNoPermission() {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onRecorEnd();
                mStatusChangeCallback.onNoPermission();
            }
        }

        @Override
        public void onError(String result) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onRecorEnd();
                mStatusChangeCallback.onError(result);
            }
        }

        @Override
        public void onFailed(String text) {
            if (mStatusChangeCallback!=null){
                mStatusChangeCallback.onRecorEnd();
                mStatusChangeCallback.onFailed(text);
            }
        }
    };

    public class ViewHolder {

        private String mUrl;
        private AudioTagHandler mAudioTagHandler;

        public SpokenSpanTextView mQuestionView;
        private LinearLayout mHandLayout;
        private ImageView mHandView1;
        private ImageView mHandView2;
        private ImageView mHandView3;
    }
}
