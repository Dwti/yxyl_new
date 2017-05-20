package com.yanxiu.gphone.student.homework.classmanage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.data.ClassBean;
import com.yanxiu.gphone.student.homework.data.JoinClassRequest;
import com.yanxiu.gphone.student.homework.data.JoinClassResponse;
import com.yanxiu.gphone.student.homework.data.UpdateUserInfoRequest;
import com.yanxiu.gphone.student.homework.data.UpdateUserInfoResponse;
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
        ClassBean bean = (ClassBean) getIntent().getSerializableExtra(EXTRA_CLASS_INFO);
        initView(bean);
    }
    private void initView(final ClassBean bean) {
        TextView classNum = (TextView) findViewById(R.id.tv_class_num);
        TextView teacherName = (TextView) findViewById(R.id.tv_teacher_name);
        TextView studentNum = (TextView) findViewById(R.id.tv_student_num);
        TextView schoolName = (TextView) findViewById(R.id.tv_school_name);
        final EditText name = (EditText) findViewById(R.id.et_name);
        Button next = (Button) findViewById(R.id.btn_next);
        name.setText(LoginInfo.getRealName());
        if(bean != null){
            mClassId = bean.getId();
            classNum.setText(bean.getId());
            teacherName.setText(bean.getAdminName());
            studentNum.setText(String.valueOf(bean.getStdnum()));
            schoolName.setText(bean.getSchoolname());
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先申请加入班级,成功后更新用户姓名信息
                mName = name.getText().toString();
                requestJoinClass(bean.getId());
            }
        });
    }

    private void updateUserInfo(String name) {
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setRealname(name);
        request.startRequest(UpdateUserInfoResponse.class,mUpdateUserInfoCallback);
    }

    private void requestJoinClass(String classId) {
        JoinClassRequest request = new JoinClassRequest();
        request.setClassId(classId);
        request.setValidMsg("");
        request.startRequest(JoinClassResponse.class,mJoinClassCallback);
    }

    HttpCallback<JoinClassResponse> mJoinClassCallback = new HttpCallback<JoinClassResponse>() {
        @Override
        public void onSuccess(RequestBase request, JoinClassResponse ret) {
            if(ret.getStatus().getCode() == 0){
                updateUserInfo(mName);
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {

        }
    };

    HttpCallback<UpdateUserInfoResponse> mUpdateUserInfoCallback = new HttpCallback<UpdateUserInfoResponse>() {
        @Override
        public void onSuccess(RequestBase request, UpdateUserInfoResponse ret) {
            //TODO 跳转到取消加入申请界面
        }

        @Override
        public void onFail(RequestBase request, Error error) {

        }
    };
}
