package com.yanxiu.gphone.student.login.model.impl;

import com.yanxiu.gphone.student.login.bean.LoginBean;
import com.yanxiu.gphone.student.login.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.login.model.interf.LoginModel;
import com.yanxiu.gphone.student.login.presenter.impl.LoginPresenterImpl;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:45.
 * Function :
 */

public class LoginModelImpl implements LoginModel<LoginBean> {

    @Override
    public void LoginByAccount(String user_name, String pass_word, OnHttpFinishedListener<LoginBean> listener) {
        listener.onSuccess(LoginPresenterImpl.UUID_ACCOUNT,new LoginBean(3));
    }

    @Override
    public void LoginByWX(OnHttpFinishedListener<LoginBean> listener) {
        listener.onSuccess(LoginPresenterImpl.UUID_WX,new LoginBean(3));
    }

    @Override
    public void LoginByQQ(OnHttpFinishedListener<LoginBean> listener) {
        listener.onSuccess(LoginPresenterImpl.UUID_QQ,new LoginBean(3));
    }
}
