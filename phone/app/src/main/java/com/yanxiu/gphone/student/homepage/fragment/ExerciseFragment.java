package com.yanxiu.gphone.student.homepage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;

/**
 * 首页 “练习”Fragment
 */
public class ExerciseFragment extends HomePageBaseFragment {
    private final static String TAG = ExerciseFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        return view;
    }

    /**
     * 请求数据
     */
    @Override
    public void requestData() {
        Log.e("dyf",TAG);
    }
}
