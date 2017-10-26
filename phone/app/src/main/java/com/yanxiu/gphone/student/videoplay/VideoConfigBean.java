package com.yanxiu.gphone.student.videoplay;

import org.litepal.crud.DataSupport;

/**
 * Created by sp on 17-10-26.
 */

public class VideoConfigBean extends DataSupport {
    private boolean hasShowVideoTips = false;
    private String paperId;

    public boolean isHasShowVideoTips() {
        return hasShowVideoTips;
    }

    public void setHasShowVideoTips(boolean hasShowVideoTips) {
        this.hasShowVideoTips = hasShowVideoTips;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }
}
