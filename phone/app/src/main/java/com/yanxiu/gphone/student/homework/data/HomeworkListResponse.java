package com.yanxiu.gphone.student.homework.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/5/8.
 */

public class HomeworkListResponse {
    private StatusBean status;
    private PageBean page;
    private List<HomeworkBean> data = new ArrayList<>();

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<HomeworkBean> getData() {
        return data;
    }

    public void setData(List<HomeworkBean> data) {
        this.data = data;
    }
}
