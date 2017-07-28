package com.yanxiu.gphone.student.exercise.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.exercise.bean.SubjectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-26.
 */

public class SubjectsResponse extends EXueELianBaseResponse {
    private List<SubjectBean> data = new ArrayList<>();

    public List<SubjectBean> getData() {
        return data;
    }

    public void setData(List<SubjectBean> data) {
        this.data = data;
    }
}
