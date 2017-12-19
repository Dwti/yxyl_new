package com.yanxiu.gphone.student;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.umeng.socialize.PlatformConfig;
import com.yanxiu.gphone.student.base.EnvConfigBean;
import com.yanxiu.gphone.student.base.UrlBean;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.db.SpManager;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.userevent.UserEventManager;
import com.yanxiu.gphone.student.userevent.bean.UserInstallBean;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.SoundManger;

import org.json.JSONException;
import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

public class YanxiuApplication extends LitePalApplication {
    private static YanxiuApplication instance;

    private boolean isForceUpdate = false;//是否强制升级

    public static YanxiuApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
            super.onCreate();
        instance = this;
        initUm();
        initUrlServer();
        Stetho.initializeWithDefaults(this);
        SoundManger.getInstence().initialize(this);
        if (SpManager.isFristStartUp()) {
            UserEventManager.getInstense().whenFirstStart();
        }else {
            UserEventManager.getInstense().whenStartApp();
        }
        try {
            UserEventManager.getInstense().whenGetUserInstalled(getAllAppsInfo(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initUm() {
        PlatformConfig.setWeixin("wxb6704ac52abcfe4c","943d690bd5020ae629c20281e53bc334");
        PlatformConfig.setQQZone("1104826608","PsLMILpDwU6QDNOk");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initUrlServer(){
        UrlBean urlBean;
        Gson gson = new Gson();
        String urlJson = FileUtil.getDataFromAssets(this, Constants.URL_SERVER_FILE_NAME);
        if(urlJson.contains(Constants.MULTICONFIG)){
            EnvConfigBean envConfigBean = gson.fromJson(urlJson,EnvConfigBean.class);
            urlBean = envConfigBean.getData().get(envConfigBean.getCurrentIndex());
        }else {
            urlBean = gson.fromJson(urlJson,UrlBean.class);
        }
        UrlRepository.getInstance().setUrlBean(urlBean);
    }

    public boolean isForceUpdate () {
        return isForceUpdate;
    }

    public void setIsForceUpdate (boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }


    /**
     * 方法描述:获取所有已安装App信息
     */
    public static List<UserInstallBean> getAllAppsInfo(Context context) {
        List<UserInstallBean> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            if (pi != null) {
                UserInstallBean message = new UserInstallBean();
                ApplicationInfo ai = pi.applicationInfo;
                message.name = ai.loadLabel(pm).toString();
                message.type = "";
                list.add(message);
            }
        }
        return list;
    }
}
