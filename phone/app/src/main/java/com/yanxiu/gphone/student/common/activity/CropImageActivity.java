package com.yanxiu.gphone.student.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.CropImageView;

import java.io.Serializable;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.student.common.activity.CameraActivity.RESULTCODE;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/23 15:53.
 * Function :
 */
public class CropImageActivity extends YanxiuBaseActivity implements View.OnClickListener, CropImageView.onCropFinishedListener {

    private static final String IMGPATH="img";

    private Context mContext;
    private CropImageView mCropView;
    private ImageView mBackView;
    private TextView mResetView;
    private ImageView mConfirmView;

    private int mFromId;
    private String mImgPath;

    public static void LaunchActivity(Context context,String imgPath,int fromId){
        Intent intent=new Intent(context,CropImageActivity.class);
        intent.putExtra(IMGPATH,imgPath);
        intent.putExtra(RESULTCODE,fromId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        mContext=CropImageActivity.this;
        mImgPath=getIntent().getStringExtra(IMGPATH);
        mFromId=getIntent().getIntExtra(RESULTCODE,-1);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mCropView= (CropImageView) findViewById(R.id.civ_crop);
        mBackView= (ImageView) findViewById(R.id.iv_back);
        mResetView= (TextView) findViewById(R.id.tv_reset);
        mConfirmView= (ImageView) findViewById(R.id.iv_ok);
    }

    private void listener() {
        mBackView.setOnClickListener(CropImageActivity.this);
        mResetView.setOnClickListener(CropImageActivity.this);
        mConfirmView.setOnClickListener(CropImageActivity.this);
    }

    private void initData() {
        Glide.with(mContext).load(mImgPath).asBitmap().into(mCropView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                CropImageActivity.this.finish();
                break;
            case R.id.tv_reset:
                mCropView.setReset();
                break;
            case R.id.iv_ok:
                mCropView.startCrop(CropImageActivity.this);
                break;
        }
    }

    @Override
    public void onFinished(String path) {
        if (!TextUtils.isEmpty(path)) {
            CropCallbackMessage message=new CropCallbackMessage();
            message.fromId=mFromId;
            message.path=path;
            EventBus.getDefault().post(message);
            CropImageActivity.this.finish();
        }
    }

    public static class CropCallbackMessage implements Serializable {
        public int fromId;
        public String path;
    }

}
