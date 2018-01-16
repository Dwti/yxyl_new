package com.yanxiu.gphone.student.questions.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.mistakeredo.MistakeRedoActivity;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class MultiChooseWrongFragment extends WrongSimpleExerciseBaseFragment {

    private MultiChoiceQuestion mData;
    private View mAnswerView;
    private TextView mQuestionView;
    private ChooseLayout mChooseView;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        this.mData = (MultiChoiceQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((MultiChoiceQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mAnswerView = inflater.inflate(R.layout.fragment_analysis_choose, container, false);
        return mAnswerView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        initView();
        initData();
    }

    private void initView() {
        mQuestionView = (TextView) mAnswerView.findViewById(R.id.tv_question);
        mChooseView = (ChooseLayout) mAnswerView.findViewById(R.id.cl_answer);
    }

    private void initData() {
        if (!TextUtils.isEmpty(mData.getStem())) {
            Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mQuestionView), null);
            mQuestionView.setText(string);
        }else {
            mQuestionView.setVisibility(View.GONE);
        }
        mChooseView.setIsClick(false);
        mChooseView.setChooseType(ChooseLayout.TYPE_MULTI);
        mChooseView.setData(mData.getChoice());
    }

    private void showAnalysis() {
//        List<String> datas = mData.getAnswerList();
        List<AnalysisBean> analysisBeans=mData.getPad().getAnalysis();
        List<String> answers=mData.getMultianswer();
        int count=mChooseView.getChildCount();
        for (int i = 0; i < answers.size(); i++) {
            int answerPosition=Integer.parseInt(answers.get(i));
            if (count>answerPosition){
                ChooseLayout.ViewHolder answerViewHolder= (ChooseLayout.ViewHolder) mChooseView.getChildAt(answerPosition).getTag();
                answerViewHolder.mQuestionIdView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ffffff));
                answerViewHolder.mQuestionIdView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.choose_right));
                answerViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_89e00d));
            }
        }

        if (analysisBeans!=null) {
            for (int i = 0; i < analysisBeans.size(); i++) {
                AnalysisBean analysisBean = analysisBeans.get(i);
                int selectPosition = Integer.parseInt(analysisBean.key);
                if (count > selectPosition) {
                    if (AnalysisBean.RIGHT.equals(analysisBean.status)) {
                        mChooseView.setSelect(selectPosition);
                    } else {
                        ChooseLayout.ViewHolder selectViewHolder = (ChooseLayout.ViewHolder) mChooseView.getChildAt(selectPosition).getTag();
                        selectViewHolder.mQuestionIdView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ffffff));
                        selectViewHolder.mQuestionIdView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.choose_wrong));
                        selectViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ff7a05));
                        selectViewHolder.mQuestionSelectView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.multi_select_wrong));
                    }
                }
            }
        }
    }

    @Override
    public void initAnalysisView() {
        showAnalysis();
        if (mData.getStatus() == Constants.ANSWER_STATUS_RIGHT) {
            showAnswerResultView(true, mData.getAnswerCompare(), null);
        } else {
            showAnswerResultView(false, mData.getAnswerCompare(), null);
        }
        showDifficultyview(mData.getStarCount());
        showAnalysisview(mData.getQuestionAnalysis());
        showPointView(mData.getPointList());
        showNoteView(mData.getJsonNoteBean());
    }
}