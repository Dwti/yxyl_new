package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/12/21 14:38.
 * Function :
 */
public class AnalysisBean extends BaseBean{

    public static final String RIGHT="0";

    public String key;
    /* *
     * 0对 1错 2半对 3未作答
     * */
    public String status;
    public String name;
    public List<String> subStatus;

}
