package com.yanxiu.gphone.student.questions.operation;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sunpeng on 2018/1/8.
 */

public class OperationBean extends BaseBean {
    private String mImageUrl;
    private String mStoredFileName;  //画线保存的文件名字

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getStoredFileName() {
        return mStoredFileName;
    }

    public void setStoredFileName(String storedFileName) {
        mStoredFileName = storedFileName;
    }
}
