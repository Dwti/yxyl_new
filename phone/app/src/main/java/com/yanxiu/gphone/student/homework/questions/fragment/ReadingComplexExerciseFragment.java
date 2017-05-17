package com.yanxiu.gphone.student.homework.questions.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;


/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class ReadingComplexExerciseFragment extends ComplexExerciseFragmentBase {
//    ReadingComplexQuestion model;

    @Override
    public void setNode(BaseQuestion node) {
        super.setNode(node);
//        model = (ReadingComplexQuestion) node;
    }

    @Override
    protected TopFragment getTopFragment() {
        ReadingComplexTopFragment topFragment = new ReadingComplexTopFragment();
//        topFragment.setNode(model);
        return topFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
//            model = (ListenComplexQuestion) savedInstanceState.getSerializable(KEY_NODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putSerializable(KEY_NODE, model);
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser                      true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param isHappenedInSetUserVisibleHintMethod true：本次回调发生在setUserVisibleHintMethod方法里；false：发生在onResume或onPause方法里
     */
    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod) {
        super.onVisibilityChangedToUser(isVisibleToUser,isHappenedInSetUserVisibleHintMethod);
    }
}
