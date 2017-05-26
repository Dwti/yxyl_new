package com.yanxiu.gphone.student.homework.homeworkdetail;

import com.yanxiu.gphone.student.base.BasePresenter;
import com.yanxiu.gphone.student.base.BaseView;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailBean;

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

        void showNoMoreData();

        void showLoadMoreDataError(String msg);

        boolean isActive();

        void finishUI();
    }

    interface Presenter extends BasePresenter{

        void loadHomework();

        void loadMoreHomework();

        void getPaper(String paperId);

        void finishUI();
    }
}
