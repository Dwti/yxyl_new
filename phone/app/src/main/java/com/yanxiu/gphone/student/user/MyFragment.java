package com.yanxiu.gphone.student.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.login.activity.ChooseStageActivity;
import com.yanxiu.gphone.student.user.feedback.activity.FeedbackActivity;
import com.yanxiu.gphone.student.exercise.SelectSubjectActivity;
import com.yanxiu.gphone.student.user.mistake.activity.MistakeListActivity;
import com.yanxiu.gphone.student.user.setting.activity.SettingActivity;
import com.yanxiu.gphone.student.user.userinfo.activity.UserInfoActivity;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;
import com.yanxiu.gphone.student.util.ToastManager;

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

    private String mHeadIconPath;//头像
    private String mUserName;//用户名字
    private String mMobile;//账号（手机号）
    private String mStageName;//学段

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
    }

    private void intData() {
        mHeadIconPath = LoginInfo.getHeadIcon();
        mUserName = LoginInfo.getRealName();
        mMobile = LoginInfo.getMobile();
        mStageName = LoginInfo.getStageName();
        Glide.with(getActivity()).load(mHeadIconPath).asBitmap().into(new CircleImageTarget(mUser_icon));
        mUser_name.setText(mUserName);
        mUser_id.setText(mMobile);
        TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_MEDIUM_PLAY,mUser_id);
        mStage.setText(mStageName);
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
            mStageName = message.stageText;
            mStage.setText(mStageName);
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
                ToastManager.showMsg("练习历史");
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
