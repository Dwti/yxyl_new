package com.yanxiu.gphone.student.learning.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.srt.refresh.EXueELianRefreshLayout;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.learning.adapter.VideoListAdapter;
import com.yanxiu.gphone.student.learning.bean.VideoDataBean;
import com.yanxiu.gphone.student.learning.request.GetResourceListDataRequest;
import com.yanxiu.gphone.student.learning.response.GetResourceListDataResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

/**
 * Created by lufengqing on 2018/1/29.
 */

public class VideoListActivity extends YanxiuBaseActivity implements View.OnClickListener {
    private GridView mGridView;
    private TextView mTips;
    private View mTipsView;
    private ImageView mTipsImg;
    private Button mRefreshBtn;
    private PublicLoadLayout rootView;
    private View mBack;
    private VideoListAdapter mAdapter;
    private TextView mTitle;
    private TextView mDefaultOrder;
    private TextView mHotOrder;

    private String mChannel;
    private String mChapter;
    private EXueELianRefreshLayout mRefreshLayout;
    private int mCurrentPage = 1;
    private int mTotalPage;
    private GetResourceListDataRequest mLastRequest;
    private String mCurrentOrder = "0";
    private String mName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView=new PublicLoadLayout(this);
        rootView.setContentView(R.layout.activity_video_list);
        rootView.setDataEmptyErrorView(R.drawable.data_empty,getResources().getString(R.string.resource_empty));
        setContentView(rootView);
        initView();
        initData();
        initListener();
    }
    private void initListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDefaultOrder.setOnClickListener(this);
        mHotOrder.setOnClickListener(this);
        mRefreshLayout.setRefreshListener(new EXueELianRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh(EXueELianRefreshLayout refreshLayout) {
                refreshData();
            }

            @Override
            public void onLoadMore(EXueELianRefreshLayout refreshLayout) {
                loadMoreData();
            }
        });
        rootView.setRetryButtonOnclickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                refreshData();
            }
        } );
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });
    }

    private void initData() {
        mChannel = getIntent().getStringExtra(Constants.EXTRA_CHANNEL);
        mChapter = getIntent().getStringExtra(Constants.EXTRA_CHAPTER);
        mName = getIntent().getStringExtra(Constants.EXTRA_NAME);
        mTitle.setText(mName);
        mRefreshLayout = (EXueELianRefreshLayout) findViewById(R.id.refreshLayout);
        mAdapter = new VideoListAdapter(VideoListActivity.this, mVideoList, mOnItemClickListener);
        mGridView.setAdapter(mAdapter);
        mGridView.setFocusable(false);
        getResourseList(mCurrentOrder,1);
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gridView);
        mTipsView = rootView.findViewById(R.id.tips_layout);
        mTipsImg = (ImageView) rootView.findViewById(R.id.iv_tips);
        mRefreshBtn = (Button) rootView.findViewById(R.id.btn_refresh);
        mTips = (TextView) rootView.findViewById(R.id.tv_tips);
        mBack = findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mDefaultOrder = (TextView) findViewById(R.id.default_order);
        mHotOrder = (TextView) findViewById(R.id.hot_order);
    }

    public void setLoadMoreEnable(boolean enable) {
        mRefreshLayout.setLoadMoreEnable(enable);
    }

    private void refreshData(){
        mCurrentPage = 1;
        getResourseList(mCurrentOrder,mCurrentPage);
    }

    private void loadMoreData(){
        if(canLoadMore()){
            getResourseList(mCurrentOrder,++mCurrentPage);
        }else {
            finishLoadingMoireIndicator();
            setLoadMoreEnable(false);
            showNoMoreData();
        }
    }

    private boolean canLoadMore(){
        return mCurrentPage < mTotalPage;
    }

    private void finishRefreshIndicator(){
        mRefreshLayout.finishRefreshing();
    }

    private void finishLoadingMoireIndicator(){
        mRefreshLayout.finishLoadMore();
    }

    private void showDataEmptyView(){
        setOrderIsClickable(false);
        mGridView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.data_empty);
        mTips.setText(R.string.resource_empty);
        mRefreshBtn.setVisibility(View.INVISIBLE);
    }

    private void setOrderIsClickable(boolean b) {
        mDefaultOrder.setEnabled(b);
        mHotOrder.setEnabled(b);
        if(b) {
            if(TextUtils.equals(mCurrentOrder, "0")) {
                mDefaultOrder.setTextColor(getResources().getColor(R.color.color_89e00d));
                mHotOrder.setTextColor(getResources().getColor(R.color.color_666666));
            } else {
                mHotOrder.setTextColor(getResources().getColor(R.color.color_89e00d));
                mDefaultOrder.setTextColor(getResources().getColor(R.color.color_666666));
            }
        } else {
            mDefaultOrder.setTextColor(getResources().getColor(R.color.color_666666));
            mHotOrder.setTextColor(getResources().getColor(R.color.color_666666));
        }
    }

    private void showDataErrorView(){
        mGridView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    public void getResourseList(String order, int page) {
        GetResourceListDataRequest request = new GetResourceListDataRequest();
        request.setChannel(mChannel);
//        request.bodyDealer = new DESBodyDealer();
        request.setChapterId(mChapter);
        request.setPage(Integer.toString(page));
        request.setOrderBy(order);
        mLastRequest = request;
        request.startRequest(GetResourceListDataResponse.class, mGetResourceListForSyncCallback);
    }
    protected void openSpecialDetailActivity(VideoDataBean bean) {
        SpecialDetailActivity.invoke(this,bean);
    }

    private int mCurrentPosition;
    VideoListAdapter.OnItemClickListener mOnItemClickListener = new VideoListAdapter.OnItemClickListener() {
        @Override
        public void onClick(VideoDataBean bean, int position) {
            mCurrentPosition = position;
            openSpecialDetailActivity(bean);
        }
    };
    private List<VideoDataBean> mVideoList;
    HttpCallback<GetResourceListDataResponse> mGetResourceListForSyncCallback = new EXueELianBaseCallback<GetResourceListDataResponse>() {
        @Override
        protected void onResponse(RequestBase request, GetResourceListDataResponse response) {
            if (mLastRequest != null && !mLastRequest.equals(request))
                return;
            if (response.getStatus().getCode() == 0) {
                if (mCurrentPage == 1) {
                    finishRefreshIndicator();
                    setLoadMoreEnable(true);
                    mTotalPage = response.getPage().getTotalPage();
                    if (response.getData() != null && response.getData().size() > 0) {
                        mVideoList = response.getData();
                        showContentView(mVideoList);
                    } else {
                        mAdapter.clearData();
                        //                    rootView.showOtherErrorView();
                        showDataEmptyView();
                    }
                } else if (mCurrentPage > 1) {
                    finishLoadingMoireIndicator();
                    if (response.getData() != null && response.getData().size() > 0) {
                        mVideoList.addAll(response.getData());
                        showContentView(mVideoList);
                    } else {
                        showNoMoreData();
                    }
                } else {
                }
            } else {
                if (mCurrentPage == 1) {
                    finishRefreshIndicator();
                    setLoadMoreEnable(true);
                    if (mAdapter != null) {
                        mAdapter.clearData();
                    }
                    //code=3 表示数据为空
                    if (response.getStatus().getCode() == 3) {
//                    rootView.showOtherErrorView();
                    showDataEmptyView();
                    } else {
                       //                rootView.showNetErrorView();
                showDataErrorView();
                    }
                } else if (mCurrentPage > 1) {
                    finishLoadingMoireIndicator();
                    if (response.getStatus().getCode() == 3) {
                        showNoMoreData();
                    } else {
                        showLoadMoreErrorMsg(response.getStatus().getDesc());
                    }
                }
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            if (mLastRequest != null && !mLastRequest.equals(request))
                return;
            if (mCurrentPage == 1) {
                finishRefreshIndicator();
                setLoadMoreEnable(true);
                if (mAdapter != null) {
                    mAdapter.clearData();
                }
               //                rootView.showNetErrorView();
                showDataErrorView();
            } else if (mCurrentPage > 1) {
                finishLoadingMoireIndicator();
                showLoadMoreErrorMsg(error.getLocalizedMessage());
            }
        }
    };

    public void showNoMoreData() {
        setLoadMoreEnable(false);
        ToastManager.showMsg(R.string.no_more_data);
    }

    private void showLoadMoreErrorMsg(String msg){
        ToastManager.showMsg(msg);
    }

    private void showContentView(List<VideoDataBean> mVideoList) {
        setOrderIsClickable(true);
        mGridView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        mAdapter.replaceData(mVideoList);
//        if(mCurrentPage == 1){
//            mGridView.scrollToPosition(0);
//        }
    }


    public static void invoke(Activity activity, String channel, String chapterId, String title) {
        Intent intent = new Intent(activity, VideoListActivity.class);
        intent.putExtra(Constants.EXTRA_CHANNEL, channel);
        intent.putExtra(Constants.EXTRA_CHAPTER,chapterId);
        intent.putExtra(Constants.EXTRA_NAME, title);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.default_order:
                mDefaultOrder.setTextColor(getResources().getColor(R.color.color_89e00d));
                mHotOrder.setTextColor(getResources().getColor(R.color.color_666666));
                mCurrentOrder = "0";
                getResourseList(mCurrentOrder,1);
                break;
            case R.id.hot_order:
                mHotOrder.setTextColor(getResources().getColor(R.color.color_89e00d));
                mDefaultOrder.setTextColor(getResources().getColor(R.color.color_666666));
                mCurrentOrder = "1";
                getResourseList(mCurrentOrder,1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    mAdapter.updatePlayTimes(mCurrentPosition);
                }
                break;
            default:
                break;
        }
    }
}
