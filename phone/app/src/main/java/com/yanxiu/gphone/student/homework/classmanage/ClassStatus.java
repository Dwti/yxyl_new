package com.yanxiu.gphone.student.homework.classmanage;

/**
 * Created by sp on 17-5-20.
 */

public enum ClassStatus {
    HAS_CLASS(0),
    NO_CLASS(71),
    APPLYING_CLASS(72);

    private int code;
    private ClassStatus(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
