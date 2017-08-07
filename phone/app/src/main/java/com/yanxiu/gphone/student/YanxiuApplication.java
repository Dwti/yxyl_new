package com.yanxiu.gphone.student;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yanxiu.gphone.student.base.UrlBean;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.FileUtil;

import org.litepal.LitePalApplication;

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
        Gson gson = new Gson();
        String urlJson = FileUtil.getDataFromAssets(this, Constants.URL_SERVER_FILE_NAME);
        UrlBean urlBean = gson.fromJson(urlJson,UrlBean.class);
        UrlRepository.getInstance().setUrlBean(urlBean);
    }

    public boolean isForceUpdate () {
        return isForceUpdate;
    }

    public void setIsForceUpdate (boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

}
