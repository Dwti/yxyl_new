package com.yanxiu.gphone.student.login.view.interf;

import com.yanxiu.gphone.student.login.bean.BaseBean;
import com.yanxiu.gphone.student.login.interf.OnViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/11 11:19.
 * Function :
 */

public interface ForgetPassWordViewChangeListener extends OnViewChangedListener<BaseBean> {
    void setEditMobileIsEmpty(boolean isEmpty);
    void setEditMobileClear(String text);
    void setSendVerCodeViewFocusChange(boolean hasFocus);
    void startTiming(int totalTime);
    void setButtonFocusChange(boolean hasFocus);
}
