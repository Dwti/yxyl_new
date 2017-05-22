package com.yanxiu.gphone.student.homework;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.ExerciseBaseCallback;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailBean;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailRequest;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-5-18.
 */

public class HomeworkDetailActivity extends Activity implements HomeworkDetailContract.View{
    public static final String EXTRA_SUBJECT_ID = "HOMEWORK_ID";

    private List<HomeworkDetailBean> mHomeworkList = new ArrayList<>();

    private HomeworkDetailAdapter mHomeworkDetailAdapter;

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsLoadingMore = false;

    private HomeworkDetailContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);

        View back = findViewById(R.id.iv_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mHomeworkDetailAdapter = new HomeworkDetailAdapter(mHomeworkList,mItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mHomeworkDetailAdapter);

        String homeworkId = getIntent().getStringExtra(EXTRA_SUBJECT_ID);

        HomeworkDetailPresenter presenter = new HomeworkDetailPresenter(homeworkId,HomeworkDetailRepository.getInstance(),this);
        setPresenter(presenter);
        mPresenter.start();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.finishUI();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if(!mIsLoadingMore && lastVisibleItemPosition + 1 == totalItemCount){
                        mIsLoadingMore = true;
                        mPresenter.loadMoreHomework();
                    }
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadHomework();
            }
        });
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
        mHomeworkDetailAdapter.replaceData(homeworkList);
    }

    @Override
    public void showDataEmpty() {
        //TODO
    }

    @Override
    public void showDataError() {
        //TODO
    }

    @Override
    public void showApplyingForClass() {
        //TODO
    }

    @Override
    public void openJoinClassUI() {
        //TODO
    }

    @Override
    public void openAnswerQuestionUI() {
        //TODO
    }

    @Override
    public void showNoMoreData() {
        //TODO
    }

    @Override
    public void showLoadMoreDataError(String msg) {
        //TODO
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

    HomeworkDetailAdapter.HomeworkItemClickListener mItemClickListener = new HomeworkDetailAdapter.HomeworkItemClickListener() {
        @Override
        public void onHomeworkClick(HomeworkDetailBean homework) {
            mPresenter.openAnswerQuestion(homework);
        }

        @Override
        public void onLoadMoreClick() {
            if(!mIsLoadingMore){
                mIsLoadingMore = true;
                mPresenter.loadMoreHomework();
            }
        }
    };
}
