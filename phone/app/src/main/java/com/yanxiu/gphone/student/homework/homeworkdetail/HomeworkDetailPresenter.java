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
    public void loadHomework() {
        mHomeworkDetailView.setLoadingIndicator(true);
        mHomeworkRepository.getHomeworkDetails(mHomeworkId, new HomeworkDetailDataSource.LoadHomeworkDetailCallback() {
            @Override
            public void onHomeworkDetailLoaded(List<HomeworkDetailBean> homeworkDetails) {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.showHomework(homeworkDetails);
                mHomeworkDetailView.setLoadingIndicator(false);
            }

            @Override
            public void onDataEmpty() {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.setLoadingIndicator(false);
                mHomeworkDetailView.showDataEmpty();
            }

            @Override
            public void onDataError(int code, String msg) {
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
            mHomeworkDetailView.showNoMoreData();
            return;
        }
        mHomeworkDetailView.setLoadingMoreIndicator(true);
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
    public void getPaper(final String paperId) {
        mHomeworkRepository.getPaper(paperId, new HomeworkDetailDataSource.LoadPaperCallback() {
            @Override
            public void onPaperLoaded(Paper paper) {
                if(!mHomeworkDetailView.isActive()){
                    return;
                }
                mHomeworkDetailView.openAnswerQuestionUI(paper.getId());
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
    public void finishUI() {
        if(!mHomeworkDetailView.isActive()){
            mHomeworkDetailView.finishUI();
        }
    }
}
