package com.yanxiu.gphone.student.homework.classmanage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.homework.response.ClassBean;
import com.yanxiu.gphone.student.homework.request.JoinClassRequest;
import com.yanxiu.gphone.student.homework.response.JoinClassResponse;
import com.yanxiu.gphone.student.homework.request.UpdateUserInfoRequest;
import com.yanxiu.gphone.student.homework.response.UpdateUserInfoResponse;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.login.activity.ChooseStageActivity;
import com.yanxiu.gphone.student.userevent.UserEventManager;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import de.greenrobot.event.EventBus;

/**
 * Created by sp on 17-5-17.
 */

public class JoinClassActivity extends YanxiuBaseActivity {

    public static final String EXTRA_CLASS_INFO = "CLASS_INFO";

    private int mClassStatus = -1;

    public static int ALLOW_TO_JOIN = 0;  //班级开放加入
    public static int NEED_VERIFY = 1;    //需要审核

    private ScrollView mScrollView;

    private String mName,mClassId;

    private Button mBtnNext;

    private WavesLayout mWavesLayout;

    private View mBack;

    private EditText mEditName;

    private ImageView mIvEdit;

    private boolean isRequesting = false;
    private ClassBean classInfo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);
        classInfo = (ClassBean) getIntent().getSerializableExtra(EXTRA_CLASS_INFO);
        mClassStatus = classInfo.getStatus();
        initView();
        initListener();
    }
    private void initView() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        TextView className = (TextView) findViewById(R.id.tv_class_name);
        TextView classNum = (TextView) findViewById(R.id.tv_class_num);
        TextView teacherName = (TextView) findViewById(R.id.tv_teacher_name);
        TextView studentNum = (TextView) findViewById(R.id.tv_student_num);
        TextView schoolName = (TextView) findViewById(R.id.tv_school_name);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mIvEdit = (ImageView) findViewById(R.id.iv_edit_name);
        mEditName = (EditText) findViewById(R.id.et_name);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mWavesLayout = (WavesLayout)findViewById(R.id.wavesLayout);
        mBack = findViewById(R.id.iv_left);

        mBack.setVisibility(View.VISIBLE);
        mBack.setBackgroundResource(R.drawable.selector_white_back);
        title.setText(R.string.class_info);
        mEditName.setText(LoginInfo.getRealName());
        mEditName.setSelection(LoginInfo.getRealName().length());
        if(TextUtils.isEmpty(LoginInfo.getRealName())){
            mWavesLayout.setCanShowWave(false);
            mBtnNext.setEnabled(false);
        }

        if(classInfo != null){
            mClassId = classInfo.getId();
            className.setText(" • " + classInfo.getGradename()+classInfo.getName());
            classNum.setText(classInfo.getId());
            teacherName.setText(classInfo.getAdminName());
            studentNum.setText(String.format(getString(R.string.student_count),classInfo.getStdnum()));
            schoolName.setText(classInfo.getSchoolname());
        }
    }

    private void initListener(){
        mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString().trim())){
                    mBtnNext.setEnabled(true);
                    mWavesLayout.setCanShowWave(true);
                }else {
                    mBtnNext.setEnabled(false);
                    mWavesLayout.setCanShowWave(false);
                }
            }
        });
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRequesting)
                    return;
                isRequesting = true;
                //先申请加入班级,成功后更新用户姓名信息
                mName = mEditName.getText().toString();
                requestJoinClass(mClassId,mName);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateUserInfo(String name) {
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setRealname(name);
        request.startRequest(UpdateUserInfoResponse.class,mUpdateUserInfoCallback);
    }

    private void requestJoinClass(String classId,String realName) {
        JoinClassRequest request = new JoinClassRequest();
        request.setClassId(classId);
        request.setValidMsg(realName);
        request.startRequest(JoinClassResponse.class,mJoinClassCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    HttpCallback<JoinClassResponse> mJoinClassCallback = new EXueELianBaseCallback<JoinClassResponse>() {
        @Override
        public void onSuccess(RequestBase request, JoinClassResponse ret) {
            super.onSuccess(request, ret);
            isRequesting = false;
        }

        @Override
        public void onResponse(RequestBase request, JoinClassResponse ret) {
            if(ret.getStatus().getCode() == 0){
                updateUserInfo(mName);
                saveStageMessage(String.valueOf(classInfo.getStageid()));
                if(mClassStatus == ALLOW_TO_JOIN){
                    ToastManager.showMsg(getString(R.string.join_class_success));
                }else if(mClassStatus == NEED_VERIFY){
                    ToastManager.showMsg(getString(R.string.need_verify));
                }
                UserEventManager.getInstense().whenEnterClass();
            }else {
                ToastManager.showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
            isRequesting = false;
        }
    };

    private void saveStageMessage(String stage_Id){
        String[] stageIds= Constants.StageId;
        int[] stageNames=Constants.StageTxtId;
        for (int i=0;i<stageIds.length;i++){
            String stageId=stageIds[i];
            int stageNameId=stageNames[i];
            if (stageId.equals(stage_Id)){
                LoginInfo.saveStageid(stage_Id);
                LoginInfo.saveStageName(getText(stageNameId).toString());

                ChooseStageActivity.StageMessage message = new ChooseStageActivity.StageMessage();
                message.requestCode = 0;
                message.stageId = stageId;
                message.stageText = getText(stageNameId).toString();
                EventBus.getDefault().post(message);
                return;
            }
        }
    }

    HttpCallback<UpdateUserInfoResponse> mUpdateUserInfoCallback = new EXueELianBaseCallback<UpdateUserInfoResponse>() {
        @Override
        public void onResponse(RequestBase request, UpdateUserInfoResponse ret) {
            if(ret.getStatus().getCode() == 0 || ret.getStatus().getCode() ==2){
                LoginInfo.saveRealName(mName);
                UserNameChangeMsg msg = new UserNameChangeMsg();
                msg.name = mName;
                EventBus.getDefault().post(msg);
                setResult(RESULT_OK);
                finish();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };

    public static class UserNameChangeMsg{
        public String name;
    }
}
