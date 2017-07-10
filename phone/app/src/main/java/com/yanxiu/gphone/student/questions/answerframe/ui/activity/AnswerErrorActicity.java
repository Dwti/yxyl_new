package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.http.request.AnalysisErrorRequest;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;

/**
 * 题目报错页面
 * Created by 戴延枫 on 2017/7/5.
 */

public class AnswerErrorActicity extends YanxiuBaseActivity implements View.OnClickListener {

    private String mQid;
    private String mTag;
    private String mType;
    private String mContent;
    private View mTag1, mTag2, mTag3, mTag4, mTag5, mTag6;
    private EditText mError_content;
    private TextView mSubmit;
    private ArrayList<String> mTagList = new ArrayList<>(6);

    private AnalysisErrorRequest mRequest;
    private Handler mHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_error);
        initData();
        initView();
    }

    private void initData() {
        mQid = getIntent().getStringExtra(Constants.EXTRA_QID);

    }

    private void initView() {
        mHandler = new Handler();
        mTag1 = findViewById(R.id.error_1_layout);
        mTag2 = findViewById(R.id.error_2_layout);
        mTag3 = findViewById(R.id.error_3_layout);
        mTag4 = findViewById(R.id.error_4_layout);
        mTag5 = findViewById(R.id.error_5_layout);
        mTag6 = findViewById(R.id.error_6_layout);
        mError_content = (EditText) findViewById(R.id.error_content);
        mSubmit = (TextView) findViewById(R.id.submit);
        mSubmit.setEnabled(false);
        setListener();

    }

    private void setListener() {
        mTag1.setOnClickListener(this);
        mTag2.setOnClickListener(this);
        mTag3.setOnClickListener(this);
        mTag4.setOnClickListener(this);
        mTag5.setOnClickListener(this);
        mTag6.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mError_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mContent = mError_content.getText().toString();
                checkSubmitButtonState();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_1_layout:
                mTag = "1";
                addType(mTag, v);
                break;
            case R.id.error_2_layout:
                mTag = "2";
                addType(mTag, v);
                break;
            case R.id.error_3_layout:
                mTag = "3";
                addType(mTag, v);
                break;
            case R.id.error_4_layout:
                mTag = "4";
                addType(mTag, v);
                break;
            case R.id.error_5_layout:
                mTag = "5";
                addType(mTag, v);
                break;
            case R.id.error_6_layout:
                mTag = "6";
                addType(mTag, v);
                break;
            case R.id.submit:
                mContent = mError_content.getText().toString();
                if (TextUtils.isEmpty(mContent)) {
                    mContent = "";
                }
                if (mTagList != null && !mTagList.isEmpty()) {
                    for (int i = 0; i < mTagList.size(); i++) {
                        if (i < mTagList.size() - 1) {
                            //不是最后一个
                            mType += mTagList.get(i) + ",";
                        } else {
                            mType += mTagList.get(i);
                        }
                    }
                } else {
                    mType = "";
                }
                requestSubmit();
                break;

        }
    }

    private void addType(String tag, View view) {
        if (mTagList.contains(tag)) {
            view.setSelected(false);
            mTagList.remove(tag);
        } else {
            view.setSelected(true);
            mTagList.add(tag);
        }
        checkSubmitButtonState();
    }

    private void checkSubmitButtonState() {
        if (!mTagList.isEmpty() || !TextUtils.isEmpty(mError_content.getText().toString())) {
            mSubmit.setEnabled(true);
            mSubmit.setBackgroundResource(R.drawable.selector_answercard_submit_button_bg);
        } else {
            mSubmit.setEnabled(false);
            mSubmit.setBackgroundResource(R.drawable.answercard_submit_button_shape_disable);
        }

    }

    private void requestSubmit() {
        if (TextUtils.isEmpty(mQid)) {
            return;
        }
        if (mRequest != null) {
            mRequest.cancelRequest();
            mRequest = null;
        }
        mRequest = new AnalysisErrorRequest(mQid, mContent, mType);
        mRequest.bodyDealer = new DESBodyDealer();
        mRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {

            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                if (response != null && response.getStatus().getCode() == 0) {
                    mType = "";
                    ToastManager.showMsg(getString(R.string.analysis_error_submit_succes));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else {
                    mType = "";
                    ToastManager.showMsg(getString(R.string.analysis_error_submit_fail));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mType = "";
                ToastManager.showMsg(getString(R.string.analysis_error_submit_fail));
            }
        });
    }


    /**
     * AnswerErrorActicity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String qid) {
        Intent intent = new Intent(activity, AnswerErrorActicity.class);
        intent.putExtra(Constants.EXTRA_QID, qid);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mRequest != null) {
            mRequest.cancelRequest();
            mRequest = null;
        }
        super.onDestroy();
    }
}
