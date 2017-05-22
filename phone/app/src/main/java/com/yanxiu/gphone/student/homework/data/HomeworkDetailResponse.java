package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.base.ExerciseBaseResponse;
import com.yanxiu.gphone.student.base.StatusBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/5/8.
 */

public class HomeworkDetailResponse extends ExerciseBaseResponse{
    private PageBean page;
    private List<HomeworkDetailBean> data = new ArrayList<>();

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<HomeworkDetailBean> getData() {
        return data;
    }

    public void setData(List<HomeworkDetailBean> data) {
        this.data = data;
    }
}
