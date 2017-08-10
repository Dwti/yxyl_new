package com.yanxiu.gphone.student.exercise.bean;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sp on 17-7-28.
 */

public class MasterBean extends BaseBean {
    protected String masterNum;
    protected String totalNum;
    protected String avgMasterRate;
    protected String masterLevel;

    public String getMasterNum() {
        return masterNum;
    }

    public void setMasterNum(String masterNum) {
        this.masterNum = masterNum;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getAvgMasterRate() {
        return avgMasterRate;
    }

    public void setAvgMasterRate(String avgMasterRate) {
        this.avgMasterRate = avgMasterRate;
    }

    public String getMasterLevel() {
        return masterLevel;
    }

    public void setMasterLevel(String masterLevel) {
        this.masterLevel = masterLevel;
    }
}
