package com.yanxiu.gphone.student.questions.readingcomplex;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.TopBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongComplexExerciseBaseFragment;


/**
 * Created by 戴延枫 on 2017/6/29.
 */

public class ReadingWrongComplexFragment extends WrongComplexExerciseBaseFragment {
    private ReadingComplexQuestion mData;

    @Override
    public void setData(BaseQuestion baseQuestion) {
        super.setData(baseQuestion);
        mData = (ReadingComplexQuestion) baseQuestion;
    }

    @Override
    protected TopBaseFragment getTopFragment() {
        ReadingWrongComplexTopFragment topFragment = new ReadingWrongComplexTopFragment();
        topFragment.setData(mData);
        return topFragment;
    }

    @Override
    protected void setTopFragment(Fragment fragment) {
        super.setTopFragment(fragment);
        ((ReadingWrongComplexTopFragment)fragment).setData(mData);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null&&mData==null) {
            setData((ReadingComplexQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser       true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param invokeInResumeOrPause true：发生在onResume或onPause方法里；false：本次回调发生在setUserVisibleHintMethod方法里
     */
    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser,invokeInResumeOrPause);
    }
}
