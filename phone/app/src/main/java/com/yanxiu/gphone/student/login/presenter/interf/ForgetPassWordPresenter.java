package com.yanxiu.gphone.student.login.presenter.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/11 11:22.
 * Function :
 */

public interface ForgetPassWordPresenter extends BasePresenter {
    void sendVerCode(String mobile);

    void onNext(String mobile, String verCode);

    void setMobileChange();
}
