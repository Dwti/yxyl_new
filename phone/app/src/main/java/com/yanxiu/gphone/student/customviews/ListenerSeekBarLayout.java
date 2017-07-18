package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.util.Logger;
import com.yanxiu.gphone.student.util.MediaPlayerUtil;
import com.yanxiu.gphone.student.util.NetWorkUtils;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;
import com.yanxiu.gphone.student.util.ToastManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/12 15:43.
 * Function :
 */
public class ListenerSeekBarLayout extends LinearLayout implements SeekBar.OnSeekBarChangeListener, MediaPlayerUtil.MediaPlayerCallBack {

    private static final String TAG = "listener_layout";
    private static final int THUMB_WIDTH=ScreenUtils.dpToPxInt(YanxiuApplication.getInstance(),30);
    private static final int THUMB_HEIGHT=ScreenUtils.dpToPxInt(YanxiuApplication.getInstance(),30);

    private Context mContext;
    private int mTouchSlop;
    private MediaPlayerUtil mMediaPlayerUtil;

    /**
     * Solve repetitive click problems
     * */
    private boolean isCanClick=true;
    private boolean isPlaying = false;
    private boolean isMove = false;
    /**
     * onresume  onpuash
     * */
    private boolean isPause = false;
    /**
     * Solve the playback progress problem returned after manual pause
     * */
    private boolean isBackPause=false;
    /**
     * The tester lets you copy iOS and press the state to disable playback
     * */
    private boolean isDownPause=false;

    private SeekBar mSeekBarView;
    private TextView mNowTimeView;
    private TextView mTotalTimeView;
    private View mLeftView;
    private View mRightView;

    private int mMax=0;
    private int mProgress=0;
    private int mSeekProgress=-1;

    private String mUrl;
//        private String mUrl="http://data.5sing.kgimg.com/G034/M05/16/17/ApQEAFXsgeqIXl7gAAVVd-n31lcAABOogKzlD4ABVWP363.mp3";
    private long mMoveDown;

    public ListenerSeekBarLayout(Context context) {
        this(context, null);
    }

    public ListenerSeekBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListenerSeekBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData();
        listener();
    }

    private void initView(Context context) {
        this.mContext = context;
        Logger.d("view_ID",this.hashCode()+"");
        mMediaPlayerUtil = MediaPlayerUtil.create();
        LayoutInflater.from(context).inflate(R.layout.layout_progress_bar, this);
        mSeekBarView = (SeekBar) findViewById(R.id.sb_progress);
        mNowTimeView = (TextView) findViewById(R.id.tv_now_time);
        mTotalTimeView = (TextView) findViewById(R.id.tv_total_time);
        mLeftView = findViewById(R.id.v_left);
        mRightView = findViewById(R.id.v_right);
    }

    private void initData() {
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        ListenDrawable listenDrawable = new ListenDrawable(bitmapDrawable.getBitmap(),0);
        listenDrawable.setBounds(0, 0, THUMB_WIDTH, THUMB_HEIGHT);
        mSeekBarView.setThumb(listenDrawable);
        mSeekBarView.setThumbOffset(mSeekBarView.getPaddingLeft());
        mSeekBarView.post(new Runnable() {
            @Override
            public void run() {
                mSeekBarView.setProgress(0);
            }
        });
        RelativeLayout.LayoutParams params_left = (RelativeLayout.LayoutParams) mLeftView.getLayoutParams();
        params_left.width = mSeekBarView.getPaddingLeft();
        mLeftView.setLayoutParams(params_left);
        RelativeLayout.LayoutParams params_right = (RelativeLayout.LayoutParams) mRightView.getLayoutParams();
        params_right.width = mSeekBarView.getPaddingLeft();
        mRightView.setLayoutParams(params_right);
        String defaultTime = transferFormat(0);
        mNowTimeView.setText(defaultTime);
        mTotalTimeView.setText(defaultTime);
        TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY,mNowTimeView);
        TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY,mTotalTimeView);
    }

    private void listener() {
        mSeekBarView.setOnSeekBarChangeListener(ListenerSeekBarLayout.this);
        mMediaPlayerUtil.addMediaPlayerCallBack(ListenerSeekBarLayout.this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isPlaying) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                    Logger.d("view_ID",ListenerSeekBarLayout.this.hashCode()+"");
                    if (checkIsInternal(ev)&&isCanClick) {
                        mMediaPlayerUtil.start(mUrl);
                    }
                    isCanClick=false;
                    break;
            }
            return true;
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Logger.d(TAG, "down");
                    isMove = false;
                    mMoveDown = 0;
                    if (checkIsInternal(ev)) {
                        mMoveDown = (long) ev.getX();
                        if (mMediaPlayerUtil.isPlaying()) {
                            isDownPause=true;
                            setPlayerPause();
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (checkIsInternal(ev)) {
                        long mMove = (long) ev.getX();
                        long moveX = Math.abs(mMove - mMoveDown);
                        if (moveX > mTouchSlop) {
                            isMove = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    Logger.d(TAG, "up");
                    if (isDownPause){
                        setPlayerResume();
                        isDownPause=false;
                    }
                    setSeekTo();
                    if (checkIsInternal(ev)) {
                        if (!isMove) {
                            if (mMediaPlayerUtil.isPlaying()) {
                                isBackPause=true;
                                setPlayerPause();
                            } else {
                                isBackPause=false;
                                setPlayerResume();
                            }
                        }
                    }
                    isMove = false;
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean checkIsInternal(MotionEvent ev) {
        boolean flag = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        Rect rect = mSeekBarView.getThumb().copyBounds();
        if (x > rect.left && x < rect.right && y > rect.top && y < rect.bottom) {
            flag = true;
        }
        return flag;
    }

    private void setSeekTo(){
        if (!isDownPause) {
            if (mSeekProgress != -1) {
                mMediaPlayerUtil.seekTo(mSeekProgress);
                this.mSeekProgress = -1;
            }
        }
    }

    private void setPlayerPause() {
        Logger.d(TAG, "pause");
        mMediaPlayerUtil.pause();
//        mSeekBarView.setThumb();
    }

    private void setPlayerResume() {
        Logger.d(TAG, "resume");
        mMediaPlayerUtil.resume();
//        mSeekBarView.setThumb();
    }

    public int getProgress(){
        return mProgress;
    }

    public int getMax(){
        return mMax;
    }

    public boolean getIsPlaying(){
        return isPlaying;
    }

    public boolean getIsPause(){
        return isBackPause;
    }

    public void setPlayToProgress(int progress,int total){
        Logger.d(TAG,"setPlayToProgress");
        this.mProgress=progress;
        this.isPlaying=true;
        this.isBackPause=false;
        mSeekBarView.setMax(total);
        mSeekBarView.setProgress(progress);
        mMediaPlayerUtil.start(mUrl);
    }

    public void setPauseToProgress(int progress,int total){
        Logger.d(TAG,"setPauseToProgress");
        this.mProgress=progress;
        this.isPlaying=true;
        this.isBackPause=true;
        mSeekBarView.setMax(total);
        mSeekBarView.setProgress(progress);
        mMediaPlayerUtil.start(mUrl);
    }

    public void setUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        this.mUrl = url;
    }

    public void setPause() {
        Logger.d(TAG,"setPause");
        if (mMediaPlayerUtil.isPlaying()) {
            Logger.d(TAG,"setPause"+" isplaying");
            mMediaPlayerUtil.pause();
            isPause = true;
        }
    }

    public void setResume() {
        Logger.d(TAG,"setResume");
        if (isPause) {
            Logger.d(TAG,"setResume"+"isplaying");
            mMediaPlayerUtil.resume();
            isPause = false;
        }
    }

    public void setDestory() {
        this.mMediaPlayerUtil.destory();
        this.isPlaying = false;
        this.isMove = false;
        this.isPause = false;
        this.mSeekBarView.setProgress(0);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            Logger.d(TAG, "change");
            this.mSeekProgress=progress;
            String nowTime = transferFormat(progress);
            mNowTimeView.setText(nowTime);
            setSeekTo();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStart(MediaPlayerUtil mpu, int duration) {
        Logger.d(TAG,"onstart");
        this.isPlaying = true;
        this.isCanClick=true;
        this.mMax=duration;
        mSeekBarView.setMax(duration);
        String totalTime = transferFormat(duration);
        mTotalTimeView.setText(totalTime);
        if (mProgress!=0) {
            mMediaPlayerUtil.seekTo(mProgress);
        }
        if (isBackPause){
            mMediaPlayerUtil.pause();
        }
    }

    @Override
    public void onProgress(MediaPlayerUtil mu, int progress) {
        Logger.d(TAG, "progress" + progress);
        this.mProgress=progress;
        if (progress>mSeekBarView.getProgress()) {
            mSeekBarView.setProgress(progress);
        }
        String nowTime = transferFormat(progress);
        mNowTimeView.setText(nowTime);
    }

    @Override
    public void onCompletion(MediaPlayerUtil mu) {
        Logger.d(TAG, "completion");
        this.isPlaying = false;
        this.mProgress=0;
        //Create visual effects and enhance user experience
        mSeekBarView.setProgress(mSeekBarView.getMax());
        mSeekBarView.post(new Runnable() {
            @Override
            public void run() {
                mSeekBarView.setProgress(0);
            }
        });

        String nowTime = transferFormat(0);
        mNowTimeView.setText(nowTime);
    }

    @Override
    public void onError(MediaPlayerUtil mu) {
        Logger.d(TAG,"onerror");
        this.isPlaying = false;
        this.isCanClick=true;
        this.mProgress=0;
        mSeekBarView.setProgress(0);
        String nowTime = transferFormat(0);
        mNowTimeView.setText(nowTime);
        if (NetWorkUtils.isNetAvailable()) {
            ToastManager.showMsg(R.string.voice_url_error);
        }else {
            ToastManager.showMsg(R.string.net_null);
        }
    }

    private String transferFormat(double duration) {
        duration = duration / 1000.0;
        duration = Math.rint(duration);
        String minute = String.valueOf((int) duration / 60);
        String second = String.valueOf((int) duration % 60);

        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        return minute + ":" + second;
    }
}
