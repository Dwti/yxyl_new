package com.yanxiu.gphone.student.user.presenter.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/16 16:12.
 * Function :
 */
public interface ResetPassWordPresenter extends BasePresenter{
    void onResetPassWord(String mobile,String verCode,String passWord);
}
