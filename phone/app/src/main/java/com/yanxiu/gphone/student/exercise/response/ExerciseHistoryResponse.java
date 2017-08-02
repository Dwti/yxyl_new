package com.yanxiu.gphone.student.exercise.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.exercise.bean.ExerciseBean;
import com.yanxiu.gphone.student.homework.response.PageBean;

import java.util.List;

/**
 * Created by sp on 17-8-2.
 */

public class ExerciseHistoryResponse extends EXueELianBaseResponse {
    protected PageBean page;
    protected List<ExerciseBean> data;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<ExerciseBean> getData() {
        return data;
    }

    public void setData(List<ExerciseBean> data) {
        this.data = data;
    }
}
