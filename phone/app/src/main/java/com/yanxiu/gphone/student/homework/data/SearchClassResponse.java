package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.base.StatusBean;

import java.util.List;

/**
 * Created by sp on 17-5-19.
 */

public class SearchClassResponse {
    private StatusBean status;
    private List<ClassBean> data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public List<ClassBean> getData() {
        return data;
    }

    public void setData(List<ClassBean> data) {
        this.data = data;
    }
}
