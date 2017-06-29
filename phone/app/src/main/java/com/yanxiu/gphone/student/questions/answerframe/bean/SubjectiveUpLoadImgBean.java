package com.yanxiu.gphone.student.questions.answerframe.bean;


import com.yanxiu.gphone.student.base.BaseBean;

import java.util.ArrayList;

/**
 * 主观题上传图片
 * dyf
 */
public class SubjectiveUpLoadImgBean extends BaseBean {

    private ArrayList<Integer> LevelPositions;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Integer> getLevelPositions() {
        return LevelPositions;
    }

    public void setLevelPositions(ArrayList<Integer> levelPositions) {
        LevelPositions = levelPositions;
    }

}
