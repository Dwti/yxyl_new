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

public class HomeworkDetailActivity extends Activity {
    public static final String EXTRA_SUBJECT_ID = "HOMEWORK_ID";
    private int mPageIndex = 1;
    private int mTotalPage = 0;
    private String mHomeworkId;
    private List<HomeworkDetailBean> mHomeworkList = new ArrayList<>();
    private HomeworkDetailAdapter mHomeworkDetailAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsLoadingMore = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        View back = findViewById(R.id.iv_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mHomeworkDetailAdapter = new HomeworkDetailAdapter(mHomeworkList,mItemClickListener,mLoadMoreItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mHomeworkDetailAdapter);
        mHomeworkId = getIntent().getStringExtra(EXTRA_SUBJECT_ID);
        loadHomework(1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                        loadMoreHomework();
                    }
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageIndex = 1;
                setLoadingIndicator(true);
                loadHomework(1);
            }
        });
    }

    private void loadHomework(int pageIndex) {
        HomeworkDetailRequest request = new HomeworkDetailRequest();
        request.setGroupId(mHomeworkId);
        request.setPage(pageIndex+"");
        request.startRequest(HomeworkDetailResponse.class,mLoadHomeworkCallback);

    }

    private void loadMoreHomework(){
        if(mPageIndex >= mTotalPage){
            return;
        }
        mPageIndex++;
        mIsLoadingMore = true;
        setLoadingMoreIndicator(true);
        loadHomework(mPageIndex);
    }

    public void setLoadingIndicator(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }

    public void setLoadingMoreIndicator(boolean active) {
        if(active){
            mHomeworkDetailAdapter.addFooterView();
        }else {
            mIsLoadingMore = false;
            mHomeworkDetailAdapter.removeFooterView();
        }
    }

    HomeworkDetailAdapter.HomeworkItemClickListener mItemClickListener = new HomeworkDetailAdapter.HomeworkItemClickListener() {
        @Override
        public void onHomeworkClick(HomeworkDetailBean homework) {
           //TODO 点击进入答题
        }
    };

    HomeworkDetailAdapter.HomeworkLoadMoreItemClickListener mLoadMoreItemClickListener = new HomeworkDetailAdapter.HomeworkLoadMoreItemClickListener() {
        @Override
        public void onLoadMoreClick() {
            if(!mIsLoadingMore){
                loadMoreHomework();
            }
        }
    };

    HttpCallback<HomeworkDetailResponse> mLoadHomeworkCallback = new ExerciseBaseCallback<HomeworkDetailResponse>() {
        @Override
        public void onSuccess(RequestBase request, HomeworkDetailResponse ret) {
            super.onSuccess(request,ret);
            if(ret.getStatus().getCode() ==0){
                if(mPageIndex ==1){
                    mHomeworkList = ret.getData();
                }else if(mPageIndex > 1){
                    mHomeworkList.addAll(ret.getData());
                }
                mTotalPage = ret.getPage().getTotalPage();
                mHomeworkDetailAdapter.replaceData(mHomeworkList);
            }

            //TODO 要考虑先上拉没返回的同时下拉的情况
            if(mPageIndex == 1){
                setLoadingIndicator(false);
            }else if(mPageIndex > 1){
                setLoadingMoreIndicator(false);
            }
            //TODO 错误的时候没有处理(别的界面也没有处理) 上拉刷新跟上拉加载是两种不同的处理
        }

        @Override
        public void onFail(RequestBase request, Error error) {

        }
    };
}
