package com.yanxiu.gphone.student.homework.questions.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyf
 */

public class QAViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseQuestion> mDatas = new ArrayList<>();

    public QAViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public QAViewPagerAdapter(FragmentManager fragmentManager, List<BaseQuestion> datas) {
        super(fragmentManager);
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
    }

    public void setData(List<BaseQuestion> datas) {
        this.mDatas = datas;
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


