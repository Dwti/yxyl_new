package com.yanxiu.gphone.student.bcresource.bean;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class BCBean extends BaseBean {

    public static final int TYPE_PARENT = 0;
    public static final int TYPE_CHILD = 1;
    public static final int TYPE_FOOTER = -1;

    protected String name;
    protected String id;
    protected String resource_num = "0";
    protected String question_num = "0";

    protected int type = TYPE_CHILD;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResource_num() {
        return resource_num;
    }

    public void setResource_num(String resource_num) {
        this.resource_num = resource_num;
    }

    public String getQuestion_num() {
        return question_num;
    }

    public void setQuestion_num(String question_num) {
        this.question_num = question_num;
    }
}
