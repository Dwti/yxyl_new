package com.yanxiu.gphone.student.base;

import android.content.Context;
import android.content.Intent;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.base.ExerciseBaseResponse;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.user.activity.LoginActivity;
import com.yanxiu.gphone.student.util.LoginInfo;


/**
 * Created by sp on 17-5-22.
 */

public abstract class ExerciseBaseCallback<T extends ExerciseBaseResponse> implements HttpCallback<T> {
    @Override
    public void onSuccess(RequestBase request, T ret) {
        //code =99 表示token失效
        if(ret.getStatus().getCode() == Constants.NOT_LOGGED_IN){
            LoginInfo.LogOut();
            Context context = YanxiuApplication.getContext();
            Intent intent=new Intent(context,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else {
            onResponse(request,ret);
        }
    }

    protected abstract void onResponse(RequestBase request, T response);
}
