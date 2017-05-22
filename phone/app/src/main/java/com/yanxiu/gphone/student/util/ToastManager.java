package com.yanxiu.gphone.student.util;

import android.widget.Toast;

import com.yanxiu.gphone.student.YanxiuApplication;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 16:01.
 * Function :
 */
public class ToastManager {
    private static Toast toast;

    public static void showMsg(CharSequence msg){
        if (toast==null){
            toast=Toast.makeText(YanxiuApplication.getContext(),msg,Toast.LENGTH_SHORT);
        }else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showMsg(int resId){
        if (toast==null){
            toast=Toast.makeText(YanxiuApplication.getContext(),YanxiuApplication.getContext().getResources().getString(resId),Toast.LENGTH_SHORT);
        }else {
            toast.setText(YanxiuApplication.getContext().getResources().getString(resId));
        }
        toast.show();
    }
}
