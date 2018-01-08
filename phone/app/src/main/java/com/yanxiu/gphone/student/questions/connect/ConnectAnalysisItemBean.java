package com.yanxiu.gphone.student.questions.connect;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/12/26 17:31.
 * Function :
 */
public class ConnectAnalysisItemBean extends BaseBean {

    public String text;
    public boolean isExtra;

    public ConnectAnalysisItemBean(String text, boolean isExtra) {
        this.text = text;
        this.isExtra = isExtra;
    }
}
