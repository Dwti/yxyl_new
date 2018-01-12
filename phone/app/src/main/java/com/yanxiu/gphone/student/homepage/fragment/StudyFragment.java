package com.yanxiu.gphone.student.homepage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/12 12:06.
 * Function :
 */
public class StudyFragment extends HomePageBaseFragment {

    private PublicLoadLayout rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=new PublicLoadLayout(inflater.getContext());
        rootView.setContentView(R.layout.fragment_study);
        initView();
        listener();
        initData();
        return rootView;
    }

    private void initView() {

    }

    private void listener() {

    }

    private void initData() {

    }
}
