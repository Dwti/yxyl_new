package com.yanxiu.gphone.student.questions.connect;

import android.graphics.Point;

/**
 * Created by sunpeng on 2017/7/14.
 */

public class LinePoints {
    private Point leftPoint, rightPoint;
    private int lineColor;

    public LinePoints() {
    }

    public LinePoints(Point leftPoint, Point rightPoint,int lineColor) {
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
        this.lineColor = lineColor;
    }

    public Point getLeftPoint() {
        return leftPoint;
    }

    public void setLeftPoint(Point leftPoint) {
        this.leftPoint = leftPoint;
    }

    public Point getRightPoint() {
        return rightPoint;
    }

    public void setRightPoint(Point rightPoint) {
        this.rightPoint = rightPoint;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}
