package com.yanxiu.gphone.student.exercise.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.List;

/**
 * Created by sp on 17-7-31.
 */

public class Node<T extends Node> extends BaseBean {
    protected boolean isExpanded = false;
    protected int level = 0;
    protected List<T> children;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public boolean hasChildren(){
        return children != null && children.size() >0;
    }
}
