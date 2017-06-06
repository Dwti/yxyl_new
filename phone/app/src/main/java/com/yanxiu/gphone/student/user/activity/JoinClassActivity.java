package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.CharacterSeparatedEditLayout;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.user.request.JoinClassRequest;
import com.yanxiu.gphone.student.user.response.JoinClassResponse;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.ToastManager;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 16:03.
 * Function :
 */
public class JoinClassActivity extends YanxiuBaseActivity implements View.OnClickListener, CharacterSeparatedEditLayout.OnTextChangedListener {

    public static final String KEY="bean";

    private Context mContext;
    private LinearLayout mCompleteInfoView;
    private TextView mJoinClassView;
    private WavesLayout mWavasView;
    private Button mNextView;
    private CharacterSeparatedEditLayout mInputClassNumberView;
    private PublicLoadLayout rootView;
    private JoinClassRequest mJoinClassRequest;
    private LoginActivity.ThridMessage thridMessage;
    private ImageView mBackView;
    private TextView mTitleView;
    private TextView mSkipView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,JoinClassActivity.class);
        intent.putExtra(LoginActivity.TYPE,LoginActivity.TYPE_DEFAULT);
        context.startActivity(intent);
    }

    public static void LaunchActivity(Context context, LoginActivity.ThridMessage message){
        Intent intent=new Intent(context,JoinClassActivity.class);
        intent.putExtra(LoginActivity.TYPE,LoginActivity.TYPE_THRID);
        intent.putExtra(LoginActivity.THRID_LOGIN,message);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=JoinClassActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.fragment_search_class);
        String type=getIntent().getStringExtra(LoginActivity.TYPE);
        if (type.equals(LoginActivity.TYPE_THRID)) {
            thridMessage = (LoginActivity.ThridMessage) getIntent().getSerializableExtra(LoginActivity.THRID_LOGIN);
        }
        setContentView(rootView);
        initView();
        initData();
        Listener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mJoinClassRequest!=null){
            mJoinClassRequest.cancelRequest();
            mJoinClassRequest=null;
        }
    }

    private void initView() {
        mSkipView= (TextView) findViewById(R.id.how_to_join_class);
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        mCompleteInfoView= (LinearLayout) findViewById(R.id.ll_complete_info);
        mJoinClassView= (TextView) findViewById(R.id.tv_join_class);
        TextView mClassNumberView= (TextView) findViewById(R.id.tv_class_number);
        mWavasView= (WavesLayout) findViewById(R.id.wavesLayout);
        mNextView= (Button) findViewById(R.id.btn_next);
        mInputClassNumberView= (CharacterSeparatedEditLayout) findViewById(R.id.input_number_layout);
    }

    private void initData() {
//        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(getText(R.string.addclass));
        mWavasView.setCanShowWave(false);
        mNextView.setEnabled(false);
        mJoinClassView.setVisibility(View.GONE);
        mSkipView.setText(R.string.skip_join_class);
    }

    private void Listener() {
        mBackView.setOnClickListener(JoinClassActivity.this);
        mCompleteInfoView.setOnClickListener(JoinClassActivity.this);
        mNextView.setOnClickListener(JoinClassActivity.this);
        mInputClassNumberView.setOnTextChangedListener(JoinClassActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                JoinClassActivity.this.finish();
                EditTextManger.getManager(mTitleView).hideSoftInput(mContext);
                break;
            case R.id.btn_next:
                String classNumber=mInputClassNumberView.getText().trim();
                if (classNumber.length()<8){
                    ToastManager.showMsg(getText(R.string.class_number_error));
                    return;
                }
                searchClass(classNumber);
                break;
            case R.id.ll_complete_info:
                if (thridMessage!=null) {
                    CompleteInfoActivity.LaunchActivity(mContext, thridMessage);
                }else {
                    CompleteInfoActivity.LaunchActivity(mContext);
                }
                break;
        }
    }

    private void searchClass(String classNumber){
        rootView.showLoadingView();
        mJoinClassRequest=new JoinClassRequest();
        mJoinClassRequest.classId=classNumber;
        mJoinClassRequest.startRequest(JoinClassResponse.class, new EXueELianBaseCallback<JoinClassResponse>() {

            @Override
            protected void onResponse(RequestBase request, JoinClassResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0&&!response.data.get(0).status.equals("2")) {
                    if (thridMessage!=null) {
                        JoinClassSubmitActivity.LaunchActivity(mContext, response.data.get(0), thridMessage);
                    }else {
                        JoinClassSubmitActivity.LaunchActivity(mContext, response.data.get(0));
                    }
                }else if (response.getStatus().getCode()==0&&response.data.get(0).status.equals("2")){
                    ToastManager.showMsg(getText(R.string.class_cannot_join));
                }else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage().trim());
            }
        });
    }

    @Override
    public void onTextChanged(Editable s) {
        String text=s.toString().trim();
        if (text.length()>0){
            mWavasView.setCanShowWave(true);
            mNextView.setEnabled(true);
        }else {
            mWavasView.setCanShowWave(false);
            mNextView.setEnabled(false);
        }
    }
}
