package com.yanxiu.gphone.student.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.db.SpManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/17 11:47.
 * Function :
 */
public class SoundManger {

    private static final int MAX_MUSIC_NUM=2;
    private static final int NOW_MUSIC_NUM=2;

    private int MUSIC_TAB;
    private int MUSIC_SUBMIT;

    private static SoundManger mSoundManger;
    private SoundPool mSoundPool;

    private int loadNum=0;
    private boolean isLoadComplete=false;
    private boolean isCanPlay=false;

    public static SoundManger getInstence(){
        if (mSoundManger==null){
            synchronized (SoundManger.class){
                if(mSoundManger == null)
                    mSoundManger=new SoundManger();
            }
        }
        return mSoundManger;
    }

    private SoundManger(){
        init();
    }

    private void init(){
        isCanPlay = SpManager.isSoundOn();
        mSoundPool=new SoundPool(MAX_MUSIC_NUM, AudioManager.STREAM_SYSTEM,0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadNum++;
                if (loadNum==NOW_MUSIC_NUM){
                    isLoadComplete=true;
                }
            }
        });
    }

    public void initialize(Context context){
        if (mSoundPool!=null){
            MUSIC_TAB=mSoundPool.load(context, R.raw.tab,1);
            MUSIC_SUBMIT=mSoundPool.load(context,R.raw.submit,1);
        }else {
            init();
            initialize(context);
        }
    }

    public void setCanPlay(boolean isCanPlay){
        this.isCanPlay=isCanPlay;
    }

    public void playTabMusic(){
        if (mSoundPool!=null&&isLoadComplete&&isCanPlay){
            mSoundPool.play(MUSIC_TAB,1,1,0,0,1);
        }
    }

    public void playSubmitMusic(){
        if (mSoundPool!=null&&isLoadComplete&&isCanPlay){
            mSoundPool.play(MUSIC_SUBMIT,1,1,0,0,1);
        }
    }
}
