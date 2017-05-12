package com.yanxiu.gphone.student.login.presenter.impl;

import com.yanxiu.gphone.student.login.bean.BaseBean;
import com.yanxiu.gphone.student.login.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.login.model.impl.ForgetPassWordModelImpl;
import com.yanxiu.gphone.student.login.presenter.interf.ForgetPassWordPresenter;
import com.yanxiu.gphone.student.login.view.interf.ForgetPassWordViewChangeListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/11 11:22.
 * Function :
 */

public class ForgetPassWordPresenterImpl implements ForgetPassWordPresenter, OnHttpFinishedListener<BaseBean> {

    private interface GetViewChangeListener {
        void set(ForgetPassWordViewChangeListener listener);
    }

    private interface GetModelImol {
        void set(ForgetPassWordModelImpl model);
    }

    /**
     * send verification code
     */
    private static final int UUID_VERCODE = 0x000;
    /**
     * do next
     */
    private static final int UUID_NEXT = 0x001;

    private ForgetPassWordModelImpl mForgetPassWordModelImpl;
    private ForgetPassWordViewChangeListener mForgetPassWordViewChangeListener;

    private boolean isMobileReady = false;
    private boolean isVerCodeReady = false;

    public ForgetPassWordPresenterImpl(ForgetPassWordViewChangeListener listener) {
        this.mForgetPassWordViewChangeListener = listener;
        mForgetPassWordModelImpl = new ForgetPassWordModelImpl();
    }

    @Override
    public void onDestory() {
        getListener(listener -> listener = null);
        getModel(model -> model = null);
    }

    @Override
    public void sendVerCode(String mobile) {
        getListener(listener -> listener.onHttpStart(UUID_VERCODE));
        getModel(model -> model.sendVerCode(mobile, ForgetPassWordPresenterImpl.this));
        getListener(listener -> listener.startTiming(45000));
    }

    @Override
    public void onNext(String mobile, String verCode) {
        getListener(listener -> listener.onHttpStart(UUID_NEXT));
        getModel(model -> model.onNext(mobile, verCode, ForgetPassWordPresenterImpl.this));
    }

    @Override
    public void setMobileChange() {
        getListener(listener -> listener.setEditMobileClear(""));
    }

    public void setMobileValue(String value) {

        if (value.length() > 0) {
            getListener(listener -> listener.setEditMobileIsEmpty(false));
        } else {
            getListener(listener -> listener.setEditMobileIsEmpty(true));
        }

        if (value.length() == 11 && value.substring(0, 1).equals("1")) {
            setMobileReady(true);
        } else {
            setMobileReady(false);
        }
        getListener(listener -> listener.setSendVerCodeViewFocusChange(isMobileReady));
        getListener(listener -> listener.setButtonFocusChange(isMobileReady && isVerCodeReady));
    }

    private void setMobileReady(boolean isReady) {
        this.isMobileReady = isReady;
    }

    public void setVerCodeValue(String value) {
        int verCodeLenth = value.length();
        if (verCodeLenth != 4) {
            setVerCodeReady(false);
        } else {
            setVerCodeReady(true);
        }
        getListener(listener -> listener.setButtonFocusChange(isMobileReady && isVerCodeReady));
    }

    private void setVerCodeReady(boolean isReady) {
        this.isVerCodeReady = isReady;
    }

    @Override
    public void onRequestFail(int uuid, String msg) {
//        getListener(listener -> listener.on);
        getListener(listener -> listener.onHttpFinished(uuid));
    }

    @Override
    public void onReturnError(int uuid, BaseBean baseBean) {
        getListener(listener -> listener.onReturntError(uuid, baseBean));
        getListener(listener -> listener.onHttpFinished(uuid));
    }

    @Override
    public void onSuccess(int uuid, BaseBean baseBean) {
        getListener(listener -> listener.onSuccess(uuid, baseBean));
        getListener(listener -> listener.onHttpFinished(uuid));
        if (uuid == UUID_VERCODE) {
            getListener(listener -> listener.startTiming(45));
        }
    }

    private void getListener(GetViewChangeListener viewChangeListener) {
        if (mForgetPassWordViewChangeListener != null) {
            viewChangeListener.set(mForgetPassWordViewChangeListener);
        }
    }

    private void getModel(GetModelImol modelImol) {
        if (mForgetPassWordModelImpl != null) {
            modelImol.set(mForgetPassWordModelImpl);
        }
    }
}
