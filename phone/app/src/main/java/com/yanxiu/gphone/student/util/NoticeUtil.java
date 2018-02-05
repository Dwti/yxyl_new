package com.yanxiu.gphone.student.util;

import android.content.Context;
import android.text.TextUtils;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.http.request.NoticeRequest;
import com.yanxiu.gphone.student.http.response.NoticeReponse;

/**
 * Created by Canghaixiao.
 * Time : 2018/2/5 16:14.
 * Function :
 */
public class NoticeUtil {

    private NoticeUtil(Context context) {
        request(context);
    }

    public static NoticeUtil Initialize(Context context) {
        return new NoticeUtil(context);
    }

    private void request(final Context context) {
        if (Constants.NOTICE != 0) {
            return;
        }
        Constants.NOTICE = 1;
        NoticeRequest request = new NoticeRequest();
        request.startRequest(NoticeReponse.class, new EXueELianBaseCallback<NoticeReponse>() {
            @Override
            protected void onResponse(RequestBase request, NoticeReponse response) {
                if (response.getStatus().getCode() == 0 && !TextUtils.isEmpty(response.property.notice)) {
                    NoticeDialog dialog = new NoticeDialog(context, response.property);
                    dialog.show();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

}
