package com.yanxiu.gphone.student.user.model.impl;

import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.user.model.interf.ForgetPassWordModel;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/11 11:30.
 * Function :
 */

public class ForgetPassWordModelImpl implements ForgetPassWordModel<BaseBean> {
    @Override
    public void sendVerCode(String mobile, OnHttpFinishedListener<BaseBean> listener) {

    }

    @Override
    public void onNext(String mobile, String verCode, OnHttpFinishedListener<BaseBean> listener) {

    }
}
