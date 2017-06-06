package com.yanxiu.gphone.student.homework.response;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sp on 17-5-18.
 */

public class PropertyBean extends BaseBean {
    private String classId;
    private String className;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
