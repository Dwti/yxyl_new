package com.yanxiu.gphone.student.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.common.Bean.CropCallbackMessage;
import com.yanxiu.gphone.student.customviews.CameraView;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/4 9:45.
 * Function :
 */
public class UserHeadCameraActivity extends YanxiuBaseActivity implements View.OnClickListener, CameraView.onTakePictureListener, OnPermissionCallback {

    public static final String RESULTCODE="code";

    private UserHeadCameraActivity mContext;
    private int mFromId;
    private CameraView mCameraView;
    private ImageView mFlipView;
    private ImageView mTakePictureView;
    private ImageView mDeleteView;

    private boolean isTakePicture=false;

    public static void LaunchActivity(Context context, int fromId){
        Intent intent=new Intent(context,UserHeadCameraActivity.class);
        intent.putExtra(RESULTCODE,fromId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_camera);
        mContext=UserHeadCameraActivity.this;
        EventBus.getDefault().register(mContext);
        mFromId=getIntent().getIntExtra(RESULTCODE,-1);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    private void initView() {
        mCameraView= (CameraView) findViewById(R.id.cv_camera);
        mFlipView= (ImageView) findViewById(R.id.iv_album);
        mTakePictureView= (ImageView) findViewById(R.id.iv_takepicture);
        mDeleteView= (ImageView) findViewById(R.id.iv_delete);
    }

    private void listener() {
        mFlipView.setOnClickListener(UserHeadCameraActivity.this);
        mTakePictureView.setOnClickListener(UserHeadCameraActivity.this);
        mDeleteView.setOnClickListener(UserHeadCameraActivity.this);
    }

    private void initData() {
        mFlipView.setImageBitmap(null);
        mFlipView.setBackgroundResource(R.drawable.selector_camera_rotate);
        mDeleteView.setBackgroundResource(R.drawable.selector_close);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCameraView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_album:
                mCameraView.changeDirection();
                break;
            case R.id.iv_takepicture:
                requestWriteAndReadPermission(UserHeadCameraActivity.this);
                break;
            case R.id.iv_delete:
                UserHeadCameraActivity.this.finish();
                break;
        }
    }

    @Override
    public void onComplete(boolean isSuccess, String path) {
        if (isSuccess){
            UserHeadCropImageActivity.LaunchActivity(mContext,path,mFromId);
        }else {
            ToastManager.showMsg(R.string.no_storage_permissions);
        }
        isTakePicture=false;
    }

    public void onEventMainThread(CropCallbackMessage message){
        if (mFromId==message.fromId){
            UserHeadCameraActivity.this.finish();
        }
    }

    @Override
    public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
        if (!isTakePicture) {
            isTakePicture=true;
            mCameraView.takePicture(UserHeadCameraActivity.this);
        }
    }

    @Override
    public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
        ToastManager.showMsg(R.string.no_storage_permissions);
    }
}
