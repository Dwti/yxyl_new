package com.yanxiu.gphone.student.util;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.http.request.InitializeRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/7 16:36.
 * Function :
 */
public class UpdataUtil {

    public static void Initialize(){
        String channel=SystemUtil.getChannelName();
        InitializeRequest mInitializeRequest=new InitializeRequest();
        mInitializeRequest.channel=channel;
        mInitializeRequest.startRequest(String.class, new HttpCallback<String>() {
            @Override
            public void onSuccess(RequestBase request, String ret) {

            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

}
