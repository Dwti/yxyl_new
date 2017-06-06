package com.yanxiu.gphone.student.questions.answerframe.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public abstract class TopBaseFragment extends Fragment {
    public abstract void setData(BaseQuestion data);
    public View mRootView;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}