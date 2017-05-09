package com.yanxiu.gphone.student.homePage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;

/**
 * 首页“练习”Fragment
 */
public class HomeWorkFragment extends Fragment {
    //ExerciseFragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intelliexe, container, false);
        return view;
    }
}
