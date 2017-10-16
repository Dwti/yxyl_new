package com.yanxiu.gphone.student.exercise.bean;

/**
 * Created by sp on 17-7-26.
 */

public class EditionChildBean {
    private String id;
    private String name;
    private String selected = "0";  // 0，未选中；  1：选中

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

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
