package com.yanxiu.gphone.student.learning.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.learning.bean.ChannelChildBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufengqing on 2018/1/25.
 */

public class GetChannelResponse extends EXueELianBaseResponse {
    private List<ChannelChildBean> data = new ArrayList<>();

    public List<ChannelChildBean> getData() {
        return data;
    }

    public void setData(List<ChannelChildBean> data) {
        this.data = data;
    }
}
