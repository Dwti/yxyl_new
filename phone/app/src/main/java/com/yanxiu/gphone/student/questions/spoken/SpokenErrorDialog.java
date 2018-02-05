package com.yanxiu.gphone.student.questions.spoken;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/24 9:59.
 * Function :
 */
public class SpokenErrorDialog extends Dialog implements View.OnClickListener {
    private TextView state_title;
    public SpokenErrorDialog(@NonNull Context context) {
        this(context,R.style.AnswerCarDialog);
    }

    public SpokenErrorDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.AnswerCarDialog);
        init(context);
    }

    protected SpokenErrorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.spoken_dialog,null,false);
        view.findViewById(R.id.button_no).setOnClickListener(this);
        state_title = (TextView) view.findViewById(R.id.state_title);
        setContentView(view);
    }

    public void setTitle(CharSequence text){
        state_title.setText(text);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
