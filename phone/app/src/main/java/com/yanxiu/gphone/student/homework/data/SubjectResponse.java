package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.base.StatusBean;

import java.util.List;

/**
 * Created by sp on 17-5-18.
 */

public class SubjectResponse {
    private StatusBean status;
    private PropertyBean property;
    private List<SubjectBean> data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public PropertyBean getProperty() {
        return property;
    }

    public void setProperty(PropertyBean property) {
        this.property = property;
    }

    public List<SubjectBean> getData() {
        return data;
    }

    public void setData(List<SubjectBean> data) {
        this.data = data;
    }
}
