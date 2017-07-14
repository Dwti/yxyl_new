package com.yanxiu.gphone.student.mistake.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseFragment;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 17:09.
 * Function :
 */
public class MistakeKongledgeFragment extends YanxiuBaseFragment {

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=inflater.getContext();
        PublicLoadLayout rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.fragment_mistakekongledge);
        return rootView;
    }
}
