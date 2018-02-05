package com.yanxiu.gphone.student.http.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

/**
 * Created by Canghaixiao.
 * Time : 2018/2/5 16:31.
 * Function :
 */
public class NoticeReponse extends EXueELianBaseResponse{

    public Property property;

    public class Property{
        public String title;
        public String notice;
    }
}
