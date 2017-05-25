package com.yanxiu.gphone.student.homework.questions.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public abstract class TopFragment extends Fragment {
    abstract void setData(BaseQuestion data);
    public View mRootView;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}