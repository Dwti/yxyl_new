package com.yanxiu.gphone.student.user.userinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import com.yanxiu.gphone.student.user.userinfo.request.UserEditNameRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/2 14:06.
 * Function :
 */
public class UserEditNameActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private TextView mConfirmView;
    private EditText mEditNameView;
    private ImageView mClearView;

    private UserEditNameRequest mUserEditNameRequest;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, UserEditNameActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = UserEditNameActivity.this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_usereditname);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUserEditNameRequest!=null){
            mUserEditNameRequest.cancelRequest();
            mUserEditNameRequest=null;
        }
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);
        mConfirmView = (TextView) findViewById(R.id.tv_right);

        mEditNameView = (EditText) findViewById(R.id.et_name);
        mClearView = (ImageView) findViewById(R.id.iv_clear);
    }

    private void listener() {
        mBackView.setOnClickListener(UserEditNameActivity.this);
        mConfirmView.setOnClickListener(UserEditNameActivity.this);
        mClearView.setOnClickListener(UserEditNameActivity.this);
        EditTextManger.getManager(mEditNameView).setTextChangedListener(UserEditNameActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.userinfo_name);
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));
        mConfirmView.setVisibility(View.VISIBLE);
        mConfirmView.setText(getText(R.string.save));
        mConfirmView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector_choose_ensure_bg));
        mConfirmView.setEnabled(false);
        mClearView.setEnabled(false);

        mEditNameView.setText(LoginInfo.getRealName());
        if (!TextUtils.isEmpty(LoginInfo.getRealName())) {
            mEditNameView.setSelection(mEditNameView.length());
        }

        mBackView.setBackgroundResource(R.drawable.selector_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                EditTextManger.getManager(mTopView).hideSoftInput();
                finish();
                break;
            case R.id.tv_right:
                String name=mEditNameView.getText().toString().trim();
                saveName(name);
                break;
            case R.id.iv_clear:
                mEditNameView.setText("");
                break;
        }
    }

    private void saveName(final String name){
        rootView.showLoadingView();
        mUserEditNameRequest=new UserEditNameRequest();
        mUserEditNameRequest.realname=name;
        mUserEditNameRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0){
                    EditNameMessage message=new EditNameMessage();
                    message.name=name;
                    EventBus.getDefault().post(message);
                    LoginInfo.saveRealName(name);
                    UserEditNameActivity.this.finish();
                }else {
                    ToastManager.showMsg(R.string.data_uploader_failed);
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
        setButtonFocusChange(!isEmpty);
    }

    private void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mConfirmView.setEnabled(true);
            mClearView.setEnabled(true);
            mClearView.setVisibility(View.VISIBLE);
        } else {
            mConfirmView.setEnabled(false);
            mClearView.setEnabled(false);
            mClearView.setVisibility(View.INVISIBLE);
        }
    }

    public class EditNameMessage {
        public String name;
    }
}
