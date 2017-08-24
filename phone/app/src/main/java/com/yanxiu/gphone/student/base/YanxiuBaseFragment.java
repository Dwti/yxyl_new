package com.yanxiu.gphone.student.base;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by 戴延枫 on 2017/5/9.
 */
public class YanxiuBaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        Log.i(Constants.TAG,this.getClass().getName());
    }
}
