package com.yanxiu.gphone.student.homework.homeworkdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.response.HomeworkDetailBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnalysisQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerReportActicity;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-5-18.
 */

public class HomeworkDetailActivity extends Activity implements HomeworkDetailContract.View{
    public static final String EXTRA_SUBJECT_ID = "SUBJECT_ID";
    public static final String EXTRA_SUBJECT_NAME = "SUBJECT_NAME";

    private List<HomeworkDetailBean> mHomeworkList = new ArrayList<>();

    private HomeworkDetailAdapter mHomeworkDetailAdapter;

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mTipsView, mBack;

    private TextView mTitle, mTips;

    private Button mRefreshBtn;

    private boolean mIsLoadingMore = false;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mTipsView = findViewById(R.id.tips_layout);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);

        mHomeworkDetailAdapter = new HomeworkDetailAdapter(mHomeworkList,mItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mHomeworkDetailAdapter);

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

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            int totalItemCount ;
            int lastVisibleItemPosition;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if(dy > 0 && !mIsLoadingMore && lastVisibleItemPosition + 1 == totalItemCount){
                    mIsLoadingMore = true;
                    mPresenter.loadMoreHomework();
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadHomework();
            }
        });
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeworkDetailAdapter.clearData();
                showContentView();
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
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void setLoadingMoreIndicator(boolean active) {
        if(active){
            mHomeworkDetailAdapter.addFooterView();
        }else {
            mIsLoadingMore = false;
            mHomeworkDetailAdapter.removeFooterView();
        }
    }

    @Override
    public void showHomework(List<HomeworkDetailBean> homeworkList) {
        showContentView();
        mHomeworkDetailAdapter.replaceData(homeworkList);
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
        mIsLoadingMore = false;
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
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
    }

    private void showDataEmptyView(){
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.class_no_homework);
        mRefreshBtn.setText(R.string.click_to_refresh);
    }

    private void showDataErrorView(){
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
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
            if(!mIsLoadingMore){
                mIsLoadingMore = true;
                mPresenter.loadMoreHomework();
            }
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
