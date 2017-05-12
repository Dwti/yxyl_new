package com.yanxiu.gphone.student.user.presenter.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 17:06.
 * Function :
 */

public interface LoginPresenter extends BasePresenter{
    void setUserNameChange();
    void setPassWorkChange();
    void LoginByAccount(String user_name,String pass_word);
    void LoginByWX();
    void LoginByQQ();
}
