package com.yanxiu.gphone.student.questions.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.cloze.ClozeAnswerComplexFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class MultiChooseAnalysisFragment extends AnalysisSimpleExerciseBaseFragment {

    private MultiChoiceQuestion mData;
    private View mAnswerView;
    private TextView mQuestionView;
    private ChooseLayout mChooseView;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        this.mData= (MultiChoiceQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((MultiChoiceQuestion) savedInstanceState.getSerializable(KEY_NODE));
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
    }

    private void initData(){
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),null);
        mQuestionView.setText(string);
        mChooseView.setIsClick(false);
        mChooseView.setChooseType(ChooseLayout.TYPE_MULTI);
        mChooseView.setData(mData.getChoice());
        List<String> datas = mData.getAnswerList();
        for (int i = 0; i < datas.size(); i++) {
            mChooseView.setSelect(Integer.parseInt(datas.get(i)));
        }
    }

    @Override
    public void initAnalysisView() {
        showView1();
        showView2();
        showView3();
    }
}