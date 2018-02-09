package com.yanxiu.gphone.student.questions.operation;

import java.io.Serializable;

/**
 * Created by sunpeng on 2018/1/19.
 */

public class PictureModifiedMessage implements Serializable {
    public int fromId;

    public PictureModifiedMessage(int fromId) {
        this.fromId = fromId;
    }
}
