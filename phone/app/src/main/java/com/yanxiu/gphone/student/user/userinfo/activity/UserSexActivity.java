package com.yanxiu.gphone.student.user.userinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.yanxiu.gphone.student.user.userinfo.request.UserSexRequest;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/2 16:10.
 * Function :
 */
public class UserSexActivity extends YanxiuBaseActivity implements View.OnClickListener, PickerView.onSelectListener {

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private TextView mConfirmView;
    private PickerView mChooseSexView;

    private int[] mSexIds = Constants.SexId;
    private String mSexText;
    private int mSexId;
    private UserSexRequest mUserSexRequest;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, UserSexActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = UserSexActivity.this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_usersex);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUserSexRequest != null) {
            mUserSexRequest.cancelRequest();
            mUserSexRequest = null;
        }
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);
        mConfirmView = (TextView) findViewById(R.id.tv_right);
        mChooseSexView = (PickerView) findViewById(R.id.pv_stage);
    }

    private void listener() {
        mBackView.setOnClickListener(UserSexActivity.this);
        mConfirmView.setOnClickListener(UserSexActivity.this);
        mChooseSexView.setOnSelectListener(UserSexActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.userinfo_sex);
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));
        mConfirmView.setVisibility(View.VISIBLE);
        mConfirmView.setText(getText(R.string.ok));
        mConfirmView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector_choose_ensure_bg));

        mChooseSexView.setData(getList());
        mChooseSexView.setTextLocation(PickerView.DEFAULT_CENTER);
        setDefaultSexData();
    }

    private void setDefaultSexData() {
        int sexId = LoginInfo.getSex();
        setSexText(sexId);
        mChooseSexView.setSelected(mSexText);
    }

    private void setSexText(int sexId) {
        switch (sexId) {
            case Constants.Sex.SEX_TYPE_MAN:
                mSexText = getText(R.string.sex_man).toString();
                break;
            case Constants.Sex.SEX_TYPE_WOMAN:
                mSexText = getText(R.string.sex_woman).toString();
                break;
            case Constants.Sex.SEX_TYPE_UNKNOWN:
                mSexText = getText(R.string.sex_unknown).toString();
                break;
        }
        mSexId = sexId;
    }

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        int[] ids = Constants.SexTxtId;
        mSexText = getText(ids[0]).toString();
        mSexId = mSexIds[0];
        for (int id : ids) {
            list.add(getText(id).toString());
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_right:
                saveSex();
                break;
        }
    }

    private void saveSex() {
        rootView.showLoadingView();
        mUserSexRequest = new UserSexRequest();
        mUserSexRequest.sex = String.valueOf(mSexId);
        mUserSexRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    LoginInfo.saveSex(mSexId);
                    SexMessage message = new SexMessage();
                    message.sexId = mSexId;
                    message.sexTxt = mSexText;
                    EventBus.getDefault().post(message);
                    UserSexActivity.this.finish();
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
    public void onSelect(View view, String text, int selectId) {
        mSexText = text;
        mSexId = mSexIds[selectId];
    }

    public class SexMessage {
        public String sexTxt;
        public int sexId;
    }
}
