package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/4 16:51.
 * Function :
 */
public class PointLayoutView extends RelativeLayout{

    private String mValue="";
    private TextView mPointView;

    public PointLayoutView(Context context) {
        super(context);
        init(context);
    }

    public PointLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PointLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.layoutview_point,this);
        mPointView= (TextView) findViewById(R.id.tv_point);
    }

    public void setData(String[] texts){
        String value="";
        for (String text:texts){
            value+=text+"  ";
        }
        this.mValue=value;
        mPointView.setText(mValue);
    }

    public void setData(String text){
        this.mValue+=text+"  ";
        mPointView.setText(mValue);
    }
}
