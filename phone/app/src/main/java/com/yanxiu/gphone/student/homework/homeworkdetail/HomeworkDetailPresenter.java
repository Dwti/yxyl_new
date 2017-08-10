package com.yanxiu.gphone.student.homework.homeworkdetail;

import com.yanxiu.gphone.student.homework.response.HomeworkDetailBean;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;

import java.util.List;

/**
 * Created by sp on 17-5-22.
 */

public class HomeworkDetailPresenter implements HomeworkDetailContract.Presenter {
    private final HomeworkDetailRepository mHomeworkRepository;

    private final HomeworkDetailContract.View mHomeworkDetailView;

    public static final int STATUS_TODO = 0;   //待完成
    public static final int STATUS_UNSUBMMIT = 1;  //逾期未交
    public static final int STATUS_FINISHED = 2;  //已完成

    private boolean mShouldRefreshData = false;  //进入答题界面之后回来需要刷新数据
    private String mHomeworkId;

    public HomeworkDetailPresenter(String homeworkId, HomeworkDetailRepository mHomeworkRepository, HomeworkDetailContract.View mHomeworkDetailView) {
        this.mHomeworkId = homeworkId;
        this.mHomeworkRepository = mHomeworkRepository;
        this.mHomeworkDetailView = mHomeworkDetailView;
    }


    @Override
    public void start() {
        loadHomework();
    }

    @Override
    public boolean shouldRefresh() {
        return mShouldRefreshData;
    }

    @Override
    public void loadHomework() {
        mHomeworkRepository.getHomeworkDetails(mHomeworkId, new HomeworkDetailDataSource.LoadHomeworkDetailCallback() {
            @Override
            public void onHomeworkDetailLoaded(List<HomeworkDetailBean> homeworkDetails) {
                mShouldRefreshData = false;
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.showHomework(homeworkDetails);
                mHomeworkDetailView.setLoadingIndicator(false);
            }

            @Override
            public void onDataEmpty() {
                mShouldRefreshData = false;
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.setLoadingIndicator(false);
                mHomeworkDetailView.showDataEmpty();
            }

            @Override
            public void onDataError(int code, String msg) {
                mShouldRefreshData = false;
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.setLoadingIndicator(false);
                mHomeworkDetailView.showDataError();
            }
        });
    }

    @Override
    public void loadMoreHomework() {
        if(!mHomeworkRepository.canLoadMore()){
            mHomeworkDetailView.setLoadingMoreIndicator(false);
            mHomeworkDetailView.showNoMoreData();
            return;
        }
        mHomeworkRepository.getMoreHomeworkDetails(mHomeworkId, new HomeworkDetailDataSource.LoadHomeworkDetailCallback() {
            @Override
            public void onHomeworkDetailLoaded(List<HomeworkDetailBean> homeworkDetails) {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.setLoadingMoreIndicator(false);
                mHomeworkDetailView.showHomework(homeworkDetails);
            }

            @Override
            public void onDataEmpty() {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.setLoadingMoreIndicator(false);
                mHomeworkDetailView.showNoMoreData();
            }

            @Override
            public void onDataError(int code, String msg) {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.setLoadingMoreIndicator(false);
                mHomeworkDetailView.showLoadMoreDataError(msg);
            }
        });
    }

    @Override
    public void getPaper(final String paperId, final int status) {
        mHomeworkRepository.getPaper(paperId, status, new HomeworkDetailDataSource.LoadPaperCallback() {
            @Override
            public void onPaperLoaded(Paper paper) {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                if(status == STATUS_UNSUBMMIT){
                    mHomeworkDetailView.openAnalysisQuestionUI(paper.getId());
                }else if (status == STATUS_TODO){
                    mShouldRefreshData = true;
                    mHomeworkDetailView.openAnswerQuestionUI(paper.getId());
                }
            }

            @Override
            public void onDataEmpty() {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.showDataEmpty();
            }

            @Override
            public void onDataError(int code, String msg) {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.showGetPaperDataError(msg);
            }
        });
    }

    @Override
    public void getReport(String paperId) {
        mHomeworkRepository.getReport(paperId, new HomeworkDetailDataSource.LoadReportCallback() {
            @Override
            public void onAnalysisLoaded(Paper paper) {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.openAnswerReportUI(paper.getId());
            }

            @Override
            public void onDataEmpty() {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.showDataEmpty();
            }

            @Override
            public void onDataError(int code, String msg) {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.showGetAnalysisDataError(msg);
            }
        });
    }

    @Override
    public void finishUI() {
        mHomeworkDetailView.finishUI();
    }
}
