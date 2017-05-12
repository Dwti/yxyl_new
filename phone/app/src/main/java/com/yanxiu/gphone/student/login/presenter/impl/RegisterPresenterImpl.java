package com.yanxiu.gphone.student.login.presenter.impl;

import com.yanxiu.gphone.student.login.model.impl.RegisterModelImpl;
import com.yanxiu.gphone.student.login.presenter.interf.RegisterPresenter;
import com.yanxiu.gphone.student.login.view.interf.RegisterViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 13:52.
 * Function :
 */

public class RegisterPresenterImpl implements RegisterPresenter {

    private  RegisterViewChangedListener mRegisterViewChangedListener;

    public RegisterPresenterImpl(RegisterViewChangedListener listener){
        this.mRegisterViewChangedListener=listener;
        RegisterModelImpl model=new RegisterModelImpl();
    }

    @Override
    public void onDestory() {

    }

    @Override
    public void getVerCode(String mobile) {

    }

    @Override
    public void onRegister(String mobile, String verCode) {

    }
}
