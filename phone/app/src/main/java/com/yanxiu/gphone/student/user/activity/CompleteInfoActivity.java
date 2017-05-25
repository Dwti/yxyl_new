package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.homepage.MainActivity;
import com.yanxiu.gphone.student.user.http.CompleteInfoRequest;
import com.yanxiu.gphone.student.user.response.LoginResponse;
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

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, CompleteInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = CompleteInfoActivity.this;
        EventBus.getDefault().register(mContext);
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_completeinfo);
        rootView.finish();
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
    }

    private void initView() {
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
    }

    private void listener() {
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
            case R.id.iv_choose_stage:
                ChooseStageActivity.LaunchActivity(mContext);
                break;
            case R.id.iv_choose_school:
                ChooseLocationActivity.LaunchActivity(mContext);
                break;
            case R.id.tv_submit:
                String userName=mUserNameView.getText().toString().trim();
                submitInfo(userName);
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
        mCompleteInfoRequest.startRequest(LoginResponse.class, new HttpCallback<LoginResponse>() {
            @Override
            public void onSuccess(RequestBase request, LoginResponse ret) {
                rootView.hiddenLoadingView();
                if (ret.status.getCode()==0&&ret.data!=null){
                    LoginInfo.saveCacheData(ret.data.get(0));
                    MainActivity.invoke(CompleteInfoActivity.this);
                    CompleteInfoActivity.this.finish();
                }else {
                    ToastManager.showMsg(ret.status.getDesc());
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
