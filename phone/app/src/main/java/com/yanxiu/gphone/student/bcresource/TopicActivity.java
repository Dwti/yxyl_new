package com.yanxiu.gphone.student.bcresource;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.srt.refresh.EXueELianRefreshLayout;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bcresource.adapter.TopicListAdapter;
import com.yanxiu.gphone.student.bcresource.bean.TopicBean;
import com.yanxiu.gphone.student.customviews.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class TopicActivity extends YanxiuBaseActivity{

    private EXueELianRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private LoadingView mLoadingView;

    private ImageView mTipsImg;

    private View mTipsView, mBack;

    private TextView mTitle, mTips;

    private Button mRefreshBtn;

    private TopicListAdapter mAdapter;

    private List<TopicBean> mData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        initView();
        initListener();
    }

    private void initView(){
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = findViewById(R.id.iv_back);
        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRefreshLayout = (EXueELianRefreshLayout) findViewById(R.id.refreshLayout);
        mTipsImg = (ImageView) findViewById(R.id.iv_tips);
        mTipsView = findViewById(R.id.tips_layout);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);

        mAdapter = new TopicListAdapter(mData,mOnItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        setLoadMoreEnable(true);
    }

    private void initListener(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefreshLayout.setRefreshListener(new EXueELianRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh(EXueELianRefreshLayout refreshLayout) {
                refreshBCData();
            }

            @Override
            public void onLoadMore(EXueELianRefreshLayout refreshLayout) {
                loadMoreBCData();
            }
        });
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.showLoadingView();
                refreshBCData();
            }
        });
    }

    public void setLoadMoreEnable(boolean enable) {
        mRefreshLayout.setLoadMoreEnable(enable);
    }

    private void refreshBCData(){

    }

    private void loadMoreBCData(){

    }

    public void showHomework(List<TopicBean> list) {
        showContentView();
        mAdapter.replaceData(list);
    }

    public void showDataEmpty() {
        showDataEmptyView();
    }

    public void showDataError() {
        showDataErrorView();
    }
    private void showContentView(){
        mRefreshLayout.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
    }

    private void showDataEmptyView(){
        mRefreshLayout.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        //TODO 图片替换
        mTipsImg.setImageResource(R.drawable.has_not_publish_hwk);
        mTips.setText(R.string.class_no_homework);
        mRefreshBtn.setText(R.string.click_to_refresh);
    }

    private void showDataErrorView(){
        mRefreshLayout.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    public void showNoMoreData() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int firstPos = layoutManager.findFirstCompletelyVisibleItemPosition();
        if(firstPos != 0 ){
            mAdapter.addFooterView();
        }
    }

    TopicListAdapter.OnItemClickListener mOnItemClickListener = new TopicListAdapter.OnItemClickListener() {
        @Override
        public void onClick(TopicBean bean, int position) {

        }
    };
}
