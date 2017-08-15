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
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;
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
    private List<String> mChoicesLeft, mChoicesRight;
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

    private void initData() {
        mChoicesLeft = mQuestion.getLeftChoices();
        mChoicesRight = mQuestion.getRightChoices();

        mFilledAnswers = mQuestion.getFilledAnswers();
        mCorrectAnswers = mQuestion.getCorrectAnswer();
        for (int i = 0; i < mFilledAnswers.size(); i++) {
            ConnectPositionInfo info;
            boolean isRight = false;
            if (mCorrectAnswers.contains(mFilledAnswers.get(i))) {
                isRight = true;
            }
            if (TextUtils.isEmpty(mFilledAnswers.get(i))) {
                info = new ConnectPositionInfo(-1, -1, false);
                mConnectPositionInfos.add(info);
            } else {
                String[] answers = mFilledAnswers.get(i).split(",");
                int left = Integer.parseInt(answers[0]);
                int right = Integer.parseInt(answers[1]);
                info = new ConnectPositionInfo(left, right, isRight);
            }
            mConnectPositionInfos.add(info);
        }
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_analysis_connect, container, false);
        return mRootView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        ConnectedView connectedView = (ConnectedView) mRootView.findViewById(R.id.connected_view);
        mStem = (TextView) mRootView.findViewById(R.id.stem);
        initData();
        mStem.post(new Runnable() {
            @Override
            public void run() {
                mStem.setText(Html.fromHtml(mQuestion.getStem(), new HtmlImageGetter(mStem), null));
            }
        });
        connectedView.setConnectPositionInfo(mConnectPositionInfos);
        connectedView.addItems(mChoicesLeft, mChoicesRight, mConnectPositionInfos);
    }

    @Override
    public void initAnalysisView() {
        showAnswerResultView(mQuestion.isRight(), mQuestion.getAnswerCompare(), null);
        showDifficultyview(mQuestion.getStarCount());
        showAnalysisview(mQuestion.getQuestionAnalysis());
        showPointView(mQuestion.getPointList());
        showNoteView(mQuestion.getJsonNoteBean());
    }
}
