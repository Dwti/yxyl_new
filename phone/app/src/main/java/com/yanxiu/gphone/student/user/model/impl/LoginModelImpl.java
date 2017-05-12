package com.yanxiu.gphone.student.user.model.impl;

import com.yanxiu.gphone.student.user.bean.LoginBean;
import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.user.model.interf.LoginModel;
import com.yanxiu.gphone.student.user.presenter.impl.LoginPresenterImpl;

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
