package com.yanxiu.gphone.student.login.presenter.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 13:52.
 * Function :
 */

public interface RegisterPresenter extends BasePresenter{
    void getVerCode(String mobile);
    void onRegister(String mobile,String verCode);
}
