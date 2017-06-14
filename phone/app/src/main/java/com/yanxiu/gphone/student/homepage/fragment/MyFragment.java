package com.yanxiu.gphone.student.homepage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.user.activity.LoginActivity;
import com.yanxiu.gphone.student.util.LoginInfo;


/**
 * 首页"我的"Fragment
 */
public class MyFragment extends HomePageBaseFragment implements View.OnClickListener {
    private static final String TAG = MyFragment.class.getSimpleName();
    private TextView mLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        mLogOut = (TextView) view.findViewById(R.id.logout);
        mLogOut.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                LoginInfo.LogOut();
                LoginActivity.LaunchActivity(getActivity());
//                getActivity().finish();
                break;
        }
    }

    /**
     * 请求数据
     */
    @Override
    public void requestData() {
        Log.e("dyf",TAG);
    }
}
