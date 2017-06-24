package com.yanxiu.gphone.student.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.CameraView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/20 10:17.
 * Function :
 */
public class CameraActivity extends YanxiuBaseActivity implements View.OnClickListener, CameraView.onTakePictureListener {

    public static final String RESULTCODE="code";

    private CameraActivity mContext;
    private CameraView mCameraView;
    private ImageView mAlbumView;
    private ImageView mTakePictureView;
    private ImageView mDeleteView;
    private int mFromId;

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
                AlbumActivity.LaunchActivity(mContext);
                break;
            case R.id.iv_takepicture:
                mCameraView.takePicture(CameraActivity.this);
                break;
            case R.id.iv_delete:
                CameraActivity.this.finish();
                break;
        }
    }

    @Override
    public void onComplete(boolean isSuccess, String path) {
        if (isSuccess){
            List<String> list=new ArrayList<>();
            list.add(path);
            CameraCallbackMessage message=new CameraCallbackMessage();
            message.fromId=mFromId;
            message.paths=list;
            EventBus.getDefault().post(message);
            CameraActivity.this.finish();
        }
    }

    public void onEventMainThread(CameraCallbackMessage message){

    }

    public static class CameraCallbackMessage implements Serializable {
        public int fromId;
        public List<String> paths;
    }
}
