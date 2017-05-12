package com.yanxiu.gphone.student.user.presenter.impl;

import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.user.model.impl.RegisterModelImpl;
import com.yanxiu.gphone.student.user.presenter.interf.RegisterPresenter;
import com.yanxiu.gphone.student.user.view.interf.RegisterViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 13:52.
 * Function :
 */

public class RegisterPresenterImpl implements RegisterPresenter, OnHttpFinishedListener<BaseBean> {

    private interface GetModelListaner {
        void set(RegisterModelImpl model);
    }

    private interface GetViewChangeListener {
        void set(RegisterViewChangedListener listener);
    }

    /**
     * send verification code
     */
    private static final int UUID_VERCODE = 0x000;
    /**
     * do register
     */
    private static final int UUID_REGISTER = 0x001;

    private RegisterModelImpl mRegisterModelImpl;
    private RegisterViewChangedListener mRegisterViewChangedListener;

    /**
     * the default they are empty
     */
    private boolean isMobileReady = false;
    private boolean isVerCodeReady = false;
    private boolean isPassWordReady = false;

    /**
     * the default password is cipher
     */
    private boolean isCipher = true;

    public RegisterPresenterImpl(RegisterViewChangedListener listener) {
        this.mRegisterViewChangedListener = listener;
        mRegisterModelImpl = new RegisterModelImpl();
    }

    @Override
    public void onDestory() {
        getModel(model -> model = null);
        getListener(listener -> listener = null);
    }

    @Override
    public void sendVerCode(String mobile) {
        getListener(listener -> listener.onHttpStart(UUID_VERCODE));
        getModel(model -> model.getVerificationCode(mobile, RegisterPresenterImpl.this));
        getListener(listener -> listener.startTiming(45000));
    }

    @Override
    public void onRegister(String mobile, String verCode, String passWrod) {
        getListener(listener -> listener.onHttpStart(UUID_REGISTER));
        getModel(model -> model.onRegister(mobile, verCode, passWrod, RegisterPresenterImpl.this));
    }

    @Override
    public void setMobileChange() {
        getListener(listener -> listener.setEditMobileClear(""));
    }

    @Override
    public void setPassWordChange() {
        this.isCipher = !isCipher;
        getListener(listener -> listener.setEditPassWordChange(isCipher));
    }

    public void setMobileValue(String value) {
        int length = value.length();
//        if (length > 0) {
//            if (length == 11 && value.substring(0, 1).equals("1")) {
//                setMobileIsReady(false, true);
//            } else {
//                setMobileIsReady(false, false);
//            }
//        } else {
//            setMobileIsReady(true, false);
//        }
        if (length>0){
            setMobileIsReady(false,true);
        }else {
            setMobileIsReady(true,false);
        }
    }

    private void setMobileIsReady(boolean isEmpty, boolean isReady) {
        this.isMobileReady = isReady;
        getListener(listener -> listener.setEditMobileIsEmpty(isEmpty));
        getListener(listener -> listener.setButtonFocusChange(isMobileReady && isVerCodeReady && isPassWordReady));
        getListener(listener -> listener.setSendVerCodeViewFocusChange(isMobileReady));
    }

    public void setVerCodeValue(String value) {
        int length = value.length();
//        if (length == 4) {
//            setVerCodeIsReady(true);
//        } else {
//            setVerCodeIsReady(false);
//        }
        if (length>0){
            setVerCodeIsReady(true);
        }else {
            setVerCodeIsReady(false);
        }
    }

    private void setVerCodeIsReady(boolean isReady) {
        this.isVerCodeReady = isReady;
        getListener(listener -> listener.setButtonFocusChange(isMobileReady && isVerCodeReady && isPassWordReady));
    }

    public void setPassWordValue(String value) {
        int length = value.length();
//        if (length < 6 || length > 18) {
//            setPassWordIsReady(false);
//        } else {
//            setPassWordIsReady(true);
//        }
        if (length>0){
            setPassWordIsReady(true);
        }else {
            setPassWordIsReady(false);
        }
    }

    private void setPassWordIsReady(boolean isReady) {
        this.isPassWordReady = isReady;
        getListener(listener -> listener.setButtonFocusChange(isMobileReady && isVerCodeReady && isPassWordReady));
    }

    @Override
    public void onRequestFail(int uuid, String msg) {
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
    }

    private void getModel(GetModelListaner listaner) {
        if (mRegisterModelImpl != null) {
            listaner.set(mRegisterModelImpl);
        }
    }

    private void getListener(GetViewChangeListener listener) {
        if (mRegisterViewChangedListener != null) {
            listener.set(mRegisterViewChangedListener);
        }
    }
}
