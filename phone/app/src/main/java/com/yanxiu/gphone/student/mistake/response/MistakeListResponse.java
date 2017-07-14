package com.yanxiu.gphone.student.mistake.response;

import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 9:26.
 * Function :
 */
public class MistakeListResponse extends EXueELianBaseResponse {

    public List<Data> data;

    public class Data{
        public int id;
        public String name;
        public DData data;

        public class DData{
            public int editionId;
            public int pointTag;
            public int wrongNum;
            public int chapterTag;
            public int has_knp;
        }
    }
}
