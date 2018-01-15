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
import com.yanxiu.gphone.student.mistakeredo.MistakeRedoActivity;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/7/17.
 */

public class ConnectWrongFragment extends WrongSimpleExerciseBaseFragment {
    private ConnectQuestion mQuestion;
    private View mRootView;
    private TextView mStem;
    private List<ConnectAnalysisItemBean> mChoicesLeft, mChoicesRight;
    private List<String> mFilledAnswers, mCorrectAnswers;
    private List<ConnectPositionInfo> mConnectPositionInfos = new ArrayList<>();


    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mQuestion = (ConnectQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null&&mQuestion==null) {
            setData((ConnectQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    private void initAnalysisData(){
        mChoicesLeft = mQuestion.getLeftChoices();
        mChoicesRight = mQuestion.getRightChoices();

        mFilledAnswers = mQuestion.getFilledAnswers();
        List<AnalysisBean> analysisBeans=mQuestion.getPad().getAnalysis();

        for(int i = 0;i<mFilledAnswers.size();i++){
            ConnectPositionInfo info;
            String answer=mFilledAnswers.get(i);

            if(TextUtils.isEmpty(answer)){
                info = new ConnectPositionInfo(-1,-1,false);
                mConnectPositionInfos.add(info);
            }else {
                boolean isRight = false;
                String[] answers = answer.split(",");
                int left = Integer.parseInt(answers[0]);
                int right = Integer.parseInt(answers[1]);

                for (AnalysisBean analysisBean:analysisBeans){
                    String[] Keyanswers = analysisBean.key.split(",");
                    int Keyleft = Integer.parseInt(Keyanswers[0]);
                    //只判断左边，极易出现误差，根源在连线题写法上面，问题太多
                    if (left==Keyleft){
                        isRight=analysisBean.status.equals(AnalysisBean.RIGHT);
                    }
                }

                info = new ConnectPositionInfo(left,right,isRight);
            }
            mConnectPositionInfos.add(info);
        }
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_wrong_analysis_connect, container, false);
        return mRootView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        ConnectedAnalysisView connectedView = (ConnectedAnalysisView) mRootView.findViewById(R.id.connected_view);
        mStem = (TextView) mRootView.findViewById(R.id.stem);
            initAnalysisData();
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
        connectedView.addItems(mChoicesLeft, mChoicesRight, mConnectPositionInfos);
    }

    @Override
    public void initAnalysisView() {
        int status=mQuestion.getStatus();
        if (status==Constants.ANSWER_STATUS_RIGHT) {
            showAnswerResultView(true, mQuestion.getAnswerCompare(), null);
        }else {
            showAnswerResultView(false, mQuestion.getAnswerCompare(), null);
        }
        showDifficultyview(mQuestion.getStarCount());
        showAnalysisview(mQuestion.getQuestionAnalysis());
        showPointView(mQuestion.getPointList());
        showNoteView(mQuestion.getJsonNoteBean());
    }
}
