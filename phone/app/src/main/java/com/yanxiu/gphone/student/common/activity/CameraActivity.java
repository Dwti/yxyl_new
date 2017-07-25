package com.yanxiu.gphone.student.common.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.CameraView;
import com.yanxiu.gphone.student.util.AlbumUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/20 10:17.
 * Function :
 */
public class CameraActivity extends YanxiuBaseActivity implements View.OnClickListener, CameraView.onTakePictureListener, AlbumUtils.onFindFinishedListener, OnPermissionCallback {

    public static final String RESULTCODE="code";

    private CameraActivity mContext;
    private CameraView mCameraView;
    private ImageView mAlbumView;
    private ImageView mTakePictureView;
    private ImageView mDeleteView;
    private int mFromId;
    private boolean isTakePicture=false;

    public static void LaunchActivity(Context context, int fromId){
        Intent intent=new Intent(context,CameraActivity.class);
        intent.putExtra(RESULTCODE,fromId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mContext=CameraActivity.this;
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
        mAlbumView= (ImageView) findViewById(R.id.iv_album);
        mTakePictureView= (ImageView) findViewById(R.id.iv_takepicture);
        mDeleteView= (ImageView) findViewById(R.id.iv_delete);
    }

    private void listener() {
        mAlbumView.setOnClickListener(CameraActivity.this);
        mTakePictureView.setOnClickListener(CameraActivity.this);
        mDeleteView.setOnClickListener(CameraActivity.this);
    }

    private void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.onResume();
        AlbumUtils.getInstence().findFirstPicture(CameraActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_album:
                YanxiuBaseActivity.requestWriteAndReadPermission(CameraActivity.this);
                break;
            case R.id.iv_takepicture:
                if (!isTakePicture) {
                    isTakePicture=true;
                    mCameraView.takePicture(CameraActivity.this);
                }
                break;
            case R.id.iv_delete:
                CameraActivity.this.finish();
                break;
        }
    }

    @Override
    public void onComplete(boolean isSuccess, String path) {
        if (isSuccess){
            CropImageActivity.LaunchActivity(mContext,path,mFromId);
        }else {
            ToastManager.showMsg(R.string.no_storage_permissions);
        }
        isTakePicture=false;
    }

    public void onEventMainThread(CropImageActivity.CropCallbackMessage message){
        if (mFromId==message.fromId){
            CameraActivity.this.finish();
        }
    }

    @Override
    public void onFinished(List<AlbumUtils.PictureMessage> list) {
        if (list!=null&&list.size()>0) {
            Glide.with(mContext).load(list.get(0).path).asBitmap().into(new CircleImageTarget(mAlbumView));
        }
    }

    /**
     * 获取权限
     *
     * @param deniedPermissions
     */
    @Override
    public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
        AlbumActivity.LaunchActivity(mContext,mFromId);
    }

    @Override
    public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
        ToastManager.showMsg(R.string.no_storage_permissions);
    }

    private class CircleImageTarget extends BitmapImageViewTarget {

        CircleImageTarget(ImageView view) {
            super(view);
        }

        @Override
        protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            view.setImageDrawable(circularBitmapDrawable);
        }
    }
}
