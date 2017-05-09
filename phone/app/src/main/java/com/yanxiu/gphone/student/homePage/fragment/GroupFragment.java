package com.yanxiu.gphone.student.homePage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;

/**
 * 首页 作业列表Fragment
 */
public class GroupFragment extends Fragment {
//    HomeWorkFragment
    private final static String TAG = GroupFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment, container, false);
        return view;
    }
}
