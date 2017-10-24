package com.yanxiu.gphone.student.questions.spoken;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/24 10:22.
 * Function :
 */
public class SpokenFirstEnterDialog extends Dialog {
    public SpokenFirstEnterDialog(@NonNull Context context) {
        this(context, R.style.AnswerCarDialog);
    }

    public SpokenFirstEnterDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.AnswerCarDialog);
        init(context);
    }

    protected SpokenFirstEnterDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.spoken_first_enter_dialog,null,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
        setCanceledOnTouchOutside(true);
    }
}
