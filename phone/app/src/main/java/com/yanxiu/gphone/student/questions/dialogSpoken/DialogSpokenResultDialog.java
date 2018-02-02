package com.yanxiu.gphone.student.questions.dialogSpoken;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.WavesLayout;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/20 14:45.
 * Function :
 */
public class DialogSpokenResultDialog {

    public interface onButtonClick {
        void onResetClick();

        void onAgainClick();
    }

    private final ImageView mScoreView;
    private onButtonClick mButtonClick;
    private Dialog mDialog;

    private DialogSpokenResultDialog(Context context) {
        mDialog = new Dialog(context, R.style.AlertDialogStyle);
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        View view = LayoutInflater.from(context).inflate(R.layout.dialogspoken_popupwindow, null);
        mScoreView = (ImageView) view.findViewById(R.id.iv_score);
        WavesLayout resetView = (WavesLayout) view.findViewById(R.id.wl_reset_waves);
        resetView.setOnClickListener(mClickListener);
        WavesLayout againView = (WavesLayout) view.findViewById(R.id.wl_again_waves);
        againView.setOnClickListener(mClickListener);
        mDialog.setContentView(view, layoutParams);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
    }

    public static DialogSpokenResultDialog create(Context context) {
        return new DialogSpokenResultDialog(context);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.wl_reset_waves:
                    mDialog.dismiss();
                    if (mButtonClick != null) {
                        mButtonClick.onResetClick();
                    }
                    break;
                case R.id.wl_again_waves:
                    mDialog.dismiss();
                    if (mButtonClick != null) {
                        mButtonClick.onAgainClick();
                    }
                    break;
            }
        }
    };

    public void setDisMissCallback(onButtonClick buttonClick) {
        this.mButtonClick = buttonClick;
    }

    public void setScore(int score) {
        switch (score) {
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

    public void show() {
        mDialog.show();
    }
}
