package com.yanxiu.gphone.student.questions.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.cloze.ClozeAnswerComplexFragment;
import com.yanxiu.gphone.student.questions.yesno.YesNoQuestion;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

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
        mAnswerView=inflater.inflate(R.layout.fragment_choose,container,false);
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
        if(null != parentFragment && parentFragment instanceof ClozeAnswerComplexFragment){
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
        if (datas.size()>0){
            mChooseView.setSelect(Integer.parseInt(datas.get(datas.size()-1)));
        }
    }

    @Override
    public void initAnalysisView() {
        showView1();
        showView2();
        showView3();
    }
}