package com.yanxiu.gphone.student.login.presenter.impl;

import com.yanxiu.gphone.student.login.bean.LoginBean;
import com.yanxiu.gphone.student.login.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.login.model.impl.LoginModelImpl;
import com.yanxiu.gphone.student.login.presenter.interf.LoginPresenter;
import com.yanxiu.gphone.student.login.view.interf.LoginViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 17:16.
 * Function :
 */

public class LoginPresenterImpl implements LoginPresenter, OnHttpFinishedListener<LoginBean> {

    private interface GetViewChangedListener {
        void set(LoginViewChangedListener listener);
    }

    private interface GetModelImpl {
        void set(LoginModelImpl model);
    }

    /**
     * Login using the account password
     */
    public static final int UUID_ACCOUNT = 0x000;
    /**
     * Login using the wx
     */
    public static final int UUID_WX = 0x001;
    /**
     * Login using the qq
     */
    public static final int UUID_QQ = 0x002;

    private LoginModelImpl mLoginModelImpl;
    private LoginViewChangedListener mLoginViewChangedListener;

    public LoginPresenterImpl(LoginViewChangedListener listener) {
        this.mLoginViewChangedListener = listener;
        this.mLoginModelImpl = new LoginModelImpl();
    }

    @Override
    public void LoginByAccount(String user_name, String pass_word) {
        getListener(listener -> listener.onHttpStart(UUID_ACCOUNT));
        getModel(model -> model.LoginByAccount(user_name, pass_word, this));
    }

    @Override
    public void LoginByWX() {
        getListener(listener -> listener.onHttpStart(UUID_WX));
        getModel(model -> model.LoginByWX(this));
    }

    @Override
    public void LoginByQQ() {
        getListener(listener -> listener.onHttpStart(UUID_QQ));
        getModel(model -> model.LoginByQQ(this));
    }

    @Override
    public void onDestory() {
        getListener(listener -> listener = null);
        getModel(model -> model = null);
    }

    @Override
    public void onRequestFail(int uuid, String msg) {
        getListener(listener -> {

            listener.onDataError(uuid, msg);
            listener.onNetWorkError(uuid, msg);

            listener.onHttpFinished(uuid);
        });
    }

    @Override
    public void onReturnError(int uuid, LoginBean bean) {
        getListener(listener -> {
            listener.onReturntError(uuid, bean);
            listener.onHttpFinished(uuid);
        });
    }

    @Override
    public void onSuccess(int uuid, LoginBean bean) {
        getListener(listener -> {
            listener.onSuccess(uuid, bean);
            listener.onHttpFinished(uuid);
        });
    }

    private void getListener(GetViewChangedListener listener) {
        if (this.mLoginViewChangedListener != null) {
            listener.set(this.mLoginViewChangedListener);
        }
    }

    private void getModel(GetModelImpl model) {
        if (this.mLoginModelImpl != null) {
            model.set(this.mLoginModelImpl);
        }
    }
}
