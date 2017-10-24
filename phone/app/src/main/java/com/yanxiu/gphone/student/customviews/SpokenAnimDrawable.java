package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.spoken.CatchUtils;
import com.yanxiu.gphone.student.util.Logger;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/23 10:14.
 * Function :                    SB 美工，妄想使用两套尺寸
 */
public class SpokenAnimDrawable extends Drawable {

    private static final String TAG="SpokenAnimDrawable";

    private static final int DURATION=200;
    private static final int KEY_DEFAULT=0x000;

    public static final int TYPE_DEFAULT = 0x001;
    public static final int TYPE_TOUCH = 0x002;
    public static final int TYPE_ANIM = 0x003;

    private static final int mAudioNormal = R.drawable.spoken_audio_normal;
    private static final int mAudioPress = R.drawable.spoken_audio_normal;

    private static final int mAudioAnim1= R.drawable.spoken_audio_anim1;
    private static final int mAudioAnim2=R.drawable.spoken_audio_anim2;
    private static final int mAudioAnim3=R.drawable.spoken_audio_anim3;

    private Context mContext;
    private View mView;
    private AnimHandle mHandle=new AnimHandle();
    private int mWidth,mHeight;
    private Paint mPaint;

    private int mType=TYPE_DEFAULT;
    private int mAinmTime=-1;

    public SpokenAnimDrawable(Context context){
        this.mContext=context;
        init();
    }

    private void init(){
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mHandle.setHandle(mHandle);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Logger.d(TAG,"draw   Type="+mType+"   time="+mAinmTime);
        Bitmap bitmap;
        if (mType==TYPE_TOUCH){
            bitmap=findBitmap(mAudioPress);
        }else if (mType==TYPE_ANIM){
            switch (mAinmTime){
                case 0:
                    bitmap=findBitmap(mAudioAnim1);
                    break;
                case 1:
                    bitmap=findBitmap(mAudioAnim2);
                    break;
                case 2:
                    bitmap=findBitmap(mAudioAnim3);
                    break;
                default:
                    bitmap=findBitmap(mAudioNormal);
                    break;
            }
            mAinmTime++;
            mAinmTime=mAinmTime%3;
        }else {
            bitmap=findBitmap(mAudioNormal);
        }
        canvas.drawBitmap(bitmap,0,0,mPaint);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        this.mWidth=right-left;
        this.mHeight=bottom-top;
        findBitmap(KEY_DEFAULT);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setView(View view){
        this.mView=view;
        mHandle.setView(mView);
    }

    public void setType(int type){
        this.mType=type;
        mView.invalidate();
    }

    public void startAnim(){
        mHandle.sendEmptyMessage(0);
    }

    public void stopAnim(){
        mHandle.removeMessages(0);
    }

    private class AnimHandle extends Handler{

        private Handler mHandle;
        private View mView;

        private void setView(View view){
            this.mView=view;
        }

        private void setHandle(Handler handle){
            this.mHandle=handle;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.d(TAG,"handleMessage");
            mView.invalidate();
            mHandle.sendEmptyMessageDelayed(0,DURATION);
        }
    }

    private Bitmap findBitmap(int key){
        Bitmap bitmap=CatchUtils.getInstence().get(String.valueOf(key));
        switch (key){
            case KEY_DEFAULT:
                CatchUtils.getInstence().put(String.valueOf(mAudioNormal),zoom(mAudioNormal));
                CatchUtils.getInstence().put(String.valueOf(mAudioPress),zoom(mAudioPress));
                CatchUtils.getInstence().put(String.valueOf(mAudioAnim1),zoom(mAudioAnim1));
                CatchUtils.getInstence().put(String.valueOf(mAudioAnim2),zoom(mAudioAnim2));
                CatchUtils.getInstence().put(String.valueOf(mAudioAnim3),zoom(mAudioAnim3));
                break;
            case mAudioNormal:
                if (bitmap==null){
                    bitmap=zoom(mAudioNormal);
                    CatchUtils.getInstence().put(String.valueOf(key),bitmap);
                }
                break;
//            case mAudioPress:
//                if (bitmap==null){
//                    bitmap=zoom(mAudioPress);
//                    CatchUtils.getInstence().put(String.valueOf(key),bitmap);
//                }
//                break;
            case mAudioAnim1:
                if (bitmap==null){
                    bitmap=zoom(mAudioAnim1);
                    CatchUtils.getInstence().put(String.valueOf(key),bitmap);
                }
                break;
            case mAudioAnim2:
                if (bitmap==null){
                    bitmap=zoom(mAudioAnim2);
                    CatchUtils.getInstence().put(String.valueOf(key),bitmap);
                }
                break;
            case mAudioAnim3:
                if (bitmap==null){
                    bitmap=zoom(mAudioAnim3);
                    CatchUtils.getInstence().put(String.valueOf(key),bitmap);
                }
                break;
            default:
                if (bitmap==null){
                    bitmap=zoom(mAudioNormal);
                }
                break;
        }
        return bitmap;
    }

    private Bitmap zoom(int drawableId){
        Drawable drawable= ContextCompat.getDrawable(mContext,drawableId);
        BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
        return Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(),mWidth,mHeight,false);
    }
}
