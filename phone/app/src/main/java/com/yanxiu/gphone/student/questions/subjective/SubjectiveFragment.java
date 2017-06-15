package com.yanxiu.gphone.student.questions.subjective;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.SimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:02.
 * Function :
 */
public class SubjectiveFragment extends SimpleExerciseBaseFragment {

    private SubjectiveQuestion mData;
    private TextView mQuestionView;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mData= (SubjectiveQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData ==null) {
            setData((SubjectiveQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_subjective,container,false);
        setQaNumber(view);
        setQaName(view);
        initView(view);
        initComplexStem(view,mData);
        initData();
        listener();
        return view;
    }

    private void initView(View view) {
        mQuestionView= (TextView) view.findViewById(R.id.tv_question);
    }

    private void initData() {
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),null);
        mQuestionView.setText(string);

    }

    private void listener() {

    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser,invokeInResumeOrPause);
    }
}
