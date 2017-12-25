package com.yanxiu.gphone.student.questions.fillblank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisFillBlankTextView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.util.StemUtil;

import java.util.List;

/**
 * Created by sunpeng on 2017/6/21.
 */

public class FillBlankAnalysisFragment extends AnalysisSimpleExerciseBaseFragment{

    private FillBlankQuestion mQuestion;

    private AnalysisFillBlankTextView mFillBlank;
    private String mStem;

    private View mRootView;

    private List<String> mFilledAnswers,mCorrectAnswers;


    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mQuestion = (FillBlankQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setData((FillBlankQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    private void initData() {
        mStem = mQuestion.getStem();
        mFilledAnswers = mQuestion.getStringAnswers();
        mCorrectAnswers = mQuestion.getCorrectAnswers();
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_analysis_fill_blank,container,false);
        return mRootView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mFillBlank = (AnalysisFillBlankTextView) mRootView.findViewById(R.id.tv_fill_blank);
        mFillBlank.setBlankEditable(false);
        initData();
        initListener();
        setStem(mStem);
    }

    @Override
    public void initAnalysisView() {
        if(Constants.HAS_FINISH_STATUS.equals(mQuestion.getPaperStatus())){ //已完成
            int status=mQuestion.getStatus();
            if (status==Constants.ANSWER_STATUS_RIGHT){
                showAnswerResultView(true,mQuestion.getAnswerCompare(),null);
            }else if (status==Constants.ANSWER_STATUS_HALFRIGHT){
                showAnswerResultView(true,mQuestion.getAnswerCompare(),null,true);
            }else {
                showAnswerResultView(false,mQuestion.getAnswerCompare(),null);
            }
            showDifficultyview(mQuestion.getStarCount());
            showAnalysisview(mQuestion.getQuestionAnalysis());
            showPointView(mQuestion.getPointList());
        }else{ //逾期未提交的作业 题目解析展示“难度”、“答案”、“题目解析”、“知识点”
            showDifficultyview(mQuestion.getStarCount());
            String answer="";
            if (mQuestion.getServer_answer()!=null){
                for (int i = 0; i < mQuestion.getCorrectAnswers().size(); i++) {
                    if(i < mQuestion.getCorrectAnswers().size() -1){// 不是最后一个
                        answer += mQuestion.getCorrectAnswers().get(i) + ",";
                    }else{
                        answer += mQuestion.getCorrectAnswers().get(i);
                    }
                }
            }
            showAnswerView(answer);
            showAnalysisview(mQuestion.getQuestionAnalysis());
            showPointView(mQuestion.getPointList());
        }
    }

    private void setStem(String text){
        String stem = StemUtil.initAnalysisFillBlankStem(text, mQuestion.getPad().getAnalysis());
        mFillBlank.setText(stem);
    }

    private void initListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
