package com.yanxiu.gphone.student.homework.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

import java.util.List;

/**
 * Created by sp on 17-5-18.
 */

public class SubjectResponse extends EXueELianBaseResponse {
    private PropertyBean property;
    private List<SubjectBean> data;

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
