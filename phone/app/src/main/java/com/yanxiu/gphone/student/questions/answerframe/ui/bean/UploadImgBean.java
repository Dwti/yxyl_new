package com.yanxiu.gphone.student.questions.answerframe.ui.bean;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/10 14:24.
 * Function :
 */
public class UploadImgBean extends EXueELianBaseResponse{
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
