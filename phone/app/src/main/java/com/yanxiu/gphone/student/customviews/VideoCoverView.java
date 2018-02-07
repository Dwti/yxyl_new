package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by lufengqing on 2018/2/7.
 */

public class VideoCoverView extends ImageView {

    public VideoCoverView(Context context) {
       super(context);
    }

    public VideoCoverView(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] rids = {12.0f, 12.0f, 12.0f, 12.0f, 0.0f, 0.0f, 0.0f, 0.0f,};

   protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
       int h = this.getHeight();
       /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
      canvas.clipPath(path);
      super.onDraw(canvas);
   }
}



