package com.yanxiu.gphone.student.bcresource.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by sp on 17-10-19.
 */

public class TopicPaperRequest extends EXueELianBaseRequest {
    protected String type;
    protected String id;
    protected String order = "0";   //order： 0-字母升序，1-字母降序，10-浏览数降序
    protected String scope = "0";   //scope： 查询范围 0-全部，1-已做答，2-未作答
    protected String page = "1";    //从1开始
    protected String pageSize = "10";
    @Override
    protected String urlPath() {
        return "topic/getTopicPaperList.do";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
