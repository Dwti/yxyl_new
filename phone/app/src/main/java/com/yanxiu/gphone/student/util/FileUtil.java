package com.yanxiu.gphone.student.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    public static synchronized boolean saveObject(Object object,String path){

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(oos != null){
                try {
                    oos.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
    }

    public static synchronized <T> T readObject(String path){
        ObjectInputStream ojs = null;
        T t = null;
        try {
            ojs = new ObjectInputStream(new FileInputStream(path));
            t = (T) ojs.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(ojs != null){
                try {
                    ojs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return t;
        }
    }

    public static synchronized boolean saveBitmapToFile(Bitmap bitmap,String path){
        boolean success;
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if(file.exists()){
                file.delete();
                file.createNewFile();
            }
            fos = new FileOutputStream(path);
            success = bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    success = false;
                }
            }
        }
        return  success;
    }

    public static synchronized Bitmap readBitmapFromFile(String path){
       return BitmapFactory.decodeFile(path);
    }
}
