package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.util.ExerciseRequestBase;

/**
 * Created by sp on 17-5-18.
 */

public class SubjectRequest extends ExerciseRequestBase {
    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/class";
    }

    @Override
    protected String urlPath() {
        return "/listGroups.do";
    }
}
