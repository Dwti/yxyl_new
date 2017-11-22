package com.yanxiu.gphone.student.user.mistake.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.yanxiu.gphone.student.mistakeredo.WrongQPointActivity;
import com.yanxiu.gphone.student.user.mistake.adapter.MistakeListAdapter;
import com.yanxiu.gphone.student.user.mistake.request.MistakeListRequest;
import com.yanxiu.gphone.student.user.mistake.response.MistakeDeleteMessage;
import com.yanxiu.gphone.student.user.mistake.response.MistakeListResponse;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/12 11:23.
 * Function :
 */
public class MistakeListActivity extends YanxiuBaseActivity implements View.OnClickListener, MistakeListAdapter.onItemClickListener {

    private Context mContext;
    private View mTopView;
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
        EventBus.getDefault().register(mContext);
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
        mTopView=findViewById(R.id.include_top);
        RecyclerView mistakeListView = (RecyclerView) findViewById(R.id.recy);
        mistakeListView.setLayoutManager(new LinearLayoutManager(this));
        mMistakeAdapter=new MistakeListAdapter(this);
        mistakeListView.setAdapter(mMistakeAdapter);
    }

    private void listener() {
        mBackView.setOnClickListener(MistakeListActivity.this);
        mMistakeAdapter.addClickListener(MistakeListActivity.this);
        rootView.setRetryButtonOnclickListener(MistakeListActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.my_mistake);
        mTitleView.setTextColor(ContextCompat.getColor(mContext,R.color.color_666666));
        mBackView.setBackgroundResource(R.drawable.selector_back);

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
                if (response.getStatus().getCode()==0&&response.data!=null&&response.data.size()>0){
                    mMistakeAdapter.setData(response.data);
                }else if (response.getStatus().getCode()==67){
                    //no data
                    rootView.showOtherErrorView();
                }else {
                    rootView.showNetErrorView();
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                rootView.showNetErrorView();
                ToastManager.showMsg(error.getMessage().trim());
            }
        });
    }

    public void onEventMainThread(MistakeDeleteMessage message){
        if (message!=null){
            List<MistakeListResponse.Data> datas= mMistakeAdapter.getData();
            for (MistakeListResponse.Data data:datas){
                if (String.valueOf(data.id).equals(message.subjectId)){
                    if (message.wrongNum==0){
                        datas.remove(data);
                        if (datas.size()>0){
                            mMistakeAdapter.notifyDataSetChanged();
                        }else {
                            MistakeListActivity.this.finish();
                        }
                    }else {
                        data.data.wrongNum=message.wrongNum;
                        mMistakeAdapter.notifyDataSetChanged();
                    }
                    return;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
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
            case R.id.retry_button:
                rootView.hiddenNetErrorView();
                requestData();
                break;
        }
    }

    @Override
    public void onItemClick(View view, MistakeListResponse.Data data, int position) {
//        MistakeClassifyActivity.LaunchActivity(mContext,data.name,String.valueOf(data.id),data.data.wrongNum,String.valueOf(data.data.editionId));
        WrongQPointActivity.invoke(this,String.valueOf(data.id),data.name);
    }
}
