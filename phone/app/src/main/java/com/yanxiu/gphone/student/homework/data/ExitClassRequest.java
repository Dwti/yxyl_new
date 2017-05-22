package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.base.ExerciseBaseRequest;

/**
 * Created by sp on 17-5-20.
 */

public class ExitClassRequest extends ExerciseBaseRequest {
    protected String classId;
    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/";
    }

    @Override
    protected String urlPath() {
        return "class/exitClass.do";
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
