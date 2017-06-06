package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.ExerciseBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.homepage.MainActivity;
import com.yanxiu.gphone.student.user.request.CompleteInfoRequest;
import com.yanxiu.gphone.student.user.request.CompleteInfoThridRequest;
import com.yanxiu.gphone.student.user.response.LoginResponse;
import com.yanxiu.gphone.student.user.response.ThridMessage;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.SysEncryptUtil;
import com.yanxiu.gphone.student.util.ToastManager;

import java.io.Serializable;

import de.greenrobot.event.EventBus;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 16:47.
 * Function :
 */
public class CompleteInfoActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private Context mContext;
    private EditText mUserNameView;
    private TextView mSchoolView;
    private TextView mStageView;
    private TextView mSubmitView;
    private WavesLayout mWavesView;
    private ImageView mChooseStageView;
    private ImageView mChooseSchoolView;

    /**
     * the default they are empty
     */
    private boolean isUserNameReady = false;
    private boolean isSchoolReady = false;
    private boolean isStageReady = false;
    private PublicLoadLayout rootView;

    /**
     * stage message
     * */
    public String stageText;
    public String stageId;
    public SchoolMessage message;
    private CompleteInfoRequest mCompleteInfoRequest;
    private ThridMessage thridMessage;
    private CompleteInfoThridRequest mCompleteInfoThridRequest;
    private ImageView mBackView;
    private TextView mTitleView;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, CompleteInfoActivity.class);
        intent.putExtra(LoginActivity.TYPE,LoginActivity.TYPE_DEFAULT);
        context.startActivity(intent);
    }

    public static void LaunchActivity(Context context, ThridMessage message){
        Intent intent=new Intent(context,CompleteInfoActivity.class);
        intent.putExtra(LoginActivity.TYPE,LoginActivity.TYPE_THRID);
        intent.putExtra(LoginActivity.THRID_LOGIN,message);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = CompleteInfoActivity.this;
        EventBus.getDefault().register(mContext);
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_completeinfo);
        String type=getIntent().getStringExtra(LoginActivity.TYPE);
        if (type.equals(LoginActivity.TYPE_THRID)) {
            thridMessage = (ThridMessage) getIntent().getSerializableExtra(LoginActivity.THRID_LOGIN);
        }
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
        if (mCompleteInfoRequest!=null){
            mCompleteInfoRequest.cancelRequest();
            mCompleteInfoRequest=null;
        }
        if (mCompleteInfoThridRequest!=null){
            mCompleteInfoThridRequest.cancelRequest();
            mCompleteInfoThridRequest=null;
        }
    }

    private void initView() {
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        mUserNameView = (EditText) findViewById(R.id.ed_user_name);
        mSchoolView = (TextView) findViewById(R.id.tv_school);
        mStageView = (TextView) findViewById(R.id.tv_stage);
        mSubmitView = (TextView) findViewById(R.id.tv_submit);
        mWavesView = (WavesLayout) findViewById(R.id.wl_waves);
        mChooseSchoolView = (ImageView) findViewById(R.id.iv_choose_school);
        mChooseStageView = (ImageView) findViewById(R.id.iv_choose_stage);
    }

    private void initData() {
        mWavesView.setCanShowWave(false);
        mSubmitView.setEnabled(false);
        mTitleView.setText(R.string.complete_message);
        mBackView.setVisibility(View.VISIBLE);
    }

    private void listener() {
        mBackView.setOnClickListener(CompleteInfoActivity.this);
        mSubmitView.setOnClickListener(CompleteInfoActivity.this);
        mChooseSchoolView.setOnClickListener(CompleteInfoActivity.this);
        mChooseStageView.setOnClickListener(CompleteInfoActivity.this);
        EditTextManger.getManager(mUserNameView).setTextChangedListener(CompleteInfoActivity.this);
        EditTextManger.getManager(mSchoolView).setTextChangedListener(CompleteInfoActivity.this);
        EditTextManger.getManager(mStageView).setTextChangedListener(CompleteInfoActivity.this);
    }

    private void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mWavesView.setCanShowWave(true);
            mSubmitView.setEnabled(true);
        } else {
            mWavesView.setCanShowWave(false);
            mSubmitView.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                CompleteInfoActivity.this.finish();
                EditTextManger.getManager(mTitleView).hideSoftInput(mContext);
                break;
            case R.id.iv_choose_stage:
                ChooseStageActivity.LaunchActivity(mContext);
                break;
            case R.id.iv_choose_school:
                ChooseLocationActivity.LaunchActivity(mContext);
                break;
            case R.id.tv_submit:
                String userName=mUserNameView.getText().toString().trim();
                if (thridMessage!=null){
                    submitInfoThrid(userName);
                }else {
                    submitInfo(userName);
                }
                break;
        }
    }

    private void submitInfo(String userName){
        rootView.showLoadingView();
        mCompleteInfoRequest=new CompleteInfoRequest();
        mCompleteInfoRequest.mobile= LoginInfo.getMobile();
        mCompleteInfoRequest.realname=userName;
        mCompleteInfoRequest.provinceid=message.provinceId;
        mCompleteInfoRequest.cityid=message.cityId;
        mCompleteInfoRequest.areaid=message.areaId;
        mCompleteInfoRequest.schoolid=message.schoolId;
        mCompleteInfoRequest.stageid=stageId;
        mCompleteInfoRequest.schoolName=message.schoolName;
        mCompleteInfoRequest.validKey= SysEncryptUtil.getMd5_32(LoginInfo.getMobile() + "&" + "yxylmobile");
        mCompleteInfoRequest.startRequest(LoginResponse.class, new ExerciseBaseCallback<LoginResponse>() {

            @Override
            protected void onResponse(RequestBase request, LoginResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0&&response.data!=null){
                    LoginInfo.saveCacheData(response.data.get(0));
                    MainActivity.invoke(CompleteInfoActivity.this,true);
                    CompleteInfoActivity.this.finish();
                }else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    private void submitInfoThrid(String userName){
        rootView.showLoadingView();
        mCompleteInfoThridRequest=new CompleteInfoThridRequest();
        mCompleteInfoThridRequest.headimg=thridMessage.head;
        mCompleteInfoThridRequest.openid=thridMessage.openid;
        mCompleteInfoThridRequest.pltform=thridMessage.platform;
        mCompleteInfoThridRequest.sex=thridMessage.sex;
        mCompleteInfoThridRequest.uniqid=thridMessage.uniqid;
        mCompleteInfoThridRequest.realname=userName;
        mCompleteInfoThridRequest.provinceid=message.provinceId;
        mCompleteInfoThridRequest.cityid=message.cityId;
        mCompleteInfoThridRequest.areaid=message.areaId;
        mCompleteInfoThridRequest.schoolid=message.schoolId;
        mCompleteInfoThridRequest.stageid=stageId;
        mCompleteInfoThridRequest.schoolName=message.schoolName;
        mCompleteInfoThridRequest.startRequest(LoginResponse.class, new ExerciseBaseCallback<LoginResponse>() {

            @Override
            protected void onResponse(RequestBase request, LoginResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0&&response.data!=null){
                    LoginInfo.saveCacheData(response.data.get(0));
                    MainActivity.invoke(CompleteInfoActivity.this,true);
                    CompleteInfoActivity.this.finish();
                }else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        if (view==mUserNameView){
            if (isEmpty) {
                isUserNameReady = false;
            } else {
                isUserNameReady = true;
            }
        }else if (view==mSchoolView){
            if (isEmpty) {
                isSchoolReady = false;
            } else {
                isSchoolReady = true;
            }
        }else if (view==mStageView){
            if (isEmpty) {
                isStageReady = false;
            } else {
                isStageReady = true;
            }
        }
        setButtonFocusChange(isUserNameReady && isSchoolReady && isStageReady);
    }

    public void onEventMainThread(StageMessage message){
        if (message!=null){
            mStageView.setText(message.stageText);
            stageText=message.stageText;
            stageId=message.stageId;
        }
    }

    public static class StageMessage{
        public String stageText;
        public String stageId;
    }

    public void onEventMainThread(SchoolMessage message) {
        if (message!=null) {
            this.message=message;
            mSchoolView.setText(message.schoolName);
        }
    }

    public static class SchoolMessage implements Serializable{
        public String provinceId;
        public String provinceName;

        public String cityId;
        public String cityName;

        public String areaId;
        public String areaName;

        public String schoolId;
        public String schoolName;
    }

}
