package com.yanxiu.gphone.student.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.common.Bean.CropCallbackMessage;
import com.yanxiu.gphone.student.customviews.CropImageView;
import com.yanxiu.gphone.student.customviews.UserHeadCropImageView;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.student.common.activity.UserHeadCameraActivity.RESULTCODE;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/4 12:01.
 * Function :
 */
public class UserHeadCropImageActivity extends YanxiuBaseActivity implements View.OnClickListener, CropImageView.onCropFinishedListener {

    private static final String IMGPATH="img";

    private Context mContext;

    private UserHeadCropImageView mCropImageView;

    private int mFromId;
    private String mImgPath;
    private boolean isCrop=false;

    public static void LaunchActivity(Context context,String imgPath,int fromId){
        Intent intent=new Intent(context,UserHeadCropImageActivity.class);
        intent.putExtra(IMGPATH,imgPath);
        intent.putExtra(RESULTCODE,fromId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userheadcropimage);
        mContext=UserHeadCropImageActivity.this;
        mImgPath=getIntent().getStringExtra(IMGPATH);
        mFromId=getIntent().getIntExtra(CameraActivity.RESULTCODE,-1);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mCropImageView= (UserHeadCropImageView) findViewById(R.id.civ_crop);
    }

    private void listener() {

    }

    private void initData() {
        Glide.with(mContext).load(mImgPath).asBitmap().into(mCropImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.iv_ok:
                if (!isCrop) {
                    isCrop = true;
                    mCropImageView.startCrop(UserHeadCropImageActivity.this);
                }
                break;
        }
    }

    @Override
    public void onFinished(String path) {
        if (!TextUtils.isEmpty(path)){
            CropCallbackMessage message=new CropCallbackMessage();
            message.fromId=mFromId;
            message.path=path;
            EventBus.getDefault().post(message);
            UserHeadCropImageActivity.this.finish();
        }
        isCrop=false;
    }
}
