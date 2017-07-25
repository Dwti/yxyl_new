package com.yanxiu.gphone.student.login.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/23 11:21.
 * Function :
 */
public class JoinClassResponse extends EXueELianBaseResponse implements Serializable{
    public List<Data> data;

    public class Data implements Serializable{
        public String id;
        public String schoolid;
        public String schoolname;
        public String teachernum;
        public String periodid;
        public String gradeid;
        public String gradename;
        public String stageid;
        public String name;
        public String status;
        public String adminid;
        public String adminName;
        public String stdnum;
    }
}
