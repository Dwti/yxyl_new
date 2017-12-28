package com.yanxiu.gphone.student.questions.connect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/7/17.
 */

public class ConnectAnalysisFragment extends AnalysisSimpleExerciseBaseFragment {
    private ConnectQuestion mQuestion;
    private View mRootView;
    private TextView mStem;
    private List<ConnectAnalysisItemBean> mChoicesLeft, mChoicesRight;
    private List<String> mFilledAnswers;
//    private List<String> mCorrectAnswers;
    private List<ConnectPositionInfo> mConnectPositionInfos = new ArrayList<>();


    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mQuestion = (ConnectQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setData((ConnectQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    private void initData(){
        mChoicesLeft = mQuestion.getLeftChoices();
        mChoicesRight = mQuestion.getRightChoices();

        mFilledAnswers = mQuestion.getFilledAnswers();
//        mCorrectAnswers = mQuestion.getCorrectAnswer();
        List<AnalysisBean> analysisBeans=mQuestion.getPad().getAnalysis();

        for(int i = 0;i<mFilledAnswers.size();i++){
            ConnectPositionInfo info;
            String answer=mFilledAnswers.get(i);

//            if(mCorrectAnswers.contains()){
//                isRight = true;
//            }

            if(TextUtils.isEmpty(answer)){
                info = new ConnectPositionInfo(-1,-1,false);
                mConnectPositionInfos.add(info);
            }else {
                boolean isRight = false;

                for (AnalysisBean analysisBean:analysisBeans){
                    if (answer.equals(analysisBean.key)){
                        isRight=analysisBean.status.equals(AnalysisBean.RIGHT);
                    }
                }

                String[] answers = answer.split(",");
                int left = Integer.parseInt(answers[0]);
                int right = Integer.parseInt(answers[1]);
                info = new ConnectPositionInfo(left,right,isRight);
            }
            mConnectPositionInfos.add(info);
        }
    }
    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_analysis_connect,container,false);
        return mRootView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        ConnectedView connectedView = (ConnectedView) mRootView.findViewById(R.id.connected_view);
        mStem = (TextView) mRootView.findViewById(R.id.stem);
        initData();
        if(TextUtils.isEmpty(mQuestion.getStem())) {
            mStem.setVisibility(View.GONE);
        } else {
            mStem.setVisibility(View.VISIBLE);
            mStem.post(new Runnable() {
                @Override
                public void run() {
                    mStem.setText(Html.fromHtml(mQuestion.getStem(), new HtmlImageGetter(mStem), null));
                }
            });
        }
        connectedView.setConnectPositionInfo(mConnectPositionInfos);
        connectedView.addItems(mChoicesLeft,mChoicesRight,mConnectPositionInfos);
    }

    @Override
    public void initAnalysisView() {
        if(Constants.HAS_FINISH_STATUS.equals(mQuestion.getPaperStatus())){ //已完成
            int status=mQuestion.getStatus();
            if (status==Constants.ANSWER_STATUS_RIGHT) {
                showAnswerResultView(true, mQuestion.getAnswerCompare(), null);
            }else {
                showAnswerResultView(false, mQuestion.getAnswerCompare(), null);
            }
            showDifficultyview(mQuestion.getStarCount());
            showAnalysisview(mQuestion.getQuestionAnalysis());
            showPointView(mQuestion.getPointList());
        }else{ //逾期未提交的作业 题目解析展示“难度”、“答案”、“题目解析”、“知识点”
            showDifficultyview(mQuestion.getStarCount());
            String answer=initCorrectAnswer(mQuestion.getCorrectAnswer());
            showAnswerView(answer);
            showAnalysisview(mQuestion.getQuestionAnalysis());
            showPointView(mQuestion.getPointList());
        }
    }

    private String initCorrectAnswer(List<String> correctAnswers){
        StringBuilder result =  new StringBuilder();
        for(String answer: correctAnswers){
            if(answer.contains(",")){
                String[] strs = answer.split(",");
                int leftPos = Integer.parseInt(strs[0]) + 1;
                int rightPos = Integer.parseInt(strs[1]) + 1;
                String r = "左" + String.valueOf(leftPos) + "连右" + String.valueOf(rightPos) + ",";
                result.append(r);
            }
        }
        if(result.lastIndexOf(",") == result.length() - 1){
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
