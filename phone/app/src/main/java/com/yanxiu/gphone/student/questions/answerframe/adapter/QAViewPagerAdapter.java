package com.yanxiu.gphone.student.questions.answerframe.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫
 * 答题adapter,是否需要基类，看以后需求而定
 */

public class QAViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BaseQuestion> mDatas = new ArrayList<>();

    private boolean isHideBottomView=false;

    public QAViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public QAViewPagerAdapter(FragmentManager fragmentManager, List<BaseQuestion> datas) {
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
        if (fragment instanceof WrongSimpleExerciseBaseFragment){
            if (isHideBottomView){
                ((WrongSimpleExerciseBaseFragment)fragment).hideBottomView();
            }
        }
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void hideBottomView(){
        isHideBottomView=true;
    }

//    @Override
//    public Parcelable saveState() {
//        return null;
//    }
}


