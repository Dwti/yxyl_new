package com.yanxiu.gphone.student.user.model.interf;

import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/16 16:20.
 * Function :
 */
public interface ResetPassWordModel<T> {
    void onReset(String mobile, String verCode, String passWord, OnHttpFinishedListener<T> listener);
}
