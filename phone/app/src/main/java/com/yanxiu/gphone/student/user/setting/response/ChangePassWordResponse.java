package com.yanxiu.gphone.student.user.setting.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/27 14:52.
 * Function :
 */
public class ChangePassWordResponse extends EXueELianBaseResponse {

    public List<ChangePassWordData> data;

    public class ChangePassWordData{
        public String deviceId;
        public int id;
        public String mobile;
        public String password;
        public String token;
        public String type;
        public int uid;
    }
}
