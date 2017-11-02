package com.yanxiu.gphone.student.base;

import android.Manifest;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.userevent.UserEventManager;
import com.yanxiu.gphone.student.util.ActivityManger;
import com.yanxiu.gphone.student.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by 戴延枫 on 2017/5/9.
 */
public class YanxiuBaseActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks {
    private final String TAG = "YanxiuBaseActivity";
    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_WRITE_READ_PERM = 124;
    private static final int RC_OTHER_PERM = 125;
    private static OnPermissionCallback mPermissionCallback;
    private boolean isActive = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManger.addActicity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Constants.TAG,this.getClass().getName());
        if (!isActive) {
            isActive = true;
            UserEventManager.getInstense().whenEnterFront();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            isActive = false;
            UserEventManager.getInstense().whenEnterBack();
        }
    }

    @Override
    protected void onDestroy() {

        ActivityManger.destoryActivity(this);
        mPermissionCallback = null;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 请求照相机权限
     */
    public static void requestCameraPermission(OnPermissionCallback onPermissionCallback) {
        mPermissionCallback = onPermissionCallback;
        ArrayList<String> permsList = new ArrayList<>();
        permsList.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { //6.0系统以下
            if (PermissionUtil.cameraIsCanUse()) {
                if (mPermissionCallback!=null) {
                    mPermissionCallback.onPermissionsGranted(permsList);
                }
            } else {
                if (mPermissionCallback!=null) {
                    mPermissionCallback.onPermissionsDenied(permsList);
                }
            }
        } else {
            if (EasyPermissions.hasPermissions(ActivityManger.getTopActivity(), Manifest.permission.CAMERA)) {
                // Have permission, do the thing!
                if (mPermissionCallback!=null) {
                    mPermissionCallback.onPermissionsGranted(permsList);
                }
            } else {
                // Ask for one permission
                EasyPermissions.requestPermissions(ActivityManger.getTopActivity(), ActivityManger.getTopActivity().getString(R.string.rationale_camera), RC_CAMERA_PERM, Manifest.permission.CAMERA);
            }
        }
    }

    /**
     * 请求读写权限
     *
     * @return flag 代表当前是否有权限(适用于不关心用户对权限的操作,只需要知道当前是否有权限的情况)
     * @param onPermissionCallback callback回调代表用户对权限的更改(适用于需要根据用户的选择做出不同处理的情况)
     *
     */
    public static boolean requestWriteAndReadPermission(OnPermissionCallback onPermissionCallback) {
        boolean flag;
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        mPermissionCallback = onPermissionCallback;
        ArrayList<String> permsList = new ArrayList<>();
        for (int i = 0; i < perms.length; i++) {
            permsList.add(perms[i]);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { //6.0系统以下
            flag=PermissionUtil.checkWritePermission() && PermissionUtil.checkReadPermission();
            if (flag) {
                if (mPermissionCallback!=null) {
                    mPermissionCallback.onPermissionsGranted(permsList);
                }
            } else {
                if (mPermissionCallback!=null) {
                    mPermissionCallback.onPermissionsDenied(permsList);
                }
            }
        } else {
            flag=EasyPermissions.hasPermissions(ActivityManger.getTopActivity(), perms);
            if (flag) {
                // Have permission, do the thing!
                if (mPermissionCallback!=null) {
                    mPermissionCallback.onPermissionsGranted(permsList);
                }
            } else {
                // Ask for one permission
                EasyPermissions.requestPermissions(ActivityManger.getTopActivity(), ActivityManger.getTopActivity().getString(R.string.rationale_write_read), RC_WRITE_READ_PERM, perms);
            }
        }
        return flag;
    }

    /**
     * 请求权限
     *
     * @return flag 代表当前是否有权限(适用于不关心用户对权限的操作,只需要知道当前是否有权限的情况)
     * @param onPermissionCallback callback回调代表用户对权限的更改(适用于需要根据用户的选择做出不同处理的情况)
     *
     * @param perms {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS};
     */
    public static boolean requestPermissions(String[] perms, OnPermissionCallback onPermissionCallback) {
        mPermissionCallback = onPermissionCallback;
        boolean flag=EasyPermissions.hasPermissions(ActivityManger.getTopActivity(), perms);
        if (flag) {
            // Have permissions, do the thing!
            ArrayList<String> permsList = new ArrayList<>();
            for (int i = 0; i < perms.length; i++) {
                permsList.add(perms[i]);
            }
            if (mPermissionCallback!=null) {
                mPermissionCallback.onPermissionsGranted(permsList);
            }
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(ActivityManger.getTopActivity(), ActivityManger.getTopActivity().getString(R.string.rationale_other), RC_OTHER_PERM, perms);
        }
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        if (mPermissionCallback!=null) {
            mPermissionCallback.onPermissionsGranted(perms);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
//        }
        if (mPermissionCallback!=null) {
            mPermissionCallback.onPermissionsDenied(perms);
        }
    }

    public boolean isAppOnForeground() {
        try {
            android.app.ActivityManager activityManager = (android.app.ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            String packageName = getApplicationContext().getPackageName();

            List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            if (appProcesses == null)
                return false;

            for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(packageName)
                        && appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
