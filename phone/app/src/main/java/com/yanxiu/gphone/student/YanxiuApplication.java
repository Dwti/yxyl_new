package com.yanxiu.gphone.student;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.yanxiu.gphone.student.base.UrlBean;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.FileUtil;

import org.litepal.LitePalApplication;

public class YanxiuApplication extends LitePalApplication {
    private static YanxiuApplication instance;

    public static YanxiuApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUrlServer();
        Stetho.initializeWithDefaults(this);
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

}
