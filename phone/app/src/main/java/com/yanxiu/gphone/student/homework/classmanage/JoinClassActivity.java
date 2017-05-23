package com.yanxiu.gphone.student.homework.classmanage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.homework.data.ClassBean;
import com.yanxiu.gphone.student.homework.data.JoinClassRequest;
import com.yanxiu.gphone.student.homework.data.JoinClassResponse;
import com.yanxiu.gphone.student.homework.data.UpdateUserInfoRequest;
import com.yanxiu.gphone.student.homework.data.UpdateUserInfoResponse;
import com.yanxiu.gphone.student.base.ExerciseBaseCallback;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

/**
 * Created by sp on 17-5-17.
 */

public class JoinClassActivity extends Activity {

    public static final String EXTRA_CLASS_INFO = "CLASS_INFO";
    private String mName;
    private String mClassId;
    private Button mBtnNext;
    private WavesLayout mWavesLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);
        ClassBean classInfo = (ClassBean) getIntent().getSerializableExtra(EXTRA_CLASS_INFO);
        initView(classInfo);
    }
    private void initView(final ClassBean classInfo) {
        TextView title = (TextView) findViewById(R.id.tv_title);
        TextView classNum = (TextView) findViewById(R.id.tv_class_num);
        TextView teacherName = (TextView) findViewById(R.id.tv_teacher_name);
        TextView studentNum = (TextView) findViewById(R.id.tv_student_num);
        TextView schoolName = (TextView) findViewById(R.id.tv_school_name);
        final EditText name = (EditText) findViewById(R.id.et_name);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mWavesLayout = (WavesLayout)findViewById(R.id.wavesLayout);
        View back = findViewById(R.id.iv_left);

        back.setVisibility(View.VISIBLE);
        title.setText(R.string.join_class);
        name.setText(LoginInfo.getRealName());
        name.setSelection(LoginInfo.getRealName().length());
        if(TextUtils.isEmpty(LoginInfo.getRealName())){
            mWavesLayout.setCanShowWave(false);
            mBtnNext.setEnabled(false);
        }

        if(classInfo != null){
            mClassId = classInfo.getId();
            classNum.setText(classInfo.getId());
            teacherName.setText(classInfo.getAdminName());
            studentNum.setText(String.valueOf(classInfo.getStdnum()));
            schoolName.setText(classInfo.getSchoolname());
        }

        name.addTextChangedListener(new TextWatcher() {
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
                //先申请加入班级,成功后更新用户姓名信息
                mName = name.getText().toString();
                requestJoinClass(classInfo.getId(),mName);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
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

    HttpCallback<JoinClassResponse> mJoinClassCallback = new ExerciseBaseCallback<JoinClassResponse>() {
        @Override
        public void onSuccess(RequestBase request, JoinClassResponse ret) {
            super.onSuccess(request,ret);
            if(ret.getStatus().getCode() == 0){
                updateUserInfo(mName);
                ToastManager.showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };

    HttpCallback<UpdateUserInfoResponse> mUpdateUserInfoCallback = new ExerciseBaseCallback<UpdateUserInfoResponse>() {
        @Override
        public void onSuccess(RequestBase request, UpdateUserInfoResponse ret) {
            super.onSuccess(request,ret);
            if(ret.getStatus().getCode() == 0){
                setResult(RESULT_OK);
                finish();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };
}
