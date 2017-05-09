package com.yanxiu.gphone.student.login.presenter.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 17:06.
 * Function :
 */

public interface LoginPresenter {
    void LoginByAccount(String user_name,String pass_word);
    void LoginByWX();
    void LoginByQQ();
    void onDestory();
}
