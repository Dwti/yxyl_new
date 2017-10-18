package com.yanxiu.gphone.student.bcresource.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.bcresource.bean.BCWrapperBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-10-18.
 */

public class TopicTreeResponse extends EXueELianBaseResponse {
    protected List<BCWrapperBean> data = new ArrayList<>();

    public List<BCWrapperBean> getData() {
        return data;
    }

    public void setData(List<BCWrapperBean> data) {
        this.data = data;
    }
}
