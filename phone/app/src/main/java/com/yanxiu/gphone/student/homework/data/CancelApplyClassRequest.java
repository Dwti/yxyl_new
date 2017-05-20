package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.util.ExerciseRequestBase;

/**
 * Created by sp on 17-5-20.
 */

public class CancelApplyClassRequest extends ExerciseRequestBase {
    protected String classId;
    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/";
    }

    @Override
    protected String urlPath() {
        return "class/cancelReply.do";
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
