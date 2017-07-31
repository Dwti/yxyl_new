package com.yanxiu.gphone.student.exercise.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-26.
 */

public class EditionBeanEx extends BaseBean {
    private String id;
    private String name;
    private EditionDataBean data;
    private List<EditionChildBean> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public EditionDataBean getData() {
        return data;
    }

    public void setData(EditionDataBean data) {
        this.data = data;
    }

    public List<EditionChildBean> getChildren() {
        return children;
    }

    public void setChildren(List<EditionChildBean> children) {
        this.children = children;
    }
}
