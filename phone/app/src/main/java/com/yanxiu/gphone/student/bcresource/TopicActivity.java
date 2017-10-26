package com.yanxiu.gphone.student.bcresource;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.srt.refresh.EXueELianRefreshLayout;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bcresource.adapter.TopicListAdapter;
import com.yanxiu.gphone.student.bcresource.bean.TopicBean;
import com.yanxiu.gphone.student.bcresource.request.RMSPaperRequest;
import com.yanxiu.gphone.student.bcresource.request.TopicPaperRequest;
import com.yanxiu.gphone.student.bcresource.response.TopicPaperResponse;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.http.request.AnswerReportRequest;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerReportActicity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class TopicActivity extends YanxiuBaseActivity{

    private EXueELianRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private LoadingView mLoadingView;

    private ImageView mTipsImg, iv_up, iv_down, iv_expand, iv_item0, iv_item1, iv_item2;

    private View mTipsView, mBack, mFilterBar, mContentView, mLlFilterLetter,mLlFilterScope;

    private TextView mTitle, mTips, tv_filter_letter, tv_filter_pop, tv_filter_scope, tv_item0, tv_item1, tv_item2;

    private Button mRefreshBtn;

    private TopicListAdapter mAdapter;

    private List<TopicBean> mTopicList = new ArrayList<>();

    private String mType, mId, mName;

    private int mCurrentPage = 1, mOrder = 0, mScope = 0, mTotalPage;

    private RequestBase mLastRequest;

    private PopupWindow mPopWindow;

    private boolean shouldRefreshWhenResume = false;


    public static void invoke(Activity activity, String type, String id, String name){
        Intent intent = new Intent(activity,TopicActivity.class);
        intent.putExtra(BCActivity.BC_TYPE,type);
        intent.putExtra(BCActivity.BC_ID,id);
        intent.putExtra(BCActivity.BC_NAME,name);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shouldRefreshWhenResume){
            getTopicPaper(1);
            shouldRefreshWhenResume = false;
        }
    }

    private void initView(){
        mContentView = findViewById(R.id.content);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = findViewById(R.id.iv_back);
        mFilterBar = findViewById(R.id.filter_bar);

        mLlFilterLetter = findViewById(R.id.ll_filter_letter);
        mLlFilterScope = findViewById(R.id.ll_filter_scope);

        tv_filter_letter = (TextView) findViewById(R.id.tv_filter_letter);
        tv_filter_pop = (TextView) findViewById(R.id.tv_filter_pop);
        tv_filter_scope = (TextView) findViewById(R.id.tv_filter_scope);
        iv_up = (ImageView) findViewById(R.id.iv_up);
        iv_down = (ImageView) findViewById(R.id.iv_down);
        iv_expand = (ImageView) findViewById(R.id.iv_expand);

        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRefreshLayout = (EXueELianRefreshLayout) findViewById(R.id.refreshLayout);
        mTipsImg = (ImageView) findViewById(R.id.iv_tips);
        mTipsView = findViewById(R.id.tips_layout);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);

        mAdapter = new TopicListAdapter(mTopicList,mOnItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        setLoadMoreEnable(true);

        tv_filter_letter.setSelected(true);
        iv_down.setSelected(true);
    }

    private void initListener(){
        mLlFilterLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow();
                if(tv_filter_pop.isSelected()){
                    tv_filter_pop.setSelected(false);
                    tv_filter_letter.setSelected(true);
                    iv_down.setSelected(true);
                    mOrder = 0 ;
                }else {
                    mOrder = (mOrder + 1 ) % 2 ;
                    iv_up.setSelected(mOrder==1);
                    iv_down.setSelected(mOrder == 0);
                }

                mCurrentPage = 1;
                getTopicPaper(1);
            }
        });

        tv_filter_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow();
                if(!tv_filter_pop.isSelected()){
                    tv_filter_letter.setSelected(false);
                    iv_up.setSelected(false);
                    iv_down.setSelected(false);
                    tv_filter_pop.setSelected(true);

                    mOrder = 10;
                    mCurrentPage = 1;
                    getTopicPaper(1);
                }
            }
        });

        mLlFilterScope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPopWindow != null && mPopWindow.isShowing()){
                    dismissPopWindow();
                }else {
                    showPopWindow();
                    iv_expand.setImageResource(R.drawable.up_pressed);
                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.showLoadingView();
                refreshData();
            }
        });
    }

    private void setScopeAreaState(){
        if(mScope == 0){
            iv_expand.setImageResource(R.drawable.down_normal);
            tv_filter_scope.setTextColor(Color.parseColor("#666666"));
            tv_filter_scope.setText("全部");
        }else {
            iv_expand.setImageResource(R.drawable.down_pressed);
            tv_filter_scope.setTextColor(Color.parseColor("#89e00d"));
            if(mScope == 1){
                tv_filter_scope.setText("已作答");
            }else if(mScope == 2){
                tv_filter_scope.setText("未作答");
            }
        }
    }

    private void initData(){
        mType = getIntent().getStringExtra(BCActivity.BC_TYPE);
        mId = getIntent().getStringExtra(BCActivity.BC_ID);
        mName = getIntent().getStringExtra(BCActivity.BC_NAME);

        mTitle.setText(mName);

        getTopicPaper(1);
    }

    private void getTopicPaper(int page){
        TopicPaperRequest request = new TopicPaperRequest();
        request.setType(mType);
        request.setId(mId);
        request.setPage(String.valueOf(page));
        request.setOrder(String.valueOf(mOrder));
        request.setScope(String.valueOf(mScope));

        mLastRequest = request;
        request.startRequest(TopicPaperResponse.class, mTopicPaperCallback);
    }

    private void showPopWindow(){
        if(mPopWindow == null){
            View view = LayoutInflater.from(this).inflate(R.layout.topic_filter_pop,null);
            mPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,mContentView.getHeight());
            View bg = view.findViewById(R.id.bg);

            View item0 = view.findViewById(R.id.item0);
            iv_item0 = (ImageView) view.findViewById(R.id.iv0);
            tv_item0 = (TextView) view.findViewById(R.id.tv0);

            View item1 = view.findViewById(R.id.item1);
            tv_item1 = (TextView) view.findViewById(R.id.tv1);
            iv_item1 = (ImageView) view.findViewById(R.id.iv1);

            View item2 = view.findViewById(R.id.item2);
            tv_item2 = (TextView) view.findViewById(R.id.tv2);
            iv_item2 = (ImageView) view.findViewById(R.id.iv2);

            setScopeItemSelected();

            item0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tv_item0.isSelected())
                        return;
                    mScope = 0;
                    setScopeItemSelected();
                    mCurrentPage=1;
                    getTopicPaper(1);
                    dismissPopWindow();
                }
            });

            item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tv_item1.isSelected())
                        return;
                    mScope = 1;
                    setScopeItemSelected();

                    mCurrentPage=1;
                    getTopicPaper(1);
                    dismissPopWindow();
                }
            });

            item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tv_item2.isSelected())
                        return;
                    mScope = 2;
                    setScopeItemSelected();

                    mCurrentPage=1;
                    getTopicPaper(1);
                    dismissPopWindow();
                }
            });

            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPopWindow();
                }
            });

            mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setScopeAreaState();
                }
            });
        }

        mPopWindow.showAsDropDown(mFilterBar);
    }

    private void dismissPopWindow(){
        if(mPopWindow != null && mPopWindow.isShowing())
            mPopWindow.dismiss();
    }
    
    private void setScopeItemSelected(){
        if(mScope == 0){
            tv_item0.setSelected(true);
            iv_item0.setVisibility(View.VISIBLE);
            tv_item1.setSelected(false);
            iv_item1.setVisibility(View.GONE);
            tv_item2.setSelected(false);
            iv_item2.setVisibility(View.GONE);
        }else if(mScope == 1){
            tv_item1.setSelected(true);
            iv_item1.setVisibility(View.VISIBLE);
            tv_item0.setSelected(false);
            iv_item0.setVisibility(View.GONE);
            tv_item2.setSelected(false);
            iv_item2.setVisibility(View.GONE);
        }else if(mScope == 2){
            tv_item2.setSelected(true);
            iv_item2.setVisibility(View.VISIBLE);
            tv_item0.setSelected(false);
            iv_item0.setVisibility(View.GONE);
            tv_item1.setSelected(false);
            iv_item1.setVisibility(View.GONE);
        }
    }
    public void setLoadMoreEnable(boolean enable) {
        mRefreshLayout.setLoadMoreEnable(enable);
    }

    private void refreshData(){
        mCurrentPage = 1;
        getTopicPaper(1);
    }

    private void loadMoreData(){
        if(canLoadMore()){
            getTopicPaper(++mCurrentPage);
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
        mLoadingView.hiddenLoadingView();
        mRefreshLayout.finishRefreshing();
    }

    private void finishLoadingMoireIndicator(){
        mRefreshLayout.finishLoadMore();
    }

    private void showContentView(List<TopicBean> list){
        mRefreshLayout.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        mAdapter.replaceData(list);
    }

    private void showDataEmptyView(){
        mRefreshLayout.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.data_empty);
        mTips.setText(R.string.no_topic);
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

    private void showLoadMoreErrorMsg(String msg){
        ToastManager.showMsg(msg);
    }

    HttpCallback<TopicPaperResponse> mTopicPaperCallback = new EXueELianBaseCallback<TopicPaperResponse>() {
        @Override
        protected void onResponse(RequestBase request, TopicPaperResponse response) {
            if(mLastRequest !=null && !mLastRequest.equals(request))
                return;
            if(response.getStatus().getCode() == 0){
                if(mCurrentPage == 1){
                    finishRefreshIndicator();
                    setLoadMoreEnable(true);
                    mTotalPage = response.getPage().getTotalPage();
                    if(response.getData() != null && response.getData().size() > 0){
                        mTopicList = response.getData();
                        showContentView(mTopicList);
                    }else {
                        mAdapter.clearData();
                        showDataEmptyView();
                    }
                }else if (mCurrentPage > 1){
                    finishLoadingMoireIndicator();
                    if(response.getData() !=null && response.getData().size() > 0){
                        mTopicList.addAll(response.getData());
                        showContentView(mTopicList);
                    }else {
                        showNoMoreData();
                    }
                }else {}
            }else {
                if(mCurrentPage == 1){
                    finishRefreshIndicator();
                    setLoadMoreEnable(true);
                    mAdapter.clearData();
                    showDataErrorView();
                }else if(mCurrentPage > 1){
                    finishLoadingMoireIndicator();
                    showLoadMoreErrorMsg(response.getStatus().getDesc());
                }
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            if(mLastRequest !=null && !mLastRequest.equals(request))
                return;
            if(mCurrentPage == 1){
                finishRefreshIndicator();
                setLoadMoreEnable(true);
                mAdapter.clearData();
                showDataErrorView();
            }else if(mCurrentPage > 1){
                finishLoadingMoireIndicator();
                showLoadMoreErrorMsg(error.getLocalizedMessage());
            }
        }
    };

    TopicListAdapter.OnItemClickListener mOnItemClickListener = new TopicListAdapter.OnItemClickListener() {
        @Override
        public void onClick(TopicBean bean, int position) {
//            Intent intent = new Intent(TopicActivity.this, VideoActivity.class);
//            startActivity(intent);
            if(bean.getPaperStatus() != null && bean.getPaperStatus().getStatus() == 2){
                getReport(bean.getPaperStatus().getPpid());
            }else {
                getRMSPaper(bean.getId());
            }
        }
    };

    private void getReport(String id){
        AnswerReportRequest reportRequest = new AnswerReportRequest(id);
        reportRequest.bodyDealer = new DESBodyDealer();
        reportRequest.startRequest(PaperResponse.class,mReportCallback);
    }

    private void openAnswerReportUI(String paperId){
        AnswerReportActicity.invoke(this,paperId,Constants.FROM_BC_RESOURCE);
        shouldRefreshWhenResume = true;
    }

    HttpCallback<PaperResponse> mReportCallback = new HttpCallback<PaperResponse>() {
        @Override
        public void onSuccess(RequestBase request, PaperResponse ret) {
            if(ret.getStatus().getCode() == 0){
                if(ret.getData().size() > 0){
                    Paper paper = new Paper(ret.getData().get(0), QuestionShowType.ANALYSIS);
                    DataFetcher.getInstance().save(paper.getId(),paper);
                    openAnswerReportUI(paper.getId());
                }else {
                    ToastManager.showMsg("报告为空");
                }
            }else {
                ToastManager.showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };

    private void getRMSPaper(String id){
        RMSPaperRequest request = new RMSPaperRequest();
        request.bodyDealer = new DESBodyDealer();
        request.setRmsPaperId(id);
        request.setType(mType);
        request.startRequest(PaperResponse.class,mPaperCallback);
    }

    private void openAnswerQuestionUI(String paperId){
        AnswerQuestionActivity.invoke(this,paperId,Constants.FROM_BC_RESOURCE);
        shouldRefreshWhenResume =true;
    }

    HttpCallback<PaperResponse> mPaperCallback = new EXueELianBaseCallback<PaperResponse>() {
        @Override
        public void onResponse(RequestBase request, PaperResponse ret) {
            if(ret.getStatus().getCode() == 0){
                if(ret.getData().size() > 0){
                    QuestionShowType type = QuestionShowType.ANSWER;
                    Paper paper = new Paper(ret.getData().get(0), type);
                    DataFetcher.getInstance().save(paper.getId(),paper);
                    openAnswerQuestionUI(paper.getId());
                }else {
                    ToastManager.showMsg("试卷为空");
                }
            }else {
                ToastManager.showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };
}
