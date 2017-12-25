package com.yanxiu.gphone.student.mistakeredo.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/12/20 15:01.
 * Function :
 */
public class CheckAnswerResponse extends EXueELianBaseResponse{

    public List<Data> data;

    public class Data{
        public List<Analysis> analysis;
        public String objectiveRate;
        public String objectiveScore;
        public String status;
        public List<Integer> subStatus;

        public class Analysis{
            public String key;
            /* *
             * 0对 1错 2半对 3未作答
             * */
            public String status;
            public String name;
            public List<Integer> subStatus;
        }
    }
}
