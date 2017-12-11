package com.yanxiu.gphone.student.mistakeredo.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-11-23.
 */

public class FinishReDoWorkRequest extends EXueELianBaseRequest {
    protected String qids ;  //错题重做之后，需要删除的错题的qid;

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlPath() {
        return "/q/finishReDoWork.do";
    }

    public String getDeleteWqidList() {
        return qids;
    }

    public void setDeleteWqidList(String qids) {
        this.qids = qids;
    }
}
