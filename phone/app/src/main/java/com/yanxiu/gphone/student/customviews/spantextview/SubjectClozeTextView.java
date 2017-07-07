package com.yanxiu.gphone.student.customviews.spantextview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by sunpeng on 2017/6/23.
 */

public class SubjectClozeTextView extends ReplacementSpanTextView<SubjectClozeView> implements OnReplaceCompleteListener {
    public SubjectClozeTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public SubjectClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubjectClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SubjectClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        addOnReplaceCompleteListener(this);
    }

    @Override
    public void onReplaceComplete() {
        List<SubjectClozeView> views = getReplaceViews();
        if(views == null || views.size() ==0){
            return;
        }
        if(views.size() == 1){
            views.get(0).setNumberVisible(View.INVISIBLE);
            return;
        }
        for(int i =0; i< views.size(); i++){
            views.get(i).setTextNumber(i+1);
        }
    }

    @Override
    protected SubjectClozeView getView() {
        return new SubjectClozeView(getContext());
    }
}
