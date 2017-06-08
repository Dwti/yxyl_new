package com.yanxiu.gphone.student.db;

import com.yanxiu.gphone.student.base.UrlBean;

/**
 * Created by sunpeng on 2017/5/25.
 */

public class UrlRepository {

    private UrlBean mUrlBean;

    private static UrlRepository INSTANCE = null;

    public static UrlRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UrlRepository();
        }
        return INSTANCE;
    }

    public String getServer(){
        if(mUrlBean != null){
            return mUrlBean.getServer();
        }else {
            return null;
        }
    }

    public String getLoginServer(){
        if(mUrlBean != null){
            return mUrlBean.getLoginServer();
        }else {
            return null;
        }
    }

    public String getUploadServer(){
        if(mUrlBean != null){
            return mUrlBean.getUploadServer();
        }else {
            return null;
        }
    }

    public String getInitializeServer(){
        if(mUrlBean != null){
            return mUrlBean.getInitializeUrl();
        }else {
            return null;
        }
    }

    public String getMode(){
        if (mUrlBean!=null){
            return mUrlBean.getMode();
        }else {
            return null;
        }
    }

    public void setUrlBean(UrlBean urlBean) {
        this.mUrlBean = urlBean;
    }
}
