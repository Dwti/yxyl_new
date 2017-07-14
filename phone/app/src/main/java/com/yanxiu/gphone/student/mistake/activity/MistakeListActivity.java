package com.yanxiu.gphone.student.mistake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.mistake.adapter.MistakeListAdapter;
import com.yanxiu.gphone.student.mistake.request.MistakeListRequest;
import com.yanxiu.gphone.student.mistake.response.MistakeListResponse;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/12 11:23.
 * Function :
 */
public class MistakeListActivity extends YanxiuBaseActivity implements View.OnClickListener, MistakeListAdapter.onItemClickListener {

    private Context mContext;
    private ImageView mBackView;
    private TextView mTitleView;
    private MistakeListAdapter mMistakeAdapter;
    private PublicLoadLayout rootView;
    private MistakeListRequest mMistakeListRequest;

    public static void LuanchActivity(Context context){
        Intent intent=new Intent(context,MistakeListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=MistakeListActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_mistakelist);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        RecyclerView mistakeListView = (RecyclerView) findViewById(R.id.recy);
        mistakeListView.setLayoutManager(new LinearLayoutManager(this));
        mMistakeAdapter=new MistakeListAdapter(this);
        mistakeListView.setAdapter(mMistakeAdapter);
    }

    private void listener() {
        mBackView.setOnClickListener(MistakeListActivity.this);
        mMistakeAdapter.addClickListener(MistakeListActivity.this);
    }

    private void initData() {
        mBackView.setVisibility(View.VISIBLE);

        requestData();
    }

    private void requestData(){
        rootView.showLoadingView();
        mMistakeListRequest=new MistakeListRequest();
        mMistakeListRequest.stageId= LoginInfo.getStageid();
        mMistakeListRequest.startRequest(MistakeListResponse.class,new EXueELianBaseCallback<MistakeListResponse>(){

            @Override
            protected void onResponse(RequestBase request, MistakeListResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0){
                    mMistakeAdapter.setData(response.data);
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
    protected void onDestroy() {
        super.onDestroy();
        if (mMistakeListRequest!=null){
            mMistakeListRequest.cancelRequest();
            mMistakeListRequest=null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, MistakeListResponse.Data data, int position) {
        MistakeClassifyActivity.LaunchActivity(mContext);
    }
}
