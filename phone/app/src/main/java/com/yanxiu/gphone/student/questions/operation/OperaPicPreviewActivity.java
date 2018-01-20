package com.yanxiu.gphone.student.questions.operation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;

/**
 * Created by sunpeng on 2018/1/19.
 */

public class OperaPicPreviewActivity extends YanxiuBaseActivity {

    private String mImgUrl;
    private ImageView mImageView;

    public static void invoke(Context context,String imgUrl){
        Intent intent = new Intent(context,OperaPicPreviewActivity.class);
        intent.putExtra(PaletteActivity.IMAGE_URL,imgUrl);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_pic_preview);
        mImageView = (ImageView) findViewById(R.id.image);
        mImgUrl = getIntent().getStringExtra(PaletteActivity.IMAGE_URL);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(this).load(mImgUrl).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.image_load_failed).into(mImageView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.activity_photo_exit);
    }
}
