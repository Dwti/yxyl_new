package com.yanxiu.gphone.student.login.model.interf;

import com.yanxiu.gphone.student.login.interf.OnHttpFinishedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:44.
 * Function :
 */

public interface LoginModel<T> {
    void LoginByAccount(String user_name, String pass_word, OnHttpFinishedListener<T> listener);
    void LoginByWX(OnHttpFinishedListener<T> listener);
    void LoginByQQ(OnHttpFinishedListener<T> listener);
}
