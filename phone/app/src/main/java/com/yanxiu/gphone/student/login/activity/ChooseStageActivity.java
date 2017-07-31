package com.yanxiu.gphone.student.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PickerView;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.login.dialog.StageDialog;
import com.yanxiu.gphone.student.login.request.ChooseStageRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/24 14:13.
 * Function :
 */
public class ChooseStageActivity extends YanxiuBaseActivity implements PickerView.onSelectListener, View.OnClickListener {

    private static final String REQUESTCODE = "requestCode";

    private Context mContext;
    private PickerView mChooseStageView;

    private String[] stageIds = Constants.StageId;

    private String stageText;
    private String stageId;
    private TextView mConfirmView;
    private ImageView mBackView;
    private View mTopView;
    private TextView mTitleView;
    private PublicLoadLayout rootView;
    private int mRequestCode = -1;

    private ChooseStageRequest mChooseStageRequest;
    private StageDialog mStageDialog;

    public static void LaunchActivity(Context context, int requestCode) {
        Intent intent = new Intent(context, ChooseStageActivity.class);
        intent.putExtra(REQUESTCODE, requestCode);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ChooseStageActivity.this;
        mRequestCode = getIntent().getIntExtra(REQUESTCODE, -1);
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_choosestage);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChooseStageRequest != null) {
            mChooseStageRequest.cancelRequest();
            mChooseStageRequest = null;
        }
    }

    private void initView() {
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mConfirmView = (TextView) findViewById(R.id.tv_right);
        mChooseStageView = (PickerView) findViewById(R.id.pv_stage);
    }

    private void listener() {
        mBackView.setOnClickListener(ChooseStageActivity.this);
        mConfirmView.setOnClickListener(ChooseStageActivity.this);
        mChooseStageView.setOnSelectListener(ChooseStageActivity.this);
    }

    private void initData() {
        mTitleView.setText(getText(R.string.choosestage));
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mConfirmView.setVisibility(View.VISIBLE);
        mConfirmView.setText(getText(R.string.ok));
        mConfirmView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector_choose_ensure_bg));
        mChooseStageView.setData(getList());
        mChooseStageView.setTextLocation(PickerView.DEFAULT_CENTER);
        String stageName = LoginInfo.getStageName();
        if (!TextUtils.isEmpty(stageName)) {
            stageText=stageName;
            stageId=LoginInfo.getStageid();
            mChooseStageView.setSelected(stageName);

        }
    }

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        int[] ids = Constants.StageTxtId;
        stageText = getText(ids[0]).toString();
        stageId = stageIds[0];
        for (int i = 0; i < ids.length; i++) {
            list.add(getText(ids[i]).toString());
        }
        return list;
    }

    @Override
    public void onSelect(View view, String text, int selectId) {
        switch (view.getId()) {
            case R.id.pv_stage:
                stageText = text;
                stageId = stageIds[selectId];
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (LoginInfo.isLogIn()) {
                    if (!stageId.equals(LoginInfo.getStageid()))
                        showDialog();
                } else {
                    result();
                }
                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }

    private void showDialog() {
        if (mStageDialog == null) {
            mStageDialog = new StageDialog(mContext);
            mStageDialog.addStageListener(new StageDialog.onStageClickListener() {
                @Override
                public void onStageChangeYes() {
                    mStageDialog.dismiss();
                    sendStageId(stageId);
                }

                @Override
                public void onStageChangeNo() {
                    mStageDialog.dismiss();
                }
            });
        }
        mStageDialog.show();
    }

    private void sendStageId(String stageId) {
        rootView.showLoadingView();
        mChooseStageRequest = new ChooseStageRequest();
        mChooseStageRequest.stageid = stageId;
        mChooseStageRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    result();
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
    public void finish() {
        EditTextManger.getManager(mTitleView).hideSoftInput();
        super.finish();
    }

    private void result() {
        StageMessage message = new StageMessage();
        message.requestCode = mRequestCode;
        message.stageId = stageId;
        message.stageText = stageText;
        EventBus.getDefault().post(message);
        finish();
    }

    public static class StageMessage {
        public int requestCode;
        public String stageText;
        public String stageId;
    }

}
