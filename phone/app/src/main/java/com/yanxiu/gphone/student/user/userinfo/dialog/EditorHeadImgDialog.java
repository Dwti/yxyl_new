package com.yanxiu.gphone.student.user.userinfo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/3 16:44.
 * Function :
 */
public class EditorHeadImgDialog extends Dialog implements View.OnClickListener {

    public interface OnViewClickListener{
        void onAlbumClick();
        void onCameraClick();
    }

    private OnViewClickListener mOnViewClickListener;

    public EditorHeadImgDialog(@NonNull Context context) {
        super(context,R.style.AnswerCarDialog);
        setOwnerActivity((Activity) context);
        init(context);
    }

    private void init(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_editorheadimg,null);
        setContentView(view);
        view.setOnClickListener(this);
        view.findViewById(R.id.ll_album).setOnClickListener(this);
        view.findViewById(R.id.ll_camera).setOnClickListener(this);
        view.findViewById(R.id.tv_cancle).setOnClickListener(this);
    }

    public void setClickListener(OnViewClickListener onViewClickListener){
        this.mOnViewClickListener=onViewClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_album:
                if (mOnViewClickListener!=null){
                    mOnViewClickListener.onAlbumClick();
                }
                this.dismiss();
                break;
            case R.id.ll_camera:
                if (mOnViewClickListener!=null){
                    mOnViewClickListener.onCameraClick();
                }
                this.dismiss();
                break;
            case R.id.rl_conver:
            case R.id.tv_cancle:
                this.dismiss();
                break;
        }
    }
}
