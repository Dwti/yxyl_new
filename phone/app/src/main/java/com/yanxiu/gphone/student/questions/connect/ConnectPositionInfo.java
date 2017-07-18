package com.yanxiu.gphone.student.questions.connect;

/**
 * Created by sunpeng on 2017/7/14.
 */

public class ConnectPositionInfo {
    private int leftPosition,rightPosition;
    private boolean isRight = false; //是否正确

    public ConnectPositionInfo() {
    }

    public ConnectPositionInfo(int leftPosition, int rightPosition,boolean isRight) {
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
        this.isRight = isRight;
    }

    public int getLeftPosition() {
        return leftPosition;
    }

    public void setLeftPosition(int leftPosition) {
        this.leftPosition = leftPosition;
    }

    public int getRightPosition() {
        return rightPosition;
    }

    public void setRightPosition(int rightPosition) {
        this.rightPosition = rightPosition;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
