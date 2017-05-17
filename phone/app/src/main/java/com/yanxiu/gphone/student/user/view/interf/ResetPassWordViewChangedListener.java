package com.yanxiu.gphone.student.user.view.interf;

import com.yanxiu.gphone.student.user.bean.ResetPassWordBean;
import com.yanxiu.gphone.student.user.interf.OnViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/16 16:11.
 * Function :
 */
public interface ResetPassWordViewChangedListener extends OnViewChangedListener<ResetPassWordBean> {
    void onButtonFocusChange(boolean hasFocus);
}
