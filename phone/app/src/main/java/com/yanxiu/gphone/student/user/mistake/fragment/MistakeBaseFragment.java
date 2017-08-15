package com.yanxiu.gphone.student.user.mistake.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.base.YanxiuBaseFragment;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.user.mistake.activity.MistakeClassifyActivity;
import com.yanxiu.gphone.student.user.mistake.response.MistakeDeleteMessage;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/17 10:44.
 * Function :
 */
public abstract class MistakeBaseFragment extends YanxiuBaseFragment {

    protected String mStageId;
    protected String mSubjectId;
    protected String mEditionId;
    protected int mWrongNum;

    protected Context mContext;
    protected PublicLoadLayout rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(MistakeBaseFragment.this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mStageId = bundle.getString(MistakeClassifyActivity.STAGEID, "");
            mSubjectId = bundle.getString(MistakeClassifyActivity.SUBJECTID, "");
            mEditionId = bundle.getString(MistakeClassifyActivity.EDITIONID, "");
            mWrongNum = bundle.getInt(MistakeClassifyActivity.WRONGNUM, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = inflater.getContext();
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(getContentViewId());
        rootView.setErrorLayoutFullScreen();
        initView();
        listener();
        initData();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requestCancle();
    }

    public void onEventMainThread(MistakeDeleteMessage message){
        onDeleteItem(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MistakeBaseFragment.this);
    }

    protected abstract int getContentViewId();

    protected abstract void initView();

    protected abstract void listener();

    protected abstract void initData();

    protected abstract void requestCancle();

    protected abstract void onDeleteItem(MistakeDeleteMessage message);
}
