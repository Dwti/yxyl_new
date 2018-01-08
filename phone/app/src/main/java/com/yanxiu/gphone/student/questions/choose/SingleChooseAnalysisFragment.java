package com.yanxiu.gphone.student.questions.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.questions.cloze.ClozeAnalysisComplexFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class SingleChooseAnalysisFragment extends AnalysisSimpleExerciseBaseFragment {

    private SingleChoiceQuestion mData;
    private View mAnswerView;
    private TextView mQuestionView;
    private ChooseLayout mChooseView;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        this.mData= (SingleChoiceQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SingleChoiceQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mAnswerView=inflater.inflate(R.layout.fragment_analysis_choose,container,false);
        return mAnswerView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        initView();
        initData();
    }

    private void initView(){
        mQuestionView= (TextView) mAnswerView.findViewById(R.id.tv_question);
        mChooseView= (ChooseLayout) mAnswerView.findViewById(R.id.cl_answer);
        hiddenNumberBar(mAnswerView);
    }

    /**
     * 在完形填空里，单选子题需要隐藏部分view
     * @param view
     */
    private void hiddenNumberBar(View view){
        Fragment parentFragment = getParentFragment();
        if(null != parentFragment && parentFragment instanceof ClozeAnalysisComplexFragment){
            View number_bar = view.findViewById(R.id.number_bar);
            View ll_question = view.findViewById(R.id.ll_question);
            View line = view.findViewById(R.id.view);

            number_bar.setVisibility(View.GONE);
            ll_question.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }

    private void initData(){
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),null);
        mQuestionView.setText(string);
        mChooseView.setIsClick(false);
        mChooseView.setData(mData.getChoice());
        List<String> datas=mData.getAnswerList();
        String answer=mData.getSingleAnswer();
        int answer_position=Integer.parseInt(answer);

        int count=mChooseView.getChildCount();

        if (datas.size()>0){
            String select=datas.get(datas.size()-1);
            int select_position=Integer.parseInt(select);
            if (count>select_position) {
                ChooseLayout.ViewHolder selectViewHolder = (ChooseLayout.ViewHolder) mChooseView.getChildAt(select_position).getTag();
                List<AnalysisBean> analysisBeans=mData.getPad().getAnalysis();
                if (analysisBeans!=null&&!analysisBeans.isEmpty()&&AnalysisBean.RIGHT.equals(analysisBeans.get(0).status)) {
                    mChooseView.setSelect(select_position);
                    selectViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_89e00d));
                    selectViewHolder.mQuestionIdView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.choose_right));
                    selectViewHolder.mQuestionIdView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ffffff));
                }else {
                    if (count>answer_position) {
                        ChooseLayout.ViewHolder answerViewHolder = (ChooseLayout.ViewHolder) mChooseView.getChildAt(answer_position).getTag();

                        selectViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ff7a05));
                        selectViewHolder.mQuestionIdView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.choose_wrong));
                        selectViewHolder.mQuestionIdView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ffffff));
                        selectViewHolder.mQuestionSelectView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.single_select_wrong));

                        answerViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_89e00d));
                        answerViewHolder.mQuestionIdView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.choose_right));
                        answerViewHolder.mQuestionIdView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ffffff));
                    }
                }
            }
        }else {
            if (count>answer_position) {
                ChooseLayout.ViewHolder answerViewHolder = (ChooseLayout.ViewHolder) mChooseView.getChildAt(answer_position).getTag();
                answerViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_89e00d));
                answerViewHolder.mQuestionIdView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.choose_right));
                answerViewHolder.mQuestionIdView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ffffff));
            }
        }
    }

    @Override
    public void initAnalysisView() {
        if(Constants.HAS_FINISH_STATUS.equals(mData.getPaperStatus())){ //已完成
            if (mData.getStatus()== Constants.ANSWER_STATUS_RIGHT){
                showAnswerResultView(true, mData.getAnswerCompare(), null);
            }else {
                showAnswerResultView(false, mData.getAnswerCompare(), null);
            }
            showDifficultyview(mData.getStarCount());
            showAnalysisview(mData.getQuestionAnalysis());
            showPointView(mData.getPointList());
        }else{ //逾期未提交的作业 题目解析展示“难度”、“答案”、“题目解析”、“知识点”
            showDifficultyview(mData.getStarCount());
//            showAnswerView(mData.getSingleAnswer());
            showAnalysisview(mData.getQuestionAnalysis());
            showPointView(mData.getPointList());
        }
    }
}