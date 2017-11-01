package com.yanxiu.gphone.student.bcresource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bcresource.adapter.BCListAdapter;
import com.yanxiu.gphone.student.bcresource.bean.BCBean;
import com.yanxiu.gphone.student.bcresource.bean.BCWrapperBean;
import com.yanxiu.gphone.student.bcresource.request.TopicTreeRequest;
import com.yanxiu.gphone.student.bcresource.response.TopicTreeResponse;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class BCActivity extends YanxiuBaseActivity {

    private RecyclerView mRecyclerView;

    private LoadingView mLoadingView;

    private ImageView mTipsImg;

    private View mTipsView, mBack;

    private TextView mTitle, mTips;

    private Button mRefreshBtn;

    private BCListAdapter mAdapter;

    private String mType,mId;

    private List<BCBean> mData = new ArrayList<>();

    public static final String BC_TYPE = "BC_TYPE";
    public static final String BC_ID = "BC_ID";
    public static final String BC_NAME = "BC_NAME";

    public static void invoke(Activity activity,String type,String id){
        Intent intent = new Intent(activity,BCActivity.class);
        intent.putExtra(BC_TYPE,type);
        intent.putExtra(BC_ID,id);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bc);
        initView();
        initListener();
        initData();
    }

    private void initView(){
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = findViewById(R.id.iv_back);
        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTipsImg = (ImageView) findViewById(R.id.iv_tips);
        mTipsView = findViewById(R.id.tips_layout);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);

        mAdapter = new BCListAdapter(mData,mOnItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initListener(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.showLoadingView();
                initData();
            }
        });
    }

    private void initData(){
        mType = getIntent().getStringExtra(BC_TYPE);
        mId = getIntent().getStringExtra(BC_ID);
        TopicTreeRequest request = new TopicTreeRequest();
        request.setId(mId);
        request.setType(mType);
        request.startRequest(TopicTreeResponse.class,topicTreeCallback);
    }

    HttpCallback<TopicTreeResponse> topicTreeCallback = new EXueELianBaseCallback<TopicTreeResponse>() {
        @Override
        protected void onResponse(RequestBase request, TopicTreeResponse response) {
            if(response.getStatus().getCode() == 0){
                if(response.getData().size() > 0){
                    showBCData(sortTopicData(response));
                }else {
                    showDataEmptyView();
                }
            }else {
                showDataErrorView();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView();
        }
    };

    private List<BCBean> sortTopicData(TopicTreeResponse response){
        List<BCBean> result = new ArrayList<>();
        if(response == null || response.getData() == null || response.getData().size() == 0 )
            return result;
        for(BCWrapperBean wrapperBean: response.getData()){
            BCBean bcBean = new BCBean();
            bcBean.setName(wrapperBean.getName());
            bcBean.setId(wrapperBean.getId());
            bcBean.setResource_num(wrapperBean.getResource_num());
            bcBean.setQuestion_num(wrapperBean.getQuestion_num());
            bcBean.setType(BCBean.TYPE_PARENT);

            result.add(bcBean);
            result.addAll(wrapperBean.getChildren());
        }
        return result;
    }

    public void showBCData(List<BCBean> list) {
        showContentView();
        mAdapter.replaceData(list);
    }

    private void showContentView(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
    }

    private void showDataEmptyView(){
        mRecyclerView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.data_empty);
        mTips.setText(R.string.no_bc);
        mRefreshBtn.setText(R.string.click_to_refresh);
    }

    private void showDataErrorView(){
        mRecyclerView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    BCListAdapter.OnItemClickListener mOnItemClickListener = new BCListAdapter.OnItemClickListener() {
        @Override
        public void onClick(BCBean bean, int position) {
            TopicActivity.invoke(BCActivity.this,mType,bean.getId(),bean.getName());
        }
    };
}
