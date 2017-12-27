package com.yanxiu.gphone.student.mistakeredo.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/12/20 15:01.
 * Function :
 */
public class CheckAnswerResponse extends EXueELianBaseResponse{

    public List<Data> data;

    public class Data{
        public List<AnalysisBean> analysis;
        public String objectiveRate;
        public String objectiveScore;
        public int status;
        public List<Integer> subStatus;
    }
}
