package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/25 15:47.
 * Function :
 */
public class ChooseLayout extends LinearLayout implements View.OnClickListener {

    public static final int TYPE_SINGLE = 0x000;
    public static final int TYPE_MULTI = 0x001;
    private static final String[] mEms = new String[]{"A.", "B.", "C.", "D.", "E.", "F.", "G.", "H.", "I.", "J.", "K.", "L.", "M.", "N."};

    private Context mContext;
    private onItemClickListener mOnItemClickListener;
    private int mChooseType = TYPE_SINGLE;

    public interface onItemClickListener {
        void onClick(int position, boolean isSelected);
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

    private void init(Context context) {
        this.mContext = context;
        if (isInEditMode()) {
            return;
        }
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public void setData(List<String> list) {
        addChildView(list);
    }

    private void addChildView(List<String> list) {
        this.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_choose_item, this, false);
            ViewHolder holder = new ViewHolder();
            holder.position = i;
            holder.mQuestionIdView = (TextView) view.findViewById(R.id.tv_question_id);
            holder.mQuestionIdView.setText(getEmsByNum(i));
            holder.mQuestionContentView = (TextView) view.findViewById(R.id.tv_question_content);
            Spanned string= Html.fromHtml(list.get(i),new HtmlImageGetter(holder.mQuestionContentView),null);
            holder.mQuestionContentView.setText(string);
            holder.mQuestionSelectView = view.findViewById(R.id.v_question_select);
            ViewCompat.setBackground(holder.mQuestionSelectView, ContextCompat.getDrawable(mContext, R.drawable.shape_choose_round_unselect));
            view.setOnClickListener(ChooseLayout.this);
            view.setTag(holder);
            this.addView(view);
        }
    }

    private class ViewHolder {
        int position;
        boolean mSelect = false;
        TextView mQuestionIdView;
        TextView mQuestionContentView;
        View mQuestionSelectView;
    }

    private String getEmsByNum(int num) {
        return mEms[num];
    }

    public void setIsClick(boolean isClick) {
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = this.getChildAt(i);
            if (isClick) {
                view.setEnabled(true);
            } else {
                view.setEnabled(false);
            }
        }
    }

    public void setChooseType(int type) {
        this.mChooseType = type;
    }

    public void setSelectItemListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setSelect(int position){
        setSelect(position,false);
    }

    private void setSelect(int position,boolean isCallBack) {
        int count = this.getChildCount();
        if (position>=count){
            return;
        }
        for (int i = 0; i < count; i++) {
            View chileView = this.getChildAt(i);
            ViewHolder holder = (ViewHolder) chileView.getTag();
            if (i == position) {
                if (holder.mSelect) {
                    setItemUnSelect(holder);
                    if (isCallBack) {
                        onClick(i, false);
                    }
                } else {
                    setItemSelect(holder);
                    if (isCallBack) {
                        onClick(i, true);
                    }
                }
            } else {
                if (mChooseType == TYPE_SINGLE) {
                    setItemUnSelect(holder);
                }
            }
        }
    }

    private void setItemUnSelect(ViewHolder holder) {
        holder.mSelect = false;
        ViewCompat.setBackground(holder.mQuestionSelectView, ContextCompat.getDrawable(mContext, R.drawable.shape_choose_round_unselect));
    }

    private void setItemSelect(ViewHolder holder) {
        holder.mSelect = true;
        ViewCompat.setBackground(holder.mQuestionSelectView, ContextCompat.getDrawable(mContext, R.drawable.shape_choose_round_select));
    }

    private void onClick(int position, boolean isSelected) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onClick(position, isSelected);
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        setSelect(holder.position,true);
    }
}
