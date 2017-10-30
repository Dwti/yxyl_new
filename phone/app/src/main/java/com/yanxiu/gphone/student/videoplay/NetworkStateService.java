package com.yanxiu.gphone.student.videoplay;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

/**
 * Created by cailei on 03/07/2017.
 */

public class NetworkStateService extends Service {
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    /**
     * 当前处于的网络
     * 0 ：null
     * 1 ：2G/3G
     * 2 ：wifi
     */
    public static int networkStatus;

    public static final String NETWORKSTATE = "com.test.yanxiu.avideoplay.network.state";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();

                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    if (name.equals("WIFI")) {
                        networkStatus = 2;
                    } else {
                        networkStatus = 1;
                    }
                } else {
                    networkStatus = 0;
                    /**
                     * 这里推荐使用本地广播的方式发送:
                     * LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                     */
                }
                Intent it = new Intent();
                it.putExtra("networkStatus", networkStatus);
                it.setAction(NETWORKSTATE);
                sendBroadcast(it); //发送无网络广播给注册了当前服务广播的Activity
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //注册网络状态的广播，绑定到mReceiver
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销接收
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取网络连接管理器
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // 获取当前网络状态信息
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }
}
