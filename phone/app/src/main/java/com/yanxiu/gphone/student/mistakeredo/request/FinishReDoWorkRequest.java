package com.yanxiu.gphone.student.mistakeredo.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-11-23.
 */

public class FinishReDoWorkRequest extends RequestBase {
    protected String token = LoginInfo.getToken();
    protected String deleteWqidList ;  //错题重做之后，需要删除的错题的wqid;

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected String urlPath() {
        return "/q/finishReDoWork.do";
    }

    public String getDeleteWqidList() {
        return deleteWqidList;
    }

    public void setDeleteWqidList(String deleteWqidList) {
        this.deleteWqidList = deleteWqidList;
    }
}
