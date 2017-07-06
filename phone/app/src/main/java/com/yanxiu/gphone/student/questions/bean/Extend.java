package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/6 10:13.
 * Function :
 */
public class Extend extends BaseBean {

    private Data data;
    private int id;
    private int ptid;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPtid() {
        return ptid;
    }

    public void setPtid(int ptid) {
        this.ptid = ptid;
    }

    public class Data{
        private String answerCompare;
        private String globalStatis;

        public String getAnswerCompare() {
            return answerCompare;
        }

        public void setAnswerCompare(String answerCompare) {
            this.answerCompare = answerCompare;
        }

        public String getGlobalStatis() {
            return globalStatis;
        }

        public void setGlobalStatis(String globalStatis) {
            this.globalStatis = globalStatis;
        }
    }
}
