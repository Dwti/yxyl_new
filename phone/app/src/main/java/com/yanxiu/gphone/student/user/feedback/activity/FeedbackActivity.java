package com.yanxiu.gphone.student.user.feedback.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.user.feedback.request.FeedbackRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.ToastManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/31 9:23.
 * Function :
 */
public class FeedbackActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private EditText mFeedContentView;
    private WavesLayout mWavesView;
    private TextView mSendView;
    private FeedbackRequest mFeedbackRequest;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = FeedbackActivity.this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_feedback);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFeedbackRequest!=null){
            mFeedbackRequest.cancelRequest();
            mFeedbackRequest=null;
        }
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);

        mFeedContentView = (EditText) findViewById(R.id.et_feed);
        mWavesView = (WavesLayout) findViewById(R.id.wl_waves);
        mSendView = (TextView) findViewById(R.id.tv_send);
    }

    private void listener() {
        mBackView.setOnClickListener(FeedbackActivity.this);
        mWavesView.setOnClickListener(FeedbackActivity.this);

        EditTextManger.getManager(mFeedContentView).setTextChangedListener(FeedbackActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.feedback);
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));

        mSendView.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                FeedbackActivity.this.finish();
                EditTextManger.getManager(mTitleView).hideSoftInput();
                break;
            case R.id.wl_waves:
                String feedContent = mFeedContentView.getText().toString().trim();

                if (feedContent.length() < 2) {
                    ToastManager.showMsg(R.string.feed_send_error);
                    return;
                }

                sendFeed(feedContent);
                break;
        }
    }

    private void sendFeed(String feedContent) {
        rootView.showLoadingView();
        mFeedbackRequest = new FeedbackRequest();
        mFeedbackRequest.content = feedContent;
        mFeedbackRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    ToastManager.showMsg(R.string.feed_send_success);
                    FeedbackActivity.this.finish();
                } else {
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
        setSendButtonFocusChange(!isEmpty);
    }

    private void setSendButtonFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mSendView.setEnabled(true);
            mWavesView.setCanShowWave(true);
        } else {
            mSendView.setEnabled(false);
            mWavesView.setCanShowWave(false);
        }
    }
}
