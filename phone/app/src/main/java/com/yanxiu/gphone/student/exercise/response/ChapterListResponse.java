package com.yanxiu.gphone.student.exercise.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.exercise.bean.ChapterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class ChapterListResponse extends EXueELianBaseResponse {

    protected List<ChapterBean> data = new ArrayList<>();

    public List<ChapterBean> getData() {
        return data;
    }

    public void setData(List<ChapterBean> data) {
        this.data = data;
    }
}
