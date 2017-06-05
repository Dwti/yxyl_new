package com.yanxiu.gphone.student.homework.request;

import com.yanxiu.gphone.student.base.ExerciseBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by sp on 17-5-18.
 */

public class SubjectRequest extends ExerciseBaseRequest {
    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected String urlPath() {
        return "/class/listGroups.do";
    }
}
