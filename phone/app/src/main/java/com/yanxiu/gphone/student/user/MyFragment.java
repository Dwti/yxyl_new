package com.yanxiu.gphone.student.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.user.mistake.activity.MistakeListActivity;
import com.yanxiu.gphone.student.login.activity.LoginActivity;
import com.yanxiu.gphone.student.user.setting.activity.SettingActivity;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;


/**
 * 首页"我的"Fragment
 */
public class MyFragment extends HomePageBaseFragment implements View.OnClickListener {
    private static final String TAG = MyFragment.class.getSimpleName();
    private View mRootView;
    private ImageView mEdit_userinfo;
    private View mMy_mistake, mExrcise_history, mXueduan, mTeaching_material_version, mFeedback, mSeeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        mEdit_userinfo = (ImageView) mRootView.findViewById(R.id.user_edit_userinfo);
        mMy_mistake = mRootView.findViewById(R.id.user_my_mistake);
        mExrcise_history = mRootView.findViewById(R.id.user_exrcise_history);
        mXueduan = mRootView.findViewById(R.id.user_xueduan);
        mTeaching_material_version = mRootView.findViewById(R.id.user_teaching_material_version);
        mFeedback = mRootView.findViewById(R.id.user_feedback);
        mSeeting = mRootView.findViewById(R.id.user_seeting);
        initListener();
    }

    private void initListener() {
        mRootView.findViewById(R.id.logout).setOnClickListener(this);
        mEdit_userinfo.setOnClickListener(this);
        mMy_mistake.setOnClickListener(this);
        mExrcise_history.setOnClickListener(this);
        mXueduan.setOnClickListener(this);
        mTeaching_material_version.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mSeeting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                LoginInfo.LogOut();
                LoginActivity.LaunchActivity(getActivity());
                break;
            case R.id.user_edit_userinfo:
                ToastManager.showMsg("编辑用户信息");
                break;
            case R.id.user_my_mistake:
                MistakeListActivity.LuanchActivity(getContext());
                break;
            case R.id.user_exrcise_history:
                ToastManager.showMsg("练习历史");
                break;
            case R.id.user_xueduan:
                ToastManager.showMsg("学段");
                break;
            case R.id.user_teaching_material_version:
                ToastManager.showMsg("教材版本");
                break;
            case R.id.user_feedback:
                ToastManager.showMsg("反馈");
                break;
            case R.id.user_seeting:
                SettingActivity.LaunchActivity(getContext());
                break;
        }
    }
}
