package com.yanxiu.gphone.student.exercise.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.exercise.bean.KnowledgePointBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class KnowledgePointResponse extends EXueELianBaseResponse {

    List<KnowledgePointBean> data = new ArrayList<>();

    public List<KnowledgePointBean> getData() {
        return data;
    }

    public void setData(List<KnowledgePointBean> data) {
        this.data = data;
    }
}
