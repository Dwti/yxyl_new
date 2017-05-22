package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.base.ExerciseBaseRequest;

/**
 * Created by sp on 17-5-20.
 */

public class JoinClassRequest extends ExerciseBaseRequest {
    protected String validMsg;
    protected String classId;
    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/";
    }

    @Override
    protected String urlPath() {
        return "class/joinClass.do";
    }

    public String getValidMsg() {
        return validMsg;
    }

    public void setValidMsg(String validMsg) {
        this.validMsg = validMsg;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
