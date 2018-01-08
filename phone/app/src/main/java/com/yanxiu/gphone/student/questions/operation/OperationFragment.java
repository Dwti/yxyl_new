package com.yanxiu.gphone.student.questions.operation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetterNew;
import com.yanxiu.gphone.student.util.StemUtil;

import java.util.List;

/**
 * Created by sunpeng on 2017/12/25.
 */

public class OperationFragment extends AnswerSimpleExerciseBaseFragment {
    private OperationQuestion mQuestion;
    private View mRootView;
    private TextView mStem;
    private List<String> mImgUrls;
    //TODO initDataWithAnswer里面没有加操作题的判断，会影响答题报告，逻辑跟主观题是一样的
    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mQuestion = (OperationQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setData((OperationQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView  = inflater.inflate(R.layout.fragment_operation,container,false);
        initView();
        initData();
        setQaNumber(mRootView);
        setQaName(mRootView);
        initComplexStem(mRootView,mQuestion);
        return mRootView;
    }

    private void initView() {
        mStem = (TextView) mRootView.findViewById(R.id.tv_stem);
    }

    private void initData() {
        mStem.setText(Html.fromHtml(mQuestion.getStem(),new HtmlImageGetterNew(mStem),null));
        mImgUrls = mQuestion.getOperateImgUrls();
    }
}
