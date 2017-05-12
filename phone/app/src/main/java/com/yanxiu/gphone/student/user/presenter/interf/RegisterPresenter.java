package com.yanxiu.gphone.student.user.presenter.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 13:52.
 * Function :
 */

public interface RegisterPresenter extends BasePresenter {
    void sendVerCode(String mobile);

    void onRegister(String mobile, String verCode, String passWrod);
    void setMobileChange();
    void setPassWordChange();
}
