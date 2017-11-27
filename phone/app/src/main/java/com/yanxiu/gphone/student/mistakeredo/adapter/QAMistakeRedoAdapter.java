package com.yanxiu.gphone.student.mistakeredo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫
 * 答题adapter,是否需要基类，看以后需求而定
 */

public class QAMistakeRedoAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BaseQuestion> mDatas = new ArrayList<>();

    public QAMistakeRedoAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public QAMistakeRedoAdapter(FragmentManager fragmentManager, List<BaseQuestion> datas) {
        super(fragmentManager);
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
    }

    public void setData(ArrayList<BaseQuestion> datas) {
        if(datas == null){
            return;
        }
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<BaseQuestion> datas) {
        if(datas == null){
            return;
        }
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
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

}


