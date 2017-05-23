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
    private TextView mPeriodView;
    private TextView mSubmitView;
    private WavesLayout mWavesView;
    private ImageView mChoosePeriodView;
    private ImageView mChooseSchoolView;

    /**
     * the default they are empty
     */
    private boolean isUserNameReady = false;
    private boolean isSchoolReady = false;
    private boolean isPeriodReady = false;
    private PublicLoadLayout rootView;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, CompleteInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = CompleteInfoActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_completeinfo);
        rootView.finish();
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mUserNameView = (EditText) findViewById(R.id.ed_user_name);
        mSchoolView = (TextView) findViewById(R.id.tv_school);
        mPeriodView = (TextView) findViewById(R.id.tv_period);
        mSubmitView = (TextView) findViewById(R.id.tv_submit);
        mWavesView = (WavesLayout) findViewById(R.id.wl_waves);
        mChooseSchoolView = (ImageView) findViewById(R.id.iv_choose_school);
        mChoosePeriodView = (ImageView) findViewById(R.id.iv_choose_period);
    }

    private void initData() {
        mWavesView.setCanShowWave(false);
        mSubmitView.setEnabled(false);
    }

    private void listener() {
        mSubmitView.setOnClickListener(CompleteInfoActivity.this);
        mChooseSchoolView.setOnClickListener(CompleteInfoActivity.this);
        mChoosePeriodView.setOnClickListener(CompleteInfoActivity.this);
        EditTextManger.getManager(mUserNameView).setTextChangedListener(CompleteInfoActivity.this);
        EditTextManger.getManager(mSchoolView).setTextChangedListener(CompleteInfoActivity.this);
        EditTextManger.getManager(mPeriodView).setTextChangedListener(CompleteInfoActivity.this);
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
            case R.id.iv_choose_period:
                break;
            case R.id.iv_choose_school:
                ChooseLocationActivity.LaunchActivity(mContext);
                break;
            case R.id.tv_submit:
                submitInfo();
                break;
        }
    }

    private void submitInfo(){
        rootView.showLoadingView();
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
        }else if (view==mPeriodView){
            if (isEmpty) {
                isPeriodReady = false;
            } else {
                isPeriodReady = true;
            }
        }
        setButtonFocusChange(isUserNameReady && isSchoolReady && isPeriodReady);
    }
}
