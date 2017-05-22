package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.base.ExerciseBaseResponse;

import java.util.List;

/**
 * Created by sp on 17-5-19.
 */

public class SearchClassResponse extends ExerciseBaseResponse {
    private List<ClassBean> data;

    public List<ClassBean> getData() {
        return data;
    }

    public void setData(List<ClassBean> data) {
        this.data = data;
    }
}
