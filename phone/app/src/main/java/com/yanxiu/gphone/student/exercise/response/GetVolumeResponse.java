package com.yanxiu.gphone.student.exercise.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.exercise.bean.EditionChildBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-9-13.
 */

public class GetVolumeResponse extends EXueELianBaseResponse {
    private List<EditionChildBean> data = new ArrayList<>();

    public List<EditionChildBean> getData() {
        return data;
    }

    public void setData(List<EditionChildBean> data) {
        this.data = data;
    }
}
