package com.yanxiu.gphone.student.user.response;

import com.yanxiu.gphone.student.base.ExerciseBaseResponse;
import com.yanxiu.gphone.student.base.StatusBean;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/25 11:12.
 * Function :
 */
public class ChooseSchoolResponse extends ExerciseBaseResponse {
    public List<School> data;

    public class School{
        public String type;
        public String areaId;
        public String cityId;
        public String id;
        public String name;
        public String provinceId;
    }
}
