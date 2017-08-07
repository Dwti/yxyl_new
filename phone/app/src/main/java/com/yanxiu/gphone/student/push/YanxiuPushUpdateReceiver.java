package com.yanxiu.gphone.student.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.homepage.MainActivity;
import com.yanxiu.gphone.student.util.LoginInfo;

import static com.yanxiu.gphone.student.constant.Constants.MAINAVTIVITY_PUSHMSGBEAN;

/**
 * 推送，点击通知栏后，接收通知栏信息的广播
 */
public class YanxiuPushUpdateReceiver extends BroadcastReceiver {
    private final String TAG = "pushTag";
    public static final String PUSH_RECEIVER_INTENT_ACTION = "com.yanxiu.gphone.student.yanxiu_push_update_receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//        isAppOnForeground(MainActivity.getInstance() != null ?
//                MainActivity.getInstance() :
//                context);
        if (!LoginInfo.isLogIn() || YanxiuApplication.getInstance().isForceUpdate()) {
            Log.e(TAG, "-----------isForceUpdate------LoginBeanIsNull------UserInfoIsNull-----");
            return;
        }
        PushMsgBean mPushMsgBean = (PushMsgBean) intent.getSerializableExtra(MAINAVTIVITY_PUSHMSGBEAN);
        if (mPushMsgBean != null) {
            if (MainActivity.getInstance() != null && mPushMsgBean.getMsg_type() != Constants.NOTIFICATION_ACTION_JOIN_THE_CLASS) {
                MainActivity.getInstance().judgeToJump(intent);
            } else {
                MainActivity.invoke(context, mPushMsgBean);
            }
        }
    }
}
