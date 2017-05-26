package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.CharacterSeparatedEditLayout;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.user.http.JoinClassRequest;
import com.yanxiu.gphone.student.user.response.JoinClassResponse;
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

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,JoinClassActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=JoinClassActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.fragment_search_class);
//        rootView.finish();
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
        mCompleteInfoView= (LinearLayout) findViewById(R.id.ll_complete_info);
        mJoinClassView= (TextView) findViewById(R.id.tv_join_class);
        TextView mClassNumberView= (TextView) findViewById(R.id.tv_class_number);
        mWavasView= (WavesLayout) findViewById(R.id.wavesLayout);
        mNextView= (Button) findViewById(R.id.btn_next);
        mInputClassNumberView= (CharacterSeparatedEditLayout) findViewById(R.id.input_number_layout);
    }

    private void initData() {
        mWavasView.setCanShowWave(false);
        mNextView.setEnabled(false);
        mJoinClassView.setVisibility(View.GONE);
    }

    private void Listener() {
        mCompleteInfoView.setOnClickListener(JoinClassActivity.this);
        mNextView.setOnClickListener(JoinClassActivity.this);
        mInputClassNumberView.setOnTextChangedListener(JoinClassActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                String classNumber=mInputClassNumberView.getText().trim();
                if (classNumber.length()<8){
                    ToastManager.showMsg(getText(R.string.class_number_error));
                    return;
                }
                searchClass(classNumber);
                break;
            case R.id.ll_complete_info:
                CompleteInfoActivity.LaunchActivity(mContext);
                break;
        }
    }

    private void searchClass(String classNumber){
        rootView.showLoadingView();
        mJoinClassRequest=new JoinClassRequest();
        mJoinClassRequest.classId=classNumber;
        mJoinClassRequest.startRequest(JoinClassResponse.class, new HttpCallback<JoinClassResponse>() {
            @Override
            public void onSuccess(RequestBase request, JoinClassResponse ret) {
                rootView.hiddenLoadingView();
                if (ret.status.getCode()==0) {
                    JoinClassSubmitActivity.LaunchActivity(mContext,ret.data.get(0));
                }else {
                    ToastManager.showMsg(ret.status.getDesc());
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
