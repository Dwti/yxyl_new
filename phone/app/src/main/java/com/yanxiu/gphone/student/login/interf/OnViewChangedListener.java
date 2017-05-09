package com.yanxiu.gphone.student.login.interf;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 17:48.
 * Function :
 */

public interface OnViewChangedListener<T> {
    /**
     * Request start
     */
    void onHttpStart(int uuid);

    /**
     * Request success but return data error
     */
    void onReturntError(int uuid, T t);

    /**
     * Request success and return data true
     */
    void onSuccess(int uuid, T t);

    /**
     * Request cancel
     */
    void onCancel(int uuid);

    /**
     * Request fail and network error
     */
    void onNetWorkError(int uuid, String msg);

    /**
     * Request fail and data error
     */
    void onDataError(int uuid, String msg);

    /**
     * Request finish
     */
    void onHttpFinished(int uuid);
}
