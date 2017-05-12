package com.yanxiu.gphone.student.user.view.interf;

import com.yanxiu.gphone.student.user.bean.LoginBean;
import com.yanxiu.gphone.student.user.interf.OnViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 17:42.
 * Function :
 */

public interface LoginViewChangedListener extends OnViewChangedListener<LoginBean>{
    void setEditUserNameClear(String text);
    void setEditUserNameIsEmpty(boolean isEmpty);
    void setEditPassWordChange(boolean isCipher);
    void setButtonFocusChange(boolean hasFocus);
}
