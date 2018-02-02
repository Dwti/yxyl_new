package com.yanxiu.gphone.student.learning.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.learning.bean.VideoDataBean;

import java.util.List;

/**
 * Created by lufengqing on 2018/1/31.
 */

public class GetRelatedCourseResponse extends EXueELianBaseResponse {

    private List<VideoDataBean> data;

    public List<VideoDataBean> getData() {
        return data;
    }

    public void setData(List<VideoDataBean> data) {
        this.data = data;
    }
}
