package com.yanxiu.gphone.student.login.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/31 14:17.
 * Function :
 */
public class StageDialog extends Dialog implements View.OnClickListener {

    public interface onStageClickListener {
        void onStageChangeYes();

        void onStageChangeNo();
    }

    private onStageClickListener mStageClickListener;

    public StageDialog(@NonNull Context context) {
        super(context, R.style.AnswerCarDialog);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_stage, null);
        setContentView(view);

        view.findViewById(R.id.button_no).setOnClickListener(this);
        view.findViewById(R.id.button_yes).setOnClickListener(this);
    }

    public void addStageListener(onStageClickListener stageClickListener) {
        this.mStageClickListener = stageClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_no:
                if (mStageClickListener != null) {
                    mStageClickListener.onStageChangeNo();
                }
                break;
            case R.id.button_yes:
                if (mStageClickListener != null) {
                    mStageClickListener.onStageChangeYes();
                }
                break;
        }
    }
}
