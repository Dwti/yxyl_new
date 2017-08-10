package com.yanxiu.gphone.student.questions.answerframe.ui.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.questions.answerframe.ui.bean.NoteBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/10 14:42.
 * Function :
 */
public class NotesRequest extends EXueELianBaseRequest {
    public String wqid;
    public NoteBean note;

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlPath() {
        return "/q/editUserWrongQNote.do";
    }
}
