package com.yanxiu.gphone.student.exercise.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.exercise.bean.EditionBeanEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-26.
 */

public class EditionResponse extends EXueELianBaseResponse {
    private List<EditionBeanEx> data = new ArrayList<>();

    public List<EditionBeanEx> getData() {
        return data;
    }

    public void setData(List<EditionBeanEx> data) {
        this.data = data;
    }
}
