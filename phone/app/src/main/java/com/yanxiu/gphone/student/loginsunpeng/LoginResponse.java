package com.yanxiu.gphone.student.loginsunpeng;

import com.yanxiu.gphone.student.homework.data.StatusBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class LoginResponse {
    private StatusBean status;
    private List<UserBean> data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public List<UserBean> getData() {
        return data;
    }

    public void setData(List<UserBean> data) {
        this.data = data;
    }
}
