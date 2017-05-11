package com.yanxiu.gphone.student.login.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:57.
 * Function :
 */

public interface OnHttpFinishedListener<T> {
    /**
     * Request error
     * */
    void onRequestFail(int uuid, String msg);
    /**
     * Request success but return data error
     * */
    void onReturnError(int uuid, T t);
    /**
     * Request success and return data true
     * */
    void onSuccess(int uuid, T t);

}
