package com.yanxiu.gphone.student.homework.classmanage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.data.ClassBean;
import com.yanxiu.gphone.student.homework.data.JoinClassRequest;
import com.yanxiu.gphone.student.homework.data.JoinClassResponse;
import com.yanxiu.gphone.student.homework.data.UpdateUserInfoRequest;
import com.yanxiu.gphone.student.homework.data.UpdateUserInfoResponse;
import com.yanxiu.gphone.student.base.ExerciseBaseCallback;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-5-17.
 */

public class JoinClassActivity extends Activity {

    public static final String EXTRA_CLASS_INFO = "CLASS_INFO";
    private String mName;
    private String mClassId;
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
        Button next = (Button) findViewById(R.id.btn_next);
        View back = findViewById(R.id.iv_left);

        back.setVisibility(View.VISIBLE);
        title.setText(R.string.join_class);
        name.setText(LoginInfo.getRealName());

        if(classInfo != null){
            mClassId = classInfo.getId();
            classNum.setText(classInfo.getId());
            teacherName.setText(classInfo.getAdminName());
            studentNum.setText(String.valueOf(classInfo.getStdnum()));
            schoolName.setText(classInfo.getSchoolname());
        }

        next.setOnClickListener(new View.OnClickListener() {
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
            if(ret.getStatus().getCode() == 0){
                updateUserInfo(mName);
            }
            Toast.makeText(JoinClassActivity.this,ret.getStatus().getDesc(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            Toast.makeText(JoinClassActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    HttpCallback<UpdateUserInfoResponse> mUpdateUserInfoCallback = new ExerciseBaseCallback<UpdateUserInfoResponse>() {
        @Override
        public void onSuccess(RequestBase request, UpdateUserInfoResponse ret) {
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            Toast.makeText(JoinClassActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    };
}
