package com.yanxiu.gphone.student.base;

import java.io.Serializable;

/**
 * @author frc
 *         created at 17-1-9.
 */

public class UrlBean implements Serializable {



    private String server;     //普通接口
    private String loginServer;//登陆接口
    private String uploadServer;
    private String initializeUrl;
    private String mode;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLoginServer() {
        return loginServer;
    }

    public void setLoginServer(String loginServer) {
        this.loginServer = loginServer;
    }

    public String getUploadServer() {
        return uploadServer;
    }

    public void setUploadServer(String uploadServer) {
        this.uploadServer = uploadServer;
    }

    public String getInitializeUrl() {
        return initializeUrl;
    }

    public void setInitializeUrl(String initializeUrl) {
        this.initializeUrl = initializeUrl;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
