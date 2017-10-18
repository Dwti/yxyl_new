package com.yanxiu.gphone.student.bcresource.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by sp on 17-10-18.
 */

public class TopicTreeRequest extends EXueELianBaseRequest {
    protected String type="0";
    protected String id="9600";

    @Override
    protected String urlPath() {
        return "/topic/getTopicTree.do?";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
