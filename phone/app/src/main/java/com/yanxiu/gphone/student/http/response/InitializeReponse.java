package com.yanxiu.gphone.student.http.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/7 17:19.
 * Function :
 */
public class InitializeReponse extends EXueELianBaseResponse{

    public List<Data> data;

    public class Data{
        public int id;
        public String version;
        public String title;
        public String resid;
        public String ostype;
        public String upgradetype;
        public String upgradeswitch;
        public String targetenv;
        public String uploadtime;
        public String modifytime;
        public String content;
        public String fileURL;
        public String fileLocalPath;
        public String productName;
        public String productLine;
        public String ifCheck;
    }
}
