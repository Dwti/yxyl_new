package com.yanxiu.gphone.student.questions.operation.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by sunpeng on 2018/1/8.
 */

public class PathDrawingInfo {
    Paint mPaint;
    Path mPath;

    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }
}
