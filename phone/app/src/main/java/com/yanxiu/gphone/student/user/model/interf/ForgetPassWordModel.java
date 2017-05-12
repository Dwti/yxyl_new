package com.yanxiu.gphone.student.user.model.interf;

import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/11 11:28.
 * Function :
 */

public interface ForgetPassWordModel<T> {
    void sendVerCode(String mobile, OnHttpFinishedListener<T> listener);
    void onNext(String mobile,String verCode,OnHttpFinishedListener<T> listener);
}
