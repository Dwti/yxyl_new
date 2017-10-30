package com.yanxiu.gphone.student.bcresource.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-10-18.
 */

public class BCWrapperBean extends BaseBean {
    protected List<BCBean> children = new ArrayList<>();
    protected String name;
    protected String id;
    protected String resource_num = "0";
    protected String question_num = "0";

    public List<BCBean> getChildren() {
        return children;
    }

    public void setChildren(List<BCBean> children) {
        this.children = children;
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
