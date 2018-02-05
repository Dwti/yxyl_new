package com.yanxiu.gphone.student.questions.operation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.activity.PhotoActivity;
import com.yanxiu.gphone.student.customviews.AlbumGridView;
import com.yanxiu.gphone.student.customviews.spantextview.SubjectClozeTextView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;
import com.yanxiu.gphone.student.util.StemUtil;

import java.util.ListIterator;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/29 10:45.
 * Function :
 */
public class OperationWrongFragment extends WrongSimpleExerciseBaseFragment implements AlbumGridView.onClickListener {

    private OperationQuestion mData;
    private SubjectClozeTextView mQuestionView;
    private View mAnswerView;
    private AlbumGridView mSubjectView;
    private TextView mNoPictureView;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mData = (OperationQuestion) node;
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
        mAnswerView = inflater.inflate(R.layout.fragment_analysis_operation, container, false);
        return mAnswerView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        initView();
        listener();
        initData();
    }

    private void initView() {
        mQuestionView = (SubjectClozeTextView) mAnswerView.findViewById(R.id.tv_question);
        mSubjectView = (AlbumGridView) mAnswerView.findViewById(R.id.ag_image);
        mNoPictureView= (TextView) mAnswerView.findViewById(R.id.no_picture);
    }

    private void listener() {
        mSubjectView.addClickListener(this);
    }

    private void initData() {
        String string= StemUtil.initClozeStem(StemUtil.initOperationStem(mData.getStem()));
        mQuestionView.setText(string);
        //去掉空字符串
        ListIterator<String> listIterator  = mData.getAnswerList().listIterator();
        while (listIterator.hasNext()){
            if(TextUtils.isEmpty(listIterator.next())){
                listIterator.remove();
            }
        }
        if (mData.getAnswerList().size()>0) {
            mNoPictureView.setVisibility(View.GONE);
            mSubjectView.setData(mData.getAnswerList());
            mSubjectView.setCanAddItem(false);
        }else {
            mNoPictureView.setVisibility(View.VISIBLE);
            mSubjectView.setVisibility(View.GONE);
        }
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

        showScoreView(String.valueOf(mData.getScore()));
        showVoiceScoldedView(mData.getAudioList());
        showDifficultyview(mData.getStarCount());
        showAnswerView(answer);
        showAnalysisview(mData.getQuestionAnalysis());
        showPointView(mData.getPointList());
        showNoteView(mData.getJsonNoteBean());
    }

    @Override
    public void onClick(int Type, int position) {
        switch (Type) {
            case AlbumGridView.TYPE_IMAGE:
                mToVoiceIsIntent=true;
                PhotoActivity.LaunchActivity(getContext(), mData.getAnswerList(), position, this.hashCode(),PhotoActivity.DELETE_CANNOT);
                break;
        }
    }
}
