package com.yanxiu.gphone.student.login.model.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 13:57.
 * Function :
 */

public interface RegisterModel {
    void getVerificationCode(String mobile);
    void onRegister(String mobile,String verCode);
}
