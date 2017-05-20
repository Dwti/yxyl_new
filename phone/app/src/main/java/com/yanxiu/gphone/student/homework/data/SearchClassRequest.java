package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.util.ExerciseRequestBase;

/**
 * Created by sp on 17-5-19.
 */

public class SearchClassRequest extends ExerciseRequestBase {
    protected String classId;
    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/class/";
    }

    @Override
    protected String urlPath() {
        return "searchClass.do";
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
