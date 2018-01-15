package com.yanxiu.gphone.student.questions.operation;

import com.yanxiu.gphone.student.base.BaseBean;
import com.yanxiu.gphone.student.questions.operation.view.PathDrawingInfo;

import java.util.List;

/**
 * Created by sunpeng on 2018/1/8.
 */

public class OperationBean extends BaseBean {
    private String mImageUrl;
    private List<PathDrawingInfo> mDrawingInfos;

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public List<PathDrawingInfo> getDrawingInfos() {
        return mDrawingInfos;
    }

    public void setDrawingInfos(List<PathDrawingInfo> drawingInfos) {
        mDrawingInfos = drawingInfos;
    }
}
