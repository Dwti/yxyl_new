package com.yanxiu.gphone.student.homework.questions.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;
import com.yanxiu.gphone.student.homework.questions.model.ReadingComplexQuestion;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class ReadingComplexTopFragment extends TopFragment {
    ReadingComplexQuestion mData;

    @Override
    void setData(BaseQuestion data) {
        mData = (ReadingComplexQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mData = (ReadingComplexQuestion) savedInstanceState.getSerializable(ExerciseFragmentBase.KEY_NODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_reading_top, container, false);
//        initView();
        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseFragmentBase.KEY_NODE, mData);
    }

}
