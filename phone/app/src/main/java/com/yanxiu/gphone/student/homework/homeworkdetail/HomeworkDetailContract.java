package com.yanxiu.gphone.student.homework.homeworkdetail;

import com.yanxiu.gphone.student.base.BasePresenter;
import com.yanxiu.gphone.student.base.BaseView;
import com.yanxiu.gphone.student.homework.response.HomeworkDetailBean;

import java.util.List;

/**
 * Created by sp on 17-5-22.
 */

public interface HomeworkDetailContract {

    interface  View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void setLoadingMoreIndicator(boolean active);

        void showHomework(List<HomeworkDetailBean> homeworkList);

        void showDataEmpty();

        void showDataError();

        void openAnswerQuestionUI(String key);

        void openAnswerReportUI(String key);

        void openAnalysisQuestionUI(String key);

        void showNoMoreData();

        void setLoadMoreEnable(boolean enable);

        void showCanNotViewReport(String msg);

        void showLoadMoreDataError(String msg);

        void showGetPaperDataError(String msg);

        void showGetAnalysisDataError(String msg);

        boolean isActive();

        void finishUI();
    }

    interface Presenter extends BasePresenter{

        boolean shouldRefresh();

        void resetRefreshState();

        void setSubjectId(String subjectId);

        void loadHomework();

        void loadMoreHomework();

        void getPaper(String paperId,int status);

        void getReport(String paperId);

        void finishUI();
    }
}
