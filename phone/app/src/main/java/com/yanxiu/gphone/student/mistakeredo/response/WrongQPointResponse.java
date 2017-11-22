package com.yanxiu.gphone.student.mistakeredo.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.mistakeredo.bean.WrongQPointBean;

import java.util.List;

/**
 * Created by sp on 17-11-21.
 */

public class WrongQPointResponse extends EXueELianBaseResponse {
    private List<WrongQPointBean> data;

    public List<WrongQPointBean> getData() {
        return data;
    }

    public void setData(List<WrongQPointBean> data) {
        this.data = data;
    }
}
