package com.yanxiu.gphone.student.questions.spoken;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/16 9:49.
 * Function :
 */
public class SpokenWrongFragment extends WrongSimpleExerciseBaseFragment {

    private SpokenQuestion mData;
    private View mAnswerView;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        mData= (SpokenQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SpokenQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mAnswerView=inflater.inflate(R.layout.fragment_spokenwrong,container,false);
        return mAnswerView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        initView();
        listener();
        initData();
    }

    private void initView(){

    }

    private void listener(){

    }

    private void initData(){

    }

    @Override
    public void initAnalysisView() {

    }
}
