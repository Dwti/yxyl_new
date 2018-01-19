package com.yanxiu.gphone.student.questions.operation;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sunpeng on 2018/1/8.
 */

public class OperationBean extends BaseBean {
    private String mImageUrl;
    private String mStoredFilePath;  //画线保存的文件完整路径（此路径缓存的是画的上面一层的bitmap，文件名是不带格式后缀的）

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getStoredFilePath() {
        return mStoredFilePath;
    }

    public void setStoredFilePath(String storedFilePath) {
        mStoredFilePath = storedFilePath;
    }
}
