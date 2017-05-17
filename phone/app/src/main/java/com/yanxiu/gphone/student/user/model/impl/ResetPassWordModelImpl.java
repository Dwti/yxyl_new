package com.yanxiu.gphone.student.user.model.impl;

import com.yanxiu.gphone.student.user.bean.ResetPassWordBean;
import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.user.model.interf.ResetPassWordModel;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/16 16:23.
 * Function :
 */
public class ResetPassWordModelImpl implements ResetPassWordModel<ResetPassWordBean> {
    @Override
    public void onReset(String mobile, String verCode, String passWord, OnHttpFinishedListener<ResetPassWordBean> listener) {

    }
}
