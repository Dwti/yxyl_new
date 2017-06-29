package com.yanxiu.gphone.student.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.yanxiu.gphone.student.constant.Constants.CHARACTER_SLASH;
import static com.yanxiu.gphone.student.constant.Constants.DOMYBOXDIR;
import static com.yanxiu.gphone.student.constant.Constants.PICTUREDIR;

/**
 * Created by sunpeng on 2017/5/25.
 */

public class FileUtil {

    public static String getDataFromAssets(Context context,String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getSavePicturePath(String name){
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+PICTUREDIR;
        File file1 = new File(dir);
        if (!file1.exists())
            file1.mkdirs();
        return dir+name;
    }

    public static String getExternalStorageAbsolutePath(String urlStr) {
        int position = urlStr.lastIndexOf(CHARACTER_SLASH);
        if (position > 0) {
            urlStr = urlStr.substring(position + 1);
        }
        String dir = getLoadExternalDir();
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        return dir + urlStr;
    }

    private static String getLoadExternalDir() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        StringBuffer mBuffer = new StringBuffer();
        mBuffer.append(path);
        mBuffer.append(CHARACTER_SLASH);
        mBuffer.append(DOMYBOXDIR);
        mBuffer.append(CHARACTER_SLASH);
        return mBuffer.toString();
    }
}
