package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/25 15:47.
 * Function :
 */
public class ChooseLayout extends LinearLayout implements View.OnClickListener {

    private static final String[] mEms=new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
    private Context mContext;
    private onSelectItemListener mOnSelectItemListener;

    public interface onSelectItemListener{
        void onSelect(int position);
    }

    public ChooseLayout(Context context) {
        super(context);
        init(context);
    }

    public ChooseLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChooseLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext=context;
    }

    public void setData(List<String> list){
        if (isInEditMode()) {
            return;
        }

        addChildView(list);
    }

    private void addChildView(List<String> list){
        this.removeAllViews();
        for (int i=0;i<list.size();i++){
            View view= LayoutInflater.from(mContext).inflate(R.layout.layout_choose_item,this,false);
            ViewHolder holder=new ViewHolder();
            holder.position=i;
            holder.mQuestionIdView= (TextView) findViewById(R.id.tv_question_id);
            holder.mQuestionIdView.setText(getEmsByNum(i));
            holder.mQuestionContentView= (TextView) findViewById(R.id.tv_question_content);
            holder.mQuestionContentView.setText(list.get(i));
            holder.mQuestionSelectView=view.findViewById(R.id.v_question_select);
            holder.mQuestionSelectView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_choose_round_unselect));
            view.setOnClickListener(ChooseLayout.this);
            view.setTag(holder);
            this.addView(view);
        }
    }

    private class ViewHolder{
        int position;
        TextView mQuestionIdView;
        TextView mQuestionContentView;
        View mQuestionSelectView;
    }

    private String getEmsByNum(int num){
        return mEms[num];
    }

    public void setSelectItemListener(onSelectItemListener mOnSelectItemListener){
        this.mOnSelectItemListener=mOnSelectItemListener;
    }

    public void setSelect(int position){
        int count=this.getChildCount();
        for (int i=0;i<count;i++){
            View chileView=this.getChildAt(i);
            ViewHolder holder= (ViewHolder) chileView.getTag();
            if (i==position){
                holder.mQuestionSelectView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_choose_round_select));
            }else {
                holder.mQuestionSelectView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_choose_round_unselect));
            }
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder= (ViewHolder) v.getTag();
        if (mOnSelectItemListener!=null){
            mOnSelectItemListener.onSelect(holder.position);
        }
        setSelect(holder.position);
    }
}
