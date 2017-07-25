package com.yanxiu.gphone.student.util;

import android.hardware.Camera;
import android.os.Environment;

import com.yanxiu.gphone.student.constant.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * 权限相关
 * Created by 戴延枫 on 2017/7/12.
 */

public class PermissionUtil {
    public static final String ROOT_DIRECTORY_NAME = "yanxiu";
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator + Constants.PICTUREDIR + File.separator;
//    public static final String OUT_SDCARD_DIR = "/storage/extSdCard" + File.separator + ROOT_DIRECTORY_NAME + File.separator;
//    public static final String OUT_SDCARD_DIR2 = "/mnt/external_sd" + File.separator + ROOT_DIRECTORY_NAME + File.separator;
//    public static final String OUT_SDCARD_DIR3 = "/mnt/sdcard2" + File.separator + ROOT_DIRECTORY_NAME + File.separator;
//    public static final String OUT_SDCARD_DIR4 = YanxiuApplication.getInstance().getFilesDir() + File.separator + ROOT_DIRECTORY_NAME + File.separator;
    public static final String TESTFILE_NAME = "testPermission.txt";

    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * 检测读取权限
     *
     * @return
     */
    public static boolean checkReadPermission() {
        boolean result;
        File file = new File(SDCARD_DIR + TESTFILE_NAME);
        if(!file.exists()){
            file.mkdirs();
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String result1 = br.readLine();
            result = true;
        } catch (Exception e) {
            // TODO 失败
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 检测写权限
     *
     * @return
     */
    public static boolean checkWritePermission() {
        boolean result;
        File file = new File(SDCARD_DIR + TESTFILE_NAME);
        if(!file.exists()){
            file.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write("hahaha".getBytes());
            fos.close();
            result = true;
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
