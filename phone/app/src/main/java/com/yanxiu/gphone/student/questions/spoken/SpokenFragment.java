package com.yanxiu.gphone.student.questions.spoken;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/16 9:43.
 * Function :
 */
public class SpokenFragment extends AnswerSimpleExerciseBaseFragment {

    private SpokenQuestion mData;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mData= (SpokenQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SubjectiveQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_spoken,container,false);
        setQaNumber(view);
        setQaName(view);
        initComplexStem(view, mData);
        initView(view);
        listener();
        initData();
        return view;
    }

    private void initView(View view) {

    }

    private void listener() {

    }

    private void initData() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }


}
