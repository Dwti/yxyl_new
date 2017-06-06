package com.yanxiu.gphone.student.homework.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.questions.bean.PaperBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class PaperResponse extends EXueELianBaseResponse {

    private List<PaperBean> data;

    public List<PaperBean> getData() {
        return data;
    }

    public void setData(List<PaperBean> data) {
        this.data = data;
    }

}
