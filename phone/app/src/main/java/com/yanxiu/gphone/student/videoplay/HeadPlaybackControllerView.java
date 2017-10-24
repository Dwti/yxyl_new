package com.yanxiu.gphone.student.videoplay;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yanxiu.gphone.student.R;


/**
 * Created by cailei on 04/07/2017.
 */

public class HeadPlaybackControllerView extends PlaybackControllerView {
    public HeadPlaybackControllerView(@NonNull Context context) {
        super(context);
    }

    public HeadPlaybackControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadPlaybackControllerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int layoutId() {
        return R.layout.head_playback_control;
    }

    @Override
    public void hide() {
    }
}
