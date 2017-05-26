package com.yanxiu.gphone.student.base;


import android.support.v4.app.Fragment;

/**
 * 首页三个Fragment的基类
 */
public abstract class HomePageBaseFragment extends Fragment {
    private final static String TAG = HomePageBaseFragment.class.getSimpleName();

    /**
     * 请求数据
     */
    public abstract void requestData();
}
