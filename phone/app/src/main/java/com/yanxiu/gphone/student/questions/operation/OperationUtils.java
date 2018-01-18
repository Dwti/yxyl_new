package com.yanxiu.gphone.student.questions.operation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.StringUtil;

import java.io.File;

/**
 * Created by sunpeng on 2018/1/18.
 */

public class OperationUtils {

    public static boolean hasStoredBitmap(String fileName){
        if(TextUtils.isEmpty(fileName)){
            return false;
        }
        String filePath = FileUtil.getSavePicturePath(fileName);
        File file = new File(filePath);
        if(file.exists()){
            return true;
        }else {
            return false;
        }
    }

    public static Bitmap getStoredBitmap(String fileName){
        if(TextUtils.isEmpty(fileName)){
            return null;
        }
        String filePath = FileUtil.getSavePicturePath(fileName);
        return FileUtil.readBitmapFromFile(filePath);
    }

    public static Bitmap drawableToBitmap(Drawable drawable){
        Bitmap bitmap;
        Rect rect = drawable.getBounds();
        int width = rect.width();
        int height = rect.height();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(width,height,config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,width,height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static void scaleDrawable(Drawable drawable,int toWidth,int toHeight){
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        int scaleWidth,scaleHeight;
        float scale;
        if(width >= height){
            scale = toWidth / width;
            scaleWidth = toWidth;
            scaleHeight = (int) (height * scale);
            if(scaleHeight > toHeight){
                scale = toHeight / scaleHeight;
                scaleHeight = toHeight;
                scaleWidth = (int) (scaleWidth * scale);
            }
        }else {
            scale = toHeight / height;
            scaleWidth = (int) (width * scale);
            scaleHeight = toHeight;
            if(scaleWidth > toWidth){
                scale = toWidth / scaleWidth;
                scaleWidth = toWidth;
                scaleHeight = (int) (scaleHeight * scale);
            }
        }
        drawable.setBounds(0,0,scaleWidth,scaleHeight);
    }

}
