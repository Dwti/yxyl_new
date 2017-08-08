package com.yanxiu.gphone.student.homework.classmanage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.classmanage.ClassStatus;
import com.yanxiu.gphone.student.homework.response.CancelApplyClassResponse;
import com.yanxiu.gphone.student.homework.request.CancelApplyClassRequest;
import com.yanxiu.gphone.student.homework.response.ClassBean;
import com.yanxiu.gphone.student.homework.request.ExitClassRequest;
import com.yanxiu.gphone.student.homework.response.ExitClassResponse;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;

/**
 * Created by sp on 17-5-18.
 */

public class ClassInfoActivity extends Activity {
    public static final String EXTRA_CLASS_INFO = "CLASS_INFO";
    public static final String EXTRA_STATUS = "CLASS_STATUS";
    private String mClassId;
    private int mStatus = -1;
    private boolean isRequesting = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        ClassBean classInfo = (ClassBean) getIntent().getSerializableExtra(EXTRA_CLASS_INFO);
        mStatus = getIntent().getIntExtra(EXTRA_STATUS,-1);
        initView(classInfo,mStatus);
    }

    private void initView(final ClassBean classInfo, final int status) {
        TextView title = (TextView) findViewById(R.id.tv_title);
        TextView className = (TextView) findViewById(R.id.tv_class_name);
        TextView classNum = (TextView) findViewById(R.id.tv_class_num);
        TextView teacherName = (TextView) findViewById(R.id.tv_teacher_name);
        TextView studentNum = (TextView) findViewById(R.id.tv_student_num);
        TextView schoolName = (TextView) findViewById(R.id.tv_school_name);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        ImageView back = (ImageView) findViewById(R.id.iv_left);

        back.setVisibility(View.VISIBLE);
        title.setText(R.string.class_info);

        if(classInfo != null){
            mClassId = classInfo.getId();
            className.setText(" • " + classInfo.getGradename()+classInfo.getName());
            classNum.setText(classInfo.getId());
            teacherName.setText(classInfo.getAdminName());
            studentNum.setText(String.format(getString(R.string.student_count),classInfo.getStdnum()));
            schoolName.setText(classInfo.getSchoolname());
        }
        if(status == ClassStatus.APPLYING_CLASS.getCode()){
            btnCancel.setText(R.string.cacel_apply_for_class);
        }else if(status == ClassStatus.HAS_CLASS.getCode()){
            btnCancel.setText(R.string.exit_class);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRequesting)
                    return;
                isRequesting = true;
                if(status == ClassStatus.HAS_CLASS.getCode()){
                    //退出班级
                    exitClass(mClassId);
                }else if(status == ClassStatus.APPLYING_CLASS.getCode()){
                    //取消申请加入班级
                    cacelApplyClass(mClassId);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void exitClass(String classId){
        ExitClassRequest request = new ExitClassRequest();
        request.setClassId(classId);
        request.startRequest(ExitClassResponse.class,mExitClassCallback);
    }

    private void cacelApplyClass(String classId){
        CancelApplyClassRequest request = new CancelApplyClassRequest();
        request.setClassId(classId);
        request.startRequest(CancelApplyClassResponse.class,mCancelApplyClassCallback);
    }

    HttpCallback<ExitClassResponse> mExitClassCallback = new EXueELianBaseCallback<ExitClassResponse>() {
        @Override
        public void onSuccess(RequestBase request, ExitClassResponse ret) {
            super.onSuccess(request, ret);
            isRequesting = false;
        }

        @Override
        public void onResponse(RequestBase request, ExitClassResponse ret) {
            if(ret.getStatus().getCode() == 0 ){
                setResult(RESULT_OK);
                finish();
            }
            Toast.makeText(ClassInfoActivity.this,ret.getStatus().getDesc(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            Toast.makeText(ClassInfoActivity.this, error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            isRequesting = false;
        }
    };

    HttpCallback<CancelApplyClassResponse> mCancelApplyClassCallback = new EXueELianBaseCallback<CancelApplyClassResponse>() {
        @Override
        public void onSuccess(RequestBase request, CancelApplyClassResponse ret) {
            super.onSuccess(request, ret);
            isRequesting = false;
        }

        @Override
        public void onResponse(RequestBase request, CancelApplyClassResponse ret) {
            if(ret.getStatus().getCode() == 0 ){
                setResult(RESULT_OK);
                finish();
            }
            Toast.makeText(ClassInfoActivity.this,ret.getStatus().getDesc(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            Toast.makeText(ClassInfoActivity.this, error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            isRequesting = false;
        }
    };
}
