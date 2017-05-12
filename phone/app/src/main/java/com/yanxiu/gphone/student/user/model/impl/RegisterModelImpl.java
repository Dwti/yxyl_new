package com.yanxiu.gphone.student.user.model.impl;

import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.user.model.interf.RegisterModel;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 13:59.
 * Function :
 */

public class RegisterModelImpl implements RegisterModel<BaseBean> {
    @Override
    public void getVerificationCode(String mobile, OnHttpFinishedListener<BaseBean> listener) {

    }

    @Override
    public void onRegister(String mobile, String verCode, String passWord, OnHttpFinishedListener<BaseBean> listener) {

    }
}
