package com.yanxiu.gphone.student.exercise.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class KnowledgePointBean extends Node<KnowledgePointBean> {
    protected String id;
    protected String name;
    protected MasterBean data;

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

    public MasterBean getData() {
        return data;
    }

    public void setData(MasterBean data) {
        this.data = data;
    }

}
