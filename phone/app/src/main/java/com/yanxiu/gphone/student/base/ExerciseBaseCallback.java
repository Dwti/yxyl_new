package com.yanxiu.gphone.student.base;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.base.ExerciseBaseResponse;
import com.yanxiu.gphone.student.user.activity.LoginActivity;

/**
 * Created by sp on 17-5-22.
 */

public abstract class ExerciseBaseCallback<T extends ExerciseBaseResponse> implements HttpCallback<T> {
    @Override
    public void onSuccess(RequestBase request, T ret) {
        if(ret.getStatus().getCode() == 99){
            LoginActivity.LaunchActivity(YanxiuApplication.getContext());
            return;
        }
    }
}
