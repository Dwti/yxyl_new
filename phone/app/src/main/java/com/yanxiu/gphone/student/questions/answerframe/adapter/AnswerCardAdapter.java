package com.yanxiu.gphone.student.questions.answerframe.adapter;

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

public class AnswerCardAdapter extends RecyclerView.Adapter<AnswerCardAdapter.AnswerCardViewHolder> {

    private Context mContext;

    private ArrayList<BaseQuestion> mList;

    private OnAnswerCardItemSelectListener mListener;

    public AnswerCardAdapter(Context context, OnAnswerCardItemSelectListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<BaseQuestion> list) {
        mList = list;
    }

    @Override
    public AnswerCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ansewrcard_item, parent, false);
        return new AnswerCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerCardViewHolder holder, int position) {
        final BaseQuestion question = mList.get(position);
        generateQuestionNumber(question, holder);
        final AnswerCardViewHolder holder2 = holder;
        holder.mWavesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemSelect(question);
                }
            }
        });
    }

    /**
     * 生成题号
     */
    private void generateQuestionNumber(BaseQuestion question, AnswerCardViewHolder holder) {
        String postfixNumber = String.valueOf(question.getAnswerCardPostfixNumber());
        String prefixNumber = String.valueOf(question.getAnswerCardPrefixNumber());
        if ("-1".equals(postfixNumber)) { //单题
            holder.mPrefixNumber.setText(question.getAnswerCardSimpleNumber());
            holder.mPostfixNumber.setVisibility(View.GONE);
            holder.mLine.setVisibility(View.GONE);
        } else {
            holder.mPrefixNumber.setText(prefixNumber);
            holder.mPostfixNumber.setText("(" + postfixNumber + ")");
            holder.mPostfixNumber.setVisibility(View.VISIBLE);
            holder.mLine.setVisibility(View.VISIBLE);
        }

        if (question.getIsAnswer()) {
            holder.mQuestion_number.setBackgroundResource(R.drawable.selector_answer_card_item);
            holder.mWavesLayout.setWaveColor(WavesLayout.DEFAULT_COLOR);
            ColorStateList csl = mContext.getResources().getColorStateList(R.color.selector_answercard_item_text_color_ansewred);
            holder.mPrefixNumber.setTextColor(csl);
            holder.mPostfixNumber.setTextColor(csl);
            holder.mLine.setBackgroundResource(R.drawable.selector_answercard_item_line_color_answered);
        } else {
            holder.mQuestion_number.setBackgroundResource(R.drawable.selector_unanswer_card_item);
            holder.mWavesLayout.setWaveColor(mContext.getResources().getColor(R.color.color_999999));
//            holder.mWavesLayout.setWaveColor(Color.parseColor("#00ff00"));
            ColorStateList csl = mContext.getResources().getColorStateList(R.color.selector_answercard_item_text_color_unansewred);
            holder.mPrefixNumber.setTextColor(csl);
            holder.mPostfixNumber.setTextColor(csl);
            holder.mLine.setBackgroundResource(R.drawable.selector_answercard_item_line_color_unanswer);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
}


