package com.yanxiu.gphone.student.user.view.interf;

import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.user.interf.OnViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 13:48.
 * Function :
 */

public interface RegisterViewChangedListener extends OnViewChangedListener<BaseBean> {
    void setEditMobileIsEmpty(boolean isEmpty);

    void setEditMobileClear(String text);

    void setSendVerCodeViewFocusChange(boolean hasFocus);

    void startTiming(int totalTime);

    void setButtonFocusChange(boolean hasFocus);

    void setEditPassWordChange(boolean isCipher);
}
