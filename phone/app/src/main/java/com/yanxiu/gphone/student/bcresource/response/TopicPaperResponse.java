package com.yanxiu.gphone.student.bcresource.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.bcresource.bean.TopicBean;
import com.yanxiu.gphone.student.homework.response.PageBean;

import java.util.List;

/**
 * Created by sp on 17-10-19.
 */

public class TopicPaperResponse extends EXueELianBaseResponse {
    private PageBean page;
    private List<TopicBean> data;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<TopicBean> getData() {
        return data;
    }

    public void setData(List<TopicBean> data) {
        this.data = data;
    }
}
