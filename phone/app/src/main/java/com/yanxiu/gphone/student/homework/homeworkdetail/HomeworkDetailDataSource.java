package com.yanxiu.gphone.student.homework.homeworkdetail;

import com.yanxiu.gphone.student.homework.response.HomeworkDetailBean;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;

import java.util.List;

/**
 * Created by sp on 17-5-22.
 */

public interface HomeworkDetailDataSource {
    interface LoadHomeworkDetailCallback{

        void onHomeworkDetailLoaded(List<HomeworkDetailBean> homeworkDetails);

        void onDataEmpty();

        void onDataError(int code, String msg);
    }

    interface LoadPaperCallback{

        void onPaperLoaded(Paper paper);

        void onDataEmpty();

        void onDataError(int code, String msg);
    }

    interface LoadReportCallback {

        void onAnalysisLoaded(Paper paper);

        void onDataEmpty();

        void onDataError(int code, String msg);
    }

    void getHomeworkDetails(String homeworkId, LoadHomeworkDetailCallback loadHomeworkDetailCallback);

    void getMoreHomeworkDetails(String homeworkId, LoadHomeworkDetailCallback loadHomeworkDetailCallback);

    void getPaper(String paperId, int status, LoadPaperCallback loadPaperCallback);

    void getReport(String paperId, LoadReportCallback loadReportCallback);
}
