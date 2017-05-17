package com.yanxiu.gphone.student.user.presenter.impl;

import com.yanxiu.gphone.student.user.bean.ResetPassWordBean;
import com.yanxiu.gphone.student.user.interf.OnHttpFinishedListener;
import com.yanxiu.gphone.student.user.model.impl.ResetPassWordModelImpl;
import com.yanxiu.gphone.student.user.presenter.interf.ResetPassWordPresenter;
import com.yanxiu.gphone.student.user.view.interf.ResetPassWordViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/16 16:13.
 * Function :
 */
public class ResetPassWordPresenterImpl implements ResetPassWordPresenter, OnHttpFinishedListener<ResetPassWordBean> {

    /**
     * do reset password
     * */
    private static final int UUID_RESETPASSWORD = 0x000;

    private ResetPassWordModelImpl mResetPassWordModelImpl;
    private ResetPassWordViewChangedListener mResetPassWordViewChangedListener;

    /**
     * default passwords are empty
     * */
    private boolean isPassWordReady = false;
    private boolean isPassWordAgainReady = false;

    public ResetPassWordPresenterImpl(ResetPassWordViewChangedListener listener) {
        this.mResetPassWordViewChangedListener = listener;
        mResetPassWordModelImpl = new ResetPassWordModelImpl();
    }

    @Override
    public void onDestory() {
        this.mResetPassWordViewChangedListener = null;
        this.mResetPassWordModelImpl = null;
    }

    public void setPassWordValue(String value) {
        if (value.length() > 0) {
            setPassWordIsReady(true);
        } else {
            setPassWordIsReady(false);
        }
    }

    private void setPassWordIsReady(boolean isReady) {
        this.isPassWordReady = isReady;
        if (mResetPassWordViewChangedListener != null) {
            mResetPassWordViewChangedListener.onButtonFocusChange(isPassWordReady && isPassWordAgainReady);
        }
    }

    public void setPassWordAgainValue(String value) {
        if (value.length() > 0) {
            setPassWordAgainIsReady(true);
        } else {
            setPassWordAgainIsReady(false);
        }
    }

    private void setPassWordAgainIsReady(boolean isReady) {
        this.isPassWordAgainReady = isReady;
        if (mResetPassWordViewChangedListener != null) {
            mResetPassWordViewChangedListener.onButtonFocusChange(isPassWordReady && isPassWordAgainReady);
        }
    }

    @Override
    public void onResetPassWord(String mobile, String verCode, String passWord) {
        mResetPassWordViewChangedListener.onHttpStart(UUID_RESETPASSWORD);
        mResetPassWordModelImpl.onReset(mobile, verCode, passWord, ResetPassWordPresenterImpl.this);
    }

    @Override
    public void onRequestFail(int uuid, String msg) {
        mResetPassWordViewChangedListener.onHttpFinished(UUID_RESETPASSWORD);
    }

    @Override
    public void onReturnError(int uuid, ResetPassWordBean resetPassWordBean) {
        mResetPassWordViewChangedListener.onHttpFinished(UUID_RESETPASSWORD);
    }

    @Override
    public void onSuccess(int uuid, ResetPassWordBean resetPassWordBean) {
        mResetPassWordViewChangedListener.onHttpFinished(UUID_RESETPASSWORD);
    }
}
