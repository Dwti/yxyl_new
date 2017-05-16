package com.yanxiu.gphone.student.homepage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;

/**
 * 首页 作业列表Fragment
 */
public class ExerciseFragment extends Fragment {
    private final static String TAG = ExerciseFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework, container, false);
        return view;
    }
}
