package com.yanxiu.gphone.student.questions.answerframe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.AnswerCardFragment;

import java.util.ArrayList;


/**
 * Created by 戴延枫
 * 答题卡adapter
 */

public class AnswerCardAdapter extends RecyclerView.Adapter<AnswerCardAdapter.AnswerCardViewHolder> {

    private Context mContext;

    private ArrayList<BaseQuestion> mList;

    private OnAnswerCardItemSelectListener mListener;

    public AnswerCardAdapter(Context context,OnAnswerCardItemSelectListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<BaseQuestion> list) {
        mList = list;
    }

    @Override
    public AnswerCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new AnswerCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerCardViewHolder holder, int position) {
        final BaseQuestion question = mList.get(position);
        holder.mTitleTextView.setText(question.numberStringForShow());

        holder.mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemSelect(question);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class AnswerCardViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public AnswerCardViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }
    }
}


