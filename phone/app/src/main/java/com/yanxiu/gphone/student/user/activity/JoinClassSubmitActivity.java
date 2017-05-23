package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.util.EditTextManger;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/23 11:27.
 * Function :
 */
public class JoinClassSubmitActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private Context mContext;
    private PublicLoadLayout rootView;
    private TextView mClassIdView;
    private TextView mTeacherNameView;
    private TextView mStudentNumberView;
    private TextView mSchoolNameView;
    private EditText mInputNameView;
    private ImageView mWriteNameView;
    private WavesLayout mWavesView;
    private TextView mAddClassView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,JoinClassSubmitActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=JoinClassSubmitActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_join_class_submit);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mClassIdView= (TextView) findViewById(R.id.tv_class_id);
        mTeacherNameView= (TextView) findViewById(R.id.tv_teacher_name);
        mStudentNumberView= (TextView) findViewById(R.id.tv_student_number);
        mSchoolNameView= (TextView) findViewById(R.id.tv_school_name);
        mInputNameView= (EditText) findViewById(R.id.et_name);
        mWriteNameView= (ImageView) findViewById(R.id.iv_write);
        mWavesView= (WavesLayout) findViewById(R.id.wavesLayout);
        mAddClassView= (TextView) findViewById(R.id.tv_add_class);
    }

    private void listener() {
        EditTextManger.getManager(mInputNameView).setTextChangedListener(JoinClassSubmitActivity.this);
        mWriteNameView.setOnClickListener(JoinClassSubmitActivity.this);
        mAddClassView.setOnClickListener(JoinClassSubmitActivity.this);
    }

    private void initData() {
        mInputNameView.setEnabled(false);
        mWavesView.setCanShowWave(false);
        mAddClassView.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_write:
                mInputNameView.setEnabled(true);
                break;
            case R.id.tv_add_class:
                String userName=mInputNameView.getText().toString().trim();
                addClass(userName);
                break;
        }
    }

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        if (isEmpty){
            mWavesView.setCanShowWave(false);
            mAddClassView.setEnabled(false);
        }else {
            mWavesView.setCanShowWave(true);
            mAddClassView.setEnabled(true);
        }
    }

    private void addClass(String userName){

    }
}
