package com.yanxiu.gphone.student.userevent;

import android.text.TextUtils;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.userevent.bean.UserEventBean;
import com.yanxiu.gphone.student.userevent.bean.WorkBean;
import com.yanxiu.gphone.student.userevent.request.UploadUserEventRequest;
import com.yanxiu.gphone.student.userevent.response.UploadUserEventResponse;
import com.yanxiu.gphone.student.userevent.util.EventDataUtils;
import com.yanxiu.gphone.student.util.Logger;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 打点
 * <p>
 * Created by Canghaixiao.
 * Time : 2017/8/7 11:06.
 * Function :
 */
public class UserEventManager {

    private static final String TAG = "UserEventManager";
    private static final String RESULT_OK = "ok";

    private static UserEventManager mEventManager;

    public static UserEventManager getInstense() {
        if (mEventManager == null) {
            mEventManager = new UserEventManager();
        }
        return mEventManager;
    }

    /**
     * 注册成功
     */
    public void whenRegistSuccess() {
        startRequest(EventDataUtils.getRegistSuccessMap());
    }

    /**
     * 每次启动
     */
    public void whenStartApp() {
        startRequest(EventDataUtils.getStartAppMap());
        List<UserEventBean> list = DataSupport.findAll(UserEventBean.class);
        if (list != null) {
            for (UserEventBean eventBean : list) {
                startRequest(eventBean.data, eventBean);
            }
        }
    }

    /**
     * 提交练习/作业
     *
     * @param bedition    教材版本
     * @param gradeId     年级ID
     * @param paperType   试卷类型 0，练习，1，作业
     * @param questionNum 题目数量
     * @param subjectId   学科ID
     * @param questionId  [qid,qid,qid...]
     */
    public void whenSubmitWork(String bedition, String gradeId, String subjectId, String paperType, String questionNum, String questionId) {
        startRequest(EventDataUtils.getSubmitWorkMap(bedition, gradeId, subjectId, paperType, questionNum, questionId));
    }

    /**
     * 收到作业(每份作业统计一次)
     */
    public void whenReceiveWork(List<WorkBean> list) {
        startRequest(EventDataUtils.getReceiveWorkMap(list));
    }

    /**
     * 进入练习
     */
    public void whenEnterWork() {
        startRequest(EventDataUtils.getEnterWorkMap());
    }

    /**
     * 进入后台
     */
    public void whenEnterBack() {
        startRequest(EventDataUtils.getEnterBackMap());
    }

    /**
     * 进入前台
     */
    public void whenEnterFront() {
        startRequest(EventDataUtils.getEnterFrontMap());
    }

    /**
     * 退出app
     */
    public void whenExitApp() {
        startRequest(EventDataUtils.getExitAppMap());
    }

    /**
     * 加入班级成功
     */
    public void whenEnterClass() {
        startRequest(EventDataUtils.getEnterClassMap());
    }

    /**
     * 首次启动
     */
    public void whenFirstStart() {
        startRequest(EventDataUtils.getFirstStartMap());
    }

    /**
     * 跳出BC资源
     *
     * @param duration 使用时长
     * @param resId    资源id
     */
    public void whenExitBcWork(String duration, String resId) {
        startRequest(EventDataUtils.getExitBcWorkMap(duration, resId));
    }

    /**
     * 完成BC资源
     *
     * @param duration 使用时长
     * @param accuracy 正确率
     * @param resId    资源id
     */
    public void whenSubmitBcWork(String duration, String accuracy, String resId) {
        startRequest(EventDataUtils.getSubmitBcWorkMap(duration, accuracy, resId));
    }

    private void startRequest(String fileData) {
        startRequest(fileData, null);
    }

    private void startRequest(final String fileData, final UserEventBean eventBean) {
        if (TextUtils.isEmpty(fileData)) {
            return;
        }
        UploadUserEventRequest userEventRequest = new UploadUserEventRequest();
        userEventRequest.yxyl_statistic = fileData;
        userEventRequest.startRequest(UploadUserEventResponse.class, new HttpCallback<UploadUserEventResponse>() {
            @Override
            public void onSuccess(RequestBase request, UploadUserEventResponse ret) {
                if (ret != null && ret.getResult().equals(RESULT_OK)) {
                    if (eventBean != null) {
                        eventBean.delete();
                    }
                    Logger.d(TAG, "request success");
                } else {
                    if (eventBean == null) {
                        saveToDb(fileData);
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                if (eventBean == null) {
                    saveToDb(fileData);
                }
            }
        });
    }

    private void saveToDb(String data) {
        if (data != null) {
            try {
                UserEventBean eventBean = new UserEventBean(data);
                Logger.d(TAG, data);
                eventBean.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
