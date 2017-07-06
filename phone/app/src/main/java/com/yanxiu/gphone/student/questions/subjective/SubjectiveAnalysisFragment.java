package com.yanxiu.gphone.student.questions.subjective;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.Bean.PhotoDeleteBean;
import com.yanxiu.gphone.student.common.activity.CameraActivity;
import com.yanxiu.gphone.student.common.activity.CropImageActivity;
import com.yanxiu.gphone.student.common.activity.PhotoActivity;
import com.yanxiu.gphone.student.customviews.AlbumGridView;
import com.yanxiu.gphone.student.customviews.spantextview.SubjectClozeTextView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.util.StemUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:02.
 * Function :
 */
public class SubjectiveAnalysisFragment extends AnalysisSimpleExerciseBaseFragment implements AlbumGridView.onClickListener {

    private SubjectiveQuestion mData;
    private SubjectClozeTextView mQuestionView;
    private View mAnswerView;
    private AlbumGridView mSubjectView;

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
        mAnswerView = inflater.inflate(R.layout.fragment_subjective, container, false);
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
    }

    private void initView() {
        mQuestionView = (SubjectClozeTextView) mAnswerView.findViewById(R.id.tv_question);
        mSubjectView = (AlbumGridView) mAnswerView.findViewById(R.id.ag_image);
    }

    private void initData() {
        String string= StemUtil.initClozeStem(mData.getStem());
        mQuestionView.setText(string);
        mSubjectView.setData(mData.answerList);
        mSubjectView.setCanAddItem(false);
    }

    private void listener() {
        mSubjectView.addClickListener(this);
    }

    @Override
    public void onClick(int Type, int position) {
        switch (Type) {
            case AlbumGridView.TYPE_IMAGE:
                PhotoActivity.LaunchActivity(getContext(), mData.answerList, position, SubjectiveAnalysisFragment.this.hashCode(),PhotoActivity.DELETE_CANNOT);
                break;
        }
    }

}
