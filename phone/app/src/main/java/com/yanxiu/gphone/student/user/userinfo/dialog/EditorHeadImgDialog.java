package com.yanxiu.gphone.student.user.userinfo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.anim.JumpAnimManager;
import com.yanxiu.gphone.student.util.anim.TranslationYAnimUtil;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/3 16:44.
 * Function :
 */
public class EditorHeadImgDialog extends Dialog implements View.OnClickListener {

    private View mAnimView;
    private Context mContext;
    private int mTranslationY=800;
    private int mAnimTime=200;
    private View mAnimBgview;

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
        mContext=context;
        View rootView= LayoutInflater.from(context).inflate(R.layout.dialog_editorheadimg,null);
        setContentView(rootView);
        rootView.setOnClickListener(this);
        rootView.findViewById(R.id.ll_album).setOnClickListener(this);
        rootView.findViewById(R.id.ll_camera).setOnClickListener(this);
        rootView.findViewById(R.id.tv_cancle).setOnClickListener(this);

        mAnimBgview=rootView.findViewById(R.id.gradation);
        mAnimView=rootView.findViewById(R.id.rl_headimg);
    }

    @Override
    public void show() {
        super.show();
        TranslationYAnimUtil.getInstence().setAnimViewHeight(mContext,R.dimen.userinfo_dialog_h).setBgGradation(mAnimBgview,0f,0.7f).setStartAnim(mAnimView);
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
