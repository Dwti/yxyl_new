package com.yanxiu.gphone.student.questions.spoken;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/20 14:45.
 * Function :
 */
public class SpokenResultDialog {

    public interface onDisMissCallback{
        void disMissCallback();
    }

    private final ImageView mScoreView;
    private onDisMissCallback mDisMissCallback;
    private Dialog mDialog;

    private SpokenResultDialog(Context context){
        mDialog=new Dialog(context,R.style.AlertDialogStyle);
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
        View view= LayoutInflater.from(context).inflate(R.layout.spoken_popupwindow,null);
        mScoreView= (ImageView) view.findViewById(R.id.iv_score);
        mDialog.setContentView(view,layoutParams);
        mDialog.setOnDismissListener(mDismissListener);
        mDialog.setCanceledOnTouchOutside(false);
    }

    public static SpokenResultDialog create(Context context){
        return new SpokenResultDialog(context);
    }

    private DialogInterface.OnDismissListener mDismissListener=new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if (mDisMissCallback!=null){
                mDisMissCallback.disMissCallback();
            }
        }
    };

    public void setDisMissCallback(onDisMissCallback disMissCallback){
        this.mDisMissCallback=disMissCallback;
    }

    public void setScore(int score){
        switch (score){
            case 0:
                mScoreView.setImageResource(R.drawable.spoken_result_score0);
                break;
            case 1:
                mScoreView.setImageResource(R.drawable.spoken_result_score1);
                break;
            case 2:
                mScoreView.setImageResource(R.drawable.spoken_result_score2);
                break;
            case 3:
                mScoreView.setImageResource(R.drawable.spoken_result_score3);
                break;
        }
    }

    public void show(){
        mDialog.show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mDialog.dismiss();
        }
    };
}
