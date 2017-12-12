package com.yanxiu.gphone.student.mistakeredo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

import java.util.ArrayList;


/**
 * Created by 戴延枫
 * 答题卡adapter
 */

public class RedoAnswerCardAdapter extends RecyclerView.Adapter<RedoAnswerCardAdapter.AnswerCardViewHolder> {

    private Context mContext;

    private ArrayList<BaseQuestion> mList;

    private OnItemClickListener mListener;

    private int mTotalCount;

    public RedoAnswerCardAdapter(Context context, OnItemClickListener listener,int totalCount) {
        mContext = context;
        mListener = listener;
        mTotalCount = totalCount;
    }

    public void setData(ArrayList<BaseQuestion> list) {
        mList = list;
    }

    @Override
    public AnswerCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.redo_ansewrcard_item, parent, false);
        return new AnswerCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerCardViewHolder holder, final int position) {
        final BaseQuestion question = position < mList.size()? mList.get(position) : null;
        generateQuestionNumber(question, holder,position);
        holder.mWavesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(question,position);
                }
            }
        });
    }

    /**
     * 生成题号
     */
    private void generateQuestionNumber(BaseQuestion question, AnswerCardViewHolder holder,int position) {
        holder.mLine.setVisibility(View.GONE);
        holder.mPostfixNumber.setVisibility(View.GONE);
        holder.mPrefixNumber.setText(String.valueOf(position + 1));

        if (question != null && question.getHasAnswered()) {
            holder.mQuestion_number.setBackgroundResource(R.drawable.selector_answer_card_item);
            holder.mWavesLayout.setWaveColor(WavesLayout.DEFAULT_COLOR);
            ColorStateList csl = mContext.getResources().getColorStateList(R.color.selector_answercard_item_text_color_ansewred);
            holder.mPrefixNumber.setTextColor(csl);
            holder.mPostfixNumber.setTextColor(csl);
            holder.mLine.setBackgroundResource(R.drawable.selector_answercard_item_line_color_answered);
        } else {
            holder.mQuestion_number.setBackgroundResource(R.drawable.selector_unanswer_card_item);
            holder.mWavesLayout.setWaveColor(mContext.getResources().getColor(R.color.color_999999));
            ColorStateList csl = mContext.getResources().getColorStateList(R.color.selector_answercard_item_text_color_unansewred);
            holder.mPrefixNumber.setTextColor(csl);
            holder.mPostfixNumber.setTextColor(csl);
            holder.mLine.setBackgroundResource(R.drawable.selector_answercard_item_line_color_unanswer);
        }
    }

    @Override
    public int getItemCount() {
        return mTotalCount;
    }


    class AnswerCardViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mQuestion_number;
        private WavesLayout mWavesLayout;
        private TextView mPrefixNumber;
        private TextView mPostfixNumber;
        private View mLine;

        public AnswerCardViewHolder(View itemView) {
            super(itemView);
            mQuestion_number = (RelativeLayout) itemView.findViewById(R.id.question_number);
            mWavesLayout = (WavesLayout) itemView.findViewById(R.id.wavesLayout);
            mPrefixNumber = (TextView) itemView.findViewById(R.id.prefixNumber);
            mPostfixNumber = (TextView) itemView.findViewById(R.id.postfixNumber);
            mLine = itemView.findViewById(R.id.line);
            mWavesLayout.setOtherView(mQuestion_number);
            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY, mPrefixNumber, mPostfixNumber);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(BaseQuestion question,int position);
    }
}


