package com.yanxiu.gphone.student.user.model.interf;

import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 13:57.
 * Function :
 */

public interface RegisterModel<T> {
    void getVerificationCode(String mobile, OnHttpFinishedListener<T> listener);

    void onRegister(String mobile, String verCode, String passWord, OnHttpFinishedListener<T> listener);
}
