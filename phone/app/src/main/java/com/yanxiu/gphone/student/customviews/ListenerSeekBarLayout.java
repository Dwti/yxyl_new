package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
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
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

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
    private boolean isPlaying = false;
    private boolean isMove = false;
    private boolean isPause = false;
    private SeekBar mSeekBarView;
    private TextView mNowTimeView;
    private TextView mTotalTimeView;
    private View mLeftView;
    private View mRightView;

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
        ListenDrawable listenDrawable = new ListenDrawable(bitmapDrawable.getBitmap());
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
                    if (checkIsInternal(ev)) {
                        mMediaPlayerUtil.start(mUrl);
                    }
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
                    if (checkIsInternal(ev)) {
                        if (!isMove) {
                            if (mMediaPlayerUtil.isPlaying()) {
                                setPlayerPause();
                            } else {
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

    public void setUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        this.mUrl = url;
    }

    public void setPause() {
        if (mMediaPlayerUtil.isPlaying()) {
            mMediaPlayerUtil.pause();
            isPause = true;
        }
    }

    public void setResume() {
        if (isPause) {
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
        if (fromUser && isMove) {
            Logger.d(TAG, "change");
            mMediaPlayerUtil.seekTo(progress);
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
        Logger.d("view_ID",ListenerSeekBarLayout.this.hashCode()+"");
        this.isPlaying = true;
        mSeekBarView.setMax(duration);
        String totalTime = transferFormat(duration);
        mTotalTimeView.setText(totalTime);
    }

    @Override
    public void onProgress(MediaPlayerUtil mu, int progress) {
        Logger.d(TAG, "progress" + progress);
        mSeekBarView.setProgress(progress);
        String nowTime = transferFormat(progress);
        mNowTimeView.setText(nowTime);
    }

    @Override
    public void onCompletion(MediaPlayerUtil mu) {
        Logger.d(TAG, "completion");
        this.isPlaying = false;
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
        this.isPlaying = false;
        mSeekBarView.setProgress(0);
        String nowTime = transferFormat(0);
        mNowTimeView.setText(nowTime);
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
