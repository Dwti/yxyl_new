package com.yanxiu.gphone.student.user.presenter.impl;

import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.user.model.impl.ForgetPassWordModelImpl;
import com.yanxiu.gphone.student.user.presenter.interf.ForgetPassWordPresenter;
import com.yanxiu.gphone.student.user.view.interf.ForgetPassWordViewChangeListener;

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
//        if (value.length() > 0) {
//            if (value.length() == 11 && value.substring(0, 1).equals("1")) {
//                setMobileReady(false,true);
//            } else {
//                setMobileReady(false,false);
//            }
//        } else {
//            setMobileReady(true,false);
//        }
        if (value.length()>0){
            setMobileReady(false,true);
        }else {
            setMobileReady(true,false);
        }
    }

    private void setMobileReady(boolean isEmpty,boolean isReady) {
        this.isMobileReady = isReady;
        getListener(listener -> listener.setSendVerCodeViewFocusChange(isMobileReady));
        getListener(listener -> listener.setButtonFocusChange(isMobileReady && isVerCodeReady));
        getListener(listener -> listener.setEditMobileIsEmpty(isEmpty));
    }

    public void setVerCodeValue(String value) {
        int verCodeLenth = value.length();
//        if (verCodeLenth != 4) {
//            setVerCodeReady(false);
//        } else {
//            setVerCodeReady(true);
//        }
        if (verCodeLenth>0){
            setVerCodeReady(true);
        }else {
            setVerCodeReady(false);
        }
    }

    private void setVerCodeReady(boolean isReady) {
        this.isVerCodeReady = isReady;
        getListener(listener -> listener.setButtonFocusChange(isMobileReady && isVerCodeReady));
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
