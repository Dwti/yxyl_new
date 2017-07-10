package com.yanxiu.gphone.student.customviews.spantextview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by sunpeng on 2017/6/23.
 */

public class SubjectClozeView extends FrameLayout {

    private TextView mNumber;

    public SubjectClozeView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public SubjectClozeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SubjectClozeView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SubjectClozeView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.cloze_view1,this,true);
        mNumber = (TextView) view.findViewById(R.id.text_number);
    }

    public void setTextNumber(int num){
        mNumber.setText(String.valueOf(num));
    }

    public int getTextNumber(){
        return Integer.parseInt(mNumber.getText().toString());
    }

    public void setNumberVisible(int visible){
        mNumber.setVisibility(visible);
    }
}
