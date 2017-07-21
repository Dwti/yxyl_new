package com.yanxiu.gphone.student.questions.classify;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.ArrayList;

/**
 * 归类题的数据封装类
 * Created by 戴延枫 on 2017/7/13.
 */

public class ClassifyBean extends BaseBean {
    private String mTitle;

    private ArrayList<ClassifyItemBean> mClassifyBeanArrayList;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public ArrayList<ClassifyItemBean> getClassifyBeanArrayList() {
        return mClassifyBeanArrayList;
    }

    public void setClassifyBeanArrayList(ArrayList<ClassifyItemBean> mClassifyBeanArrayList) {
        this.mClassifyBeanArrayList = mClassifyBeanArrayList;
    }
}
