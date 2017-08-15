package com.yanxiu.gphone.student.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.srt.refresh.BaseRefreshLayout2;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.exercise.SubjectHistoryActivity;
import com.yanxiu.gphone.student.login.activity.ChooseStageActivity;
import com.yanxiu.gphone.student.user.feedback.activity.FeedbackActivity;
import com.yanxiu.gphone.student.exercise.SelectSubjectActivity;
import com.yanxiu.gphone.student.user.mistake.activity.MistakeListActivity;
import com.yanxiu.gphone.student.user.setting.activity.SettingActivity;
import com.yanxiu.gphone.student.user.userinfo.activity.UserInfoActivity;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

import de.greenrobot.event.EventBus;


/**
 * 首页"我的"Fragment
 */
public class MyFragment extends HomePageBaseFragment implements View.OnClickListener {
    private static final String TAG = MyFragment.class.getSimpleName();
    private View mRootView;
    private View mMy_mistake;//错题
    private View mExrcise_history;//练习历史
    private View mXueduan;//学段
    private View mTeaching_material_version;//教材版本
    private View mFeedback;//反馈
    private View mSetting;//设置

    private ImageView mEdit_userinfo;//编辑用户信息
    private ImageView mUser_icon;//头像
    private TextView mUser_name;//用户名
    private TextView mUser_id;//账号
    private TextView mStage;//学段

    private BaseRefreshLayout2 mRefreshLayout;

    private View mHead_layout,mFly_icon,mHill_icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        return mRootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(MyFragment.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MyFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        intData();
    }

    private void initView() {
        mEdit_userinfo = (ImageView) mRootView.findViewById(R.id.user_edit_userinfo);
        mUser_icon = (ImageView) mRootView.findViewById(R.id.user_icon);
        mUser_name = (TextView) mRootView.findViewById(R.id.user_name);
        mUser_id = (TextView) mRootView.findViewById(R.id.user_id);
        mStage = (TextView) mRootView.findViewById(R.id.stage);
        mMy_mistake = mRootView.findViewById(R.id.user_my_mistake);
        mExrcise_history = mRootView.findViewById(R.id.user_exrcise_history);
        mXueduan = mRootView.findViewById(R.id.user_xueduan);
        mTeaching_material_version = mRootView.findViewById(R.id.user_teaching_material_version);
        mFeedback = mRootView.findViewById(R.id.user_feedback);
        mSetting = mRootView.findViewById(R.id.user_seeting);
        initListener();

        mRefreshLayout = (BaseRefreshLayout2) mRootView.findViewById(R.id.refreshLayout);
        mHead_layout = mRootView.findViewById(R.id.head_layout);
        mFly_icon = mRootView.findViewById(R.id.fly_icon);
        mHill_icon = mRootView.findViewById(R.id.user_hill);
        mRefreshLayout.setHeadLayout(mHead_layout);
        mRefreshLayout.setFlyView(mFly_icon);
        mRefreshLayout.setHillView(mHill_icon);
        mRefreshLayout.setHeaderHeight(200);
    }

    private void intData() {
        String headIconPath = LoginInfo.getHeadIcon();
        String userName = LoginInfo.getRealName();
        String loginName=LoginInfo.getLoginName();
        String stageName = LoginInfo.getStageName();
        Glide.with(getActivity()).load(headIconPath).asBitmap().into(new CircleImageTarget(mUser_icon));
        mUser_name.setText(userName);
        if (TextUtils.isEmpty(loginName)){
            loginName = LoginInfo.getMobile();
        }
        mUser_id.setText(loginName);
        TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_MEDIUM_PLAY,mUser_id);
        mStage.setText(stageName);
    }

    private void initListener() {
        mEdit_userinfo.setOnClickListener(this);
        mMy_mistake.setOnClickListener(this);
        mExrcise_history.setOnClickListener(this);
        mXueduan.setOnClickListener(this);
        mTeaching_material_version.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mSetting.setOnClickListener(this);
    }

    /**
     * 用户圆形头像
     */
    private class CircleImageTarget extends BitmapImageViewTarget {

        CircleImageTarget(ImageView view) {
            super(view);
        }

        @Override
        protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            view.setImageDrawable(circularBitmapDrawable);
        }
    }

    public void onEventMainThread(ChooseStageActivity.StageMessage message){
        if (message!=null&&message.requestCode==MyFragment.this.hashCode()){
            mStage.setText(message.stageText);
            LoginInfo.saveStageid(message.stageId);
            LoginInfo.saveStageName(message.stageText);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_edit_userinfo:
                UserInfoActivity.LaunchActivity(getContext());
                break;
            case R.id.user_my_mistake:
                MistakeListActivity.LuanchActivity(getContext());
                break;
            case R.id.user_exrcise_history:
                SubjectHistoryActivity.invoke(getContext());
                break;
            case R.id.user_xueduan:
                ChooseStageActivity.LaunchActivity(getContext(),this.hashCode());
                break;
            case R.id.user_teaching_material_version:
                SelectSubjectActivity.invoke(getActivity());
                break;
            case R.id.user_feedback:
                FeedbackActivity.LaunchActivity(getContext());
                break;
            case R.id.user_seeting:
                SettingActivity.LaunchActivity(getContext());
                break;
        }
    }
}
