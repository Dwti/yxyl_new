package com.yanxiu.gphone.student.questions.connect;

/**
 * Created by sunpeng on 2017/7/13.
 */

public class ConnectedBean {
    private ConnectItemBean mLeftItem;
    private ConnectItemBean mRightItem;

    public ConnectedBean() {
    }

    public ConnectedBean(ConnectItemBean leftItem, ConnectItemBean rightItem) {
        this.mLeftItem = leftItem;
        this.mRightItem = rightItem;
    }

    public ConnectItemBean getLeftItem() {
        return mLeftItem;
    }

    public void setLeftItem(ConnectItemBean leftItem) {
        this.mLeftItem = leftItem;
    }

    public ConnectItemBean getRightItem() {
        return mRightItem;
    }

    public void setRightItem(ConnectItemBean rightItem) {
        this.mRightItem = rightItem;
    }
}
