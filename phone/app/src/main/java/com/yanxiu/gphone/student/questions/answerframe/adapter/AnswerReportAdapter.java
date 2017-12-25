package com.yanxiu.gphone.student.questions.answerframe.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.ReportAnswerBean;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 戴延枫
 * 答题报告adapter
 */

public class AnswerReportAdapter extends BaseAdapter {

    private Context mContext;

    private List<BaseQuestion> mList = new ArrayList<>();
    private OnAnswerCardItemSelectListener mListener;

    public AnswerReportAdapter(Context context, List<BaseQuestion> list, OnAnswerCardItemSelectListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseQuestion question = mList.get(position);
        if (question == null)
            return null;
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(R.layout.ansewrreport_item, null);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        generateQuestionNumber(question, holder);

        return row;
    }

    /**
     * 生成题号
     */
    private void generateQuestionNumber(final BaseQuestion question, ViewHolder holder) {
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
        int status = question.getPad().getStatus();
        ReportAnswerBean answerBean = question.getReportAnswerBean();
        if (QuestionTemplate.ANSWER.equals(question.getTemplate())) {                //如果是主观题
            if (status == QuestionUtil.ANSER_READED) {                                   //如果已批改
                if (answerBean.isHalfRight()) {
//                        holder.ivSign.setImageResource(R.drawable.answer_report_half_correct);
                    isHalfRight(question, holder);
                } else if (answerBean.isRight()) {
//                        holder.ivSign.setImageResource(R.drawable.answer_report_correct);
                    isRight(question, holder);
                } else if (!answerBean.isRight()) {
//                        holder.ivSign.setImageResource(R.drawable.answer_report_wrong);
                    isWrong(question, holder);
                }
            } else {
                //如果未批改，
                weipigai(question, holder);
            }

        }else if (QuestionTemplate.FILL.equals(question.getTemplate())){
            if (answerBean.isHalfRight()) {
                isHalfRight(question, holder);
            } else if (answerBean.isRight()) {
                isRight(question, holder);
            } else if (!answerBean.isRight()) {
                isWrong(question, holder);
            }
        }else {          //如果是客观题
            if (answerBean.isFinish()) {
                if (answerBean.isRight()) {
                    isRight(question, holder);
                } else {
                    isWrong(question, holder);
                }
            } else {
                isWrong(question, holder);
            }
        }


        holder.mWavesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemSelect(question);
                }
            }
        });

    }


    private void weipigai(BaseQuestion question, ViewHolder holder) {
        holder.mQuestion_number.setBackgroundResource(R.drawable.selector_answer_report_unpigai_item);
        holder.mWavesLayout.setWaveColor(mContext.getResources().getColor(R.color.color_999999));
//            holder.mWavesLayout.setWaveColor(Color.parseColor("#00ff00"));
        int csl = mContext.getResources().getColor(R.color.color_ffffff);
        holder.mPrefixNumber.setTextColor(csl);
        holder.mPostfixNumber.setTextColor(csl);
        holder.mLine.setBackgroundResource(R.color.color_ffffff);
    }

    private void isRight(BaseQuestion question, ViewHolder holder) {
        holder.mQuestion_number.setBackgroundResource(R.drawable.selector_answer_report_right_item);
        holder.mWavesLayout.setWaveColor(WavesLayout.DEFAULT_COLOR);
        int csl = mContext.getResources().getColor(R.color.color_ffffff);
        holder.mPrefixNumber.setTextColor(csl);
        holder.mPostfixNumber.setTextColor(csl);
        holder.mLine.setBackgroundResource(R.color.color_ffffff);
    }

    private void isHalfRight(BaseQuestion question, ViewHolder holder) {
        holder.mQuestion_number.setBackgroundResource(R.drawable.selector_answer_report_halfright_item);
        holder.mWavesLayout.setWaveColor(mContext.getResources().getColor(R.color.color_999999));
//            holder.mWavesLayout.setWaveColor(Color.parseColor("#00ff00"));
        int csl = mContext.getResources().getColor(R.color.color_ffffff);
        holder.mPrefixNumber.setTextColor(csl);
        holder.mPostfixNumber.setTextColor(csl);
        holder.mLine.setBackgroundResource(R.color.color_ffffff);
    }

    private void isWrong(BaseQuestion question, ViewHolder holder) {
        holder.mQuestion_number.setBackgroundResource(R.drawable.selector_answer_report_wrong_item);
        holder.mWavesLayout.setWaveColor(mContext.getResources().getColor(R.color.color_bfff7a05));
//            holder.mWavesLayout.setWaveColor(Color.parseColor("#00ff00"));
        int csl = mContext.getResources().getColor(R.color.color_ffffff);
        holder.mPrefixNumber.setTextColor(csl);
        holder.mPostfixNumber.setTextColor(csl);
        holder.mLine.setBackgroundResource(R.color.color_ffffff);
    }


    private class ViewHolder {
        private RelativeLayout mQuestion_number;
        private WavesLayout mWavesLayout;
        private TextView mPrefixNumber;
        private TextView mPostfixNumber;
        private View mLine;

        public ViewHolder(View itemView) {
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


