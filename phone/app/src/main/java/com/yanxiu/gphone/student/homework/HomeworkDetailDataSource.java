package com.yanxiu.gphone.student.homework;

import com.yanxiu.gphone.student.homework.data.HomeworkDetailBean;

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

    void getHomeworkDetails(String homeworkId, LoadHomeworkDetailCallback loadHomeworkDetailCallback);

    void getMoreHomeworkDetails(String homeworkId,LoadHomeworkDetailCallback loadHomeworkDetailCallback);
}
