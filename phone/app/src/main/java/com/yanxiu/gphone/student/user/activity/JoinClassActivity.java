package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.WavesLayout;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 16:03.
 * Function :
 */
public class JoinClassActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private Context mContext;
    private LinearLayout mCompleteInfoView;
    private TextView mJoinClassView;
    private WavesLayout mWavasView;
    private Button mNextView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,JoinClassActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);
        mContext=JoinClassActivity.this;
        initView();
        initData();
        Listener();
    }

    private void initView() {
        mCompleteInfoView= (LinearLayout) findViewById(R.id.ll_complete_info);
        mJoinClassView= (TextView) findViewById(R.id.tv_join_class);
        TextView mClassNumberView= (TextView) findViewById(R.id.tv_class_number);
        mWavasView= (WavesLayout) findViewById(R.id.wavesLayout);
        mNextView= (Button) findViewById(R.id.btn_next);
    }

    private void initData() {
        mWavasView.setCanShowWave(false);
        mNextView.setEnabled(false);
        mJoinClassView.setVisibility(View.GONE);
    }

    private void Listener() {
        mCompleteInfoView.setOnClickListener(JoinClassActivity.this);
        mNextView.setOnClickListener(JoinClassActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                break;
            case R.id.ll_complete_info:
                CompleteInfoActivity.LaunchActivity(mContext);
                break;
        }
    }
}
