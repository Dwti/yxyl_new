package com.yanxiu.gphone.student.questions.answerframe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerStateChangedListener;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoSimpleExerciseBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫
 * 答题adapter,是否需要基类，看以后需求而定
 */

public class RedoComplexViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BaseQuestion> mDatas = new ArrayList<>();
    private OnAnswerStateChangedListener mAnswerStateChangedListener;
    public RedoComplexViewPagerAdapter(FragmentManager fragmentManager, OnAnswerStateChangedListener listener) {
        super(fragmentManager);
        this.mAnswerStateChangedListener = listener;
    }

    public RedoComplexViewPagerAdapter(FragmentManager fragmentManager, List<BaseQuestion> datas) {
        super(fragmentManager);
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
    }

    public void setData(ArrayList<BaseQuestion> datas) {
        this.mDatas = datas;
//        BaseQuestion.generateUsedNumbersForNodes(mDatas);
    }

    public List<BaseQuestion> getDatas() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        if(fragment instanceof RedoSimpleExerciseBaseFragment && mAnswerStateChangedListener != null){
            ((RedoSimpleExerciseBaseFragment)fragment).setAnswerStateChangedListener(mAnswerStateChangedListener);
        }
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


//    @Override
//    public Parcelable saveState() {
//        return null;
//    }
}


