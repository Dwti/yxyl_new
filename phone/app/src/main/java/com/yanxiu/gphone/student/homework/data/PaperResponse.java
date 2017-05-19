package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.base.StatusBean;
import com.yanxiu.gphone.student.homework.questions.bean.PaperBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class PaperResponse {
    private List<PaperBean> data;
    private StatusBean status;

    public List<PaperBean> getData() {
        return data;
    }

    public void setData(List<PaperBean> data) {
        this.data = data;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }
}
