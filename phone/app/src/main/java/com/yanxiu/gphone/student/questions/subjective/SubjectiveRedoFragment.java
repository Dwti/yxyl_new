package com.yanxiu.gphone.student.questions.subjective;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.common.Bean.CropCallbackMessage;
import com.yanxiu.gphone.student.common.Bean.PhotoDeleteBean;
import com.yanxiu.gphone.student.common.activity.CameraActivity;
import com.yanxiu.gphone.student.common.activity.PhotoActivity;
import com.yanxiu.gphone.student.customviews.AlbumGridView;
import com.yanxiu.gphone.student.customviews.spantextview.SubjectClozeTextView;
import com.yanxiu.gphone.student.homework.homeworkdetail.HomeworkDetailContract;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.util.StemUtil;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:02.
 * Function :
 */
public class SubjectiveRedoFragment extends WrongSimpleExerciseBaseFragment implements AlbumGridView.onClickListener {
    private SubjectiveQuestion mData;
    private SubjectClozeTextView mQuestionView;
    private View mAnswerView,ll_check_analysis,fl_analysis,btnCheck;
    private AlbumGridView mSubjectView;
    private TextView mNoPictureView;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mData = (SubjectiveQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SubjectiveQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mAnswerView = inflater.inflate(R.layout.fragment_redo_subjective, container, false);
        return mAnswerView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        initView();
        initData();
        listener();
    }

    @Override
    public void initAnalysisView() {
        String answer = "";
        if (mData.getSubjectAnswer() != null) {
            for (int i = 0; i < mData.getSubjectAnswer().size(); i++) {
                if(i < mData.getSubjectAnswer().size() -1){// 不是最后一个
                    answer += mData.getSubjectAnswer().get(i) + ",";
                }else{
                    answer += mData.getSubjectAnswer().get(i);
                }
            }
        }
        if (mData.getTypeId() == QuestionUtil.QUESTION_TYP.QUESTION_FILL_BLANKS.type || mData.getTypeId() == QuestionUtil.QUESTION_TYP.QUESTION_TRANSLATION.type || mData.getTypeId() == QuestionUtil.QUESTION_TYP.QUESTION_SUBJECTSWERE.type) {
            String result;
            if (mData.getScore() == 5) {
                result = getString(R.string.correct);
                showAnswerResultView(true, null, result);
            } else {
                result = getString(R.string.wrong);
                showAnswerResultView(false, null, result);
            }
        } else {
            showScoreView(String.valueOf(mData.getScore()));
        }
        showVoiceScoldedView(mData.getAudioList());
        showDifficultyview(mData.getStarCount());
        showAnswerView(answer);
        showAnalysisview(mData.getQuestionAnalysis());
        showPointView(mData.getPointList());
        showNoteView(mData.getJsonNoteBean());
    }

    private void initView() {
        mQuestionView = (SubjectClozeTextView) mAnswerView.findViewById(R.id.tv_question);
        mSubjectView = (AlbumGridView) mAnswerView.findViewById(R.id.ag_image);
        mNoPictureView = (TextView) mAnswerView.findViewById(R.id.no_picture);
        ll_check_analysis = mAnswerView.findViewById(R.id.ll_check_analysis);
        fl_analysis = mAnswerView.findViewById(R.id.fl_analysis);
        btnCheck = mAnswerView.findViewById(R.id.btn_check);
        setAnalysisVisible(View.GONE);
    }

    private void initData() {
        String string = StemUtil.initClozeStem(mData.getStem());
        mQuestionView.setText(string);
        if (mData.answerList.size() > 0) {
            mNoPictureView.setVisibility(View.GONE);
            mSubjectView.setData(mData.answerList);
            mSubjectView.setCanAddItem(false);
        } else {
            mNoPictureView.setVisibility(View.VISIBLE);
            mSubjectView.setVisibility(View.GONE);
        }
    }

    private void listener() {
        mSubjectView.addClickListener(this);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_check_analysis.setVisibility(View.GONE);
                setAnalysisVisible(View.VISIBLE);
                fl_analysis.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(int Type, int position) {
        switch (Type) {
            case AlbumGridView.TYPE_IMAGE:
                mToVoiceIsIntent = true;
                PhotoActivity.LaunchActivity(getContext(), mData.answerList, position, SubjectiveRedoFragment.this.hashCode(), PhotoActivity.DELETE_CANNOT);
                break;
        }
    }

}
