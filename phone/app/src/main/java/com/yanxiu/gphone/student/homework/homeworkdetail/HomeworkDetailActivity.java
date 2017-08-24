package com.yanxiu.gphone.student.homework.homeworkdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.srt.refresh.EXueELianRefreshLayout;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.homework.response.HomeworkDetailBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnalysisQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerReportActicity;
import com.yanxiu.gphone.student.userevent.UserEventManager;
import com.yanxiu.gphone.student.userevent.bean.WorkBean;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-5-18.
 */

public class HomeworkDetailActivity extends YanxiuBaseActivity implements HomeworkDetailContract.View{
    public static final String EXTRA_SUBJECT_ID = "SUBJECT_ID";
    public static final String EXTRA_SUBJECT_NAME = "SUBJECT_NAME";

    private List<HomeworkDetailBean> mHomeworkList = new ArrayList<>();

    private HomeworkDetailAdapter mHomeworkDetailAdapter;

    private EXueELianRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private LoadingView mLoadingView;

    private ImageView mTipsImg;

    private View mTipsView, mBack;

    private TextView mTitle, mTips;

    private Button mRefreshBtn;

    private HomeworkDetailContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
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

        mHomeworkDetailAdapter = new HomeworkDetailAdapter(mHomeworkList,mItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mHomeworkDetailAdapter);

        mRefreshLayout.setLoadMoreEnable(true);

        String subjectId = getIntent().getStringExtra(EXTRA_SUBJECT_ID);
        String subjectName = getIntent().getStringExtra(EXTRA_SUBJECT_NAME);
        mTitle.setText(subjectName);

        HomeworkDetailPresenter presenter = new HomeworkDetailPresenter(subjectId,HomeworkDetailRepository.getInstance(),this);
        setPresenter(presenter);
        mPresenter.start();

    }

    private void initListener(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.finishUI();
            }
        });

        mRefreshLayout.setRefreshListener(new EXueELianRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh(EXueELianRefreshLayout refreshLayout) {
                mPresenter.loadHomework();
            }

            @Override
            public void onLoadMore(EXueELianRefreshLayout refreshLayout) {
                mPresenter.loadMoreHomework();
            }
        });
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.showLoadingView();
                mPresenter.loadHomework();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPresenter.shouldRefresh()){
            mPresenter.loadHomework();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(!active){
            mLoadingView.hiddenLoadingView();
            mRefreshLayout.finishRefreshing();
        }
    }

    @Override
    public void setLoadingMoreIndicator(boolean active) {
        if(!active){
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void showHomework(List<HomeworkDetailBean> homeworkList) {
        showContentView();
        mHomeworkDetailAdapter.replaceData(homeworkList);
        uploadUserEvent(homeworkList);
    }

    @Override
    public void showDataEmpty() {
        showDataEmptyView();
    }

    @Override
    public void showDataError() {
        showDataErrorView();
    }

    @Override
    public void openAnswerQuestionUI(String key) {
        AnswerQuestionActivity.invoke(HomeworkDetailActivity.this,key);
    }

    @Override
    public void openAnswerReportUI(String key) {
        AnswerReportActicity.invoke(HomeworkDetailActivity.this,key);
    }

    @Override
    public void openAnalysisQuestionUI(String key) {
        AnalysisQuestionActivity.invoke(HomeworkDetailActivity.this,key);
    }

    @Override
    public void showNoMoreData() {
        //TODO
    }

    @Override
    public void showCanNotViewReport(String msg) {
        ToastManager.showMsg(msg);
    }

    @Override
    public void showLoadMoreDataError(String msg) {
        //TODO
    }

    private void uploadUserEvent(List<HomeworkDetailBean> list){
        List<WorkBean> works = new ArrayList<>();
        for(HomeworkDetailBean bean : list){
            WorkBean work = new WorkBean();
            work.volume = bean.getVolume();
            work.questionNum = bean.getQuesnum();
            works.add(work);
        }
        UserEventManager.getInstense().whenReceiveWork(works);
    }

    @Override
    public void showGetPaperDataError(String msg) {
        ToastManager.showMsg(msg);
    }

    @Override
    public void showGetAnalysisDataError(String msg) {
        ToastManager.showMsg(msg);
    }

    @Override
    public boolean isActive() {
        return !(isDestroyed() || isFinishing());
    }

    @Override
    public void finishUI() {
        finish();
    }

    @Override
    public void setPresenter(HomeworkDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void showContentView(){
        mRefreshLayout.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
    }

    private void showDataEmptyView(){
        mRefreshLayout.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
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

    HomeworkDetailAdapter.HomeworkItemClickListener mItemClickListener = new HomeworkDetailAdapter.HomeworkItemClickListener() {
        @Override
        public void onHomeworkClick(HomeworkDetailBean homework) {
            if(homework.getPaperStatus().getStatus() == HomeworkDetailPresenter.STATUS_FINISHED){
                if("0".equals(homework.getShowana())){
                    mPresenter.getReport(homework.getId());
                }else {
                    showCanNotViewReport(String.format(getString(R.string.can_not_view_report),homework.getOverTime()));
                }
            }else {
                mPresenter.getPaper(homework.getId(),homework.getPaperStatus().getStatus());
            }
        }

        @Override
        public void onLoadMoreClick() {

        }
    };

    /**
     * 跳转HomeworkDetailActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity,String id , String name) {
        Intent intent = new Intent(activity, HomeworkDetailActivity.class);
        intent.putExtra(HomeworkDetailActivity.EXTRA_SUBJECT_ID,id);
        intent.putExtra(HomeworkDetailActivity.EXTRA_SUBJECT_NAME,name);
        activity.startActivity(intent);
    }
}
