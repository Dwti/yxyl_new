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
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.AlbumGridView;
import com.yanxiu.gphone.student.customviews.spantextview.SubjectClozeTextView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;
import com.yanxiu.gphone.student.util.StemUtil;

import java.util.ListIterator;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:02.
 * Function :
 */
public class OperationAnalysisFragment extends AnalysisSimpleExerciseBaseFragment implements AlbumGridView.onClickListener {

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
        initData();
        listener();
    }

    @Override
    public void initAnalysisView() {
        String answer="";
        if (mData.getSubjectAnswer()!=null){
            for (int i = 0; i < mData.getSubjectAnswer().size(); i++) {
                if(i < mData.getSubjectAnswer().size() -1){// 不是最后一个
                    answer += mData.getSubjectAnswer().get(i) + ",";
                }else{
                    answer += mData.getSubjectAnswer().get(i);
                }
            }
        }
        if(Constants.HAS_FINISH_STATUS.equals(mData.getPaperStatus())){ //已完成
            /* *
             * 复制时候看一下，操作题型是不会有翻译题这种题的
             * cwq
             * */
//            if (mData.getTypeId()== QuestionUtil.QUESTION_TYP.QUESTION_FILL_BLANKS.type||mData.getTypeId()== QuestionUtil.QUESTION_TYP.QUESTION_TRANSLATION.type||mData.getTypeId()== QuestionUtil.QUESTION_TYP.QUESTION_SUBJECTSWERE.type){
//                String result;
//                if (mData.getScore()==5){
//                    result=getString(R.string.correct);
//                    showAnswerResultView(true,null,result);
//                }else {
//                    result=getString(R.string.wrong);
//                    showAnswerResultView(false,null,result);
//                }
//            }else {
                showScoreView(String.valueOf(mData.getScore()));
//            }
            showVoiceScoldedView(mData.getAudioList());
            showDifficultyview(mData.getStarCount());
            showAnswerView(answer);
            showAnalysisview(mData.getQuestionAnalysis());
            showPointView(mData.getPointList());
        }else{ //逾期未提交的作业 题目解析展示“难度”、“答案”、“题目解析”、“知识点”
            showDifficultyview(mData.getStarCount());
            showAnswerView(answer);
            showAnalysisview(mData.getQuestionAnalysis());
            showPointView(mData.getPointList());
        }
    }

    private void initView() {
        mQuestionView = (SubjectClozeTextView) mAnswerView.findViewById(R.id.tv_question);
        mSubjectView = (AlbumGridView) mAnswerView.findViewById(R.id.ag_image);
        mNoPictureView= (TextView) mAnswerView.findViewById(R.id.no_picture);
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

    private void listener() {
        mSubjectView.addClickListener(this);
    }

    @Override
    public void onClick(int Type, int position) {
        switch (Type) {
            case AlbumGridView.TYPE_IMAGE:
                mToVoiceIsIntent=true;
                PhotoActivity.LaunchActivity(getContext(), mData.getAnswerList(), position, OperationAnalysisFragment.this.hashCode(),PhotoActivity.DELETE_CANNOT);
                break;
        }
    }

}