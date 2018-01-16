package com.yanxiu.gphone.student.questions.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/26 15:32.
 * Function :
 */
public class MultiChooseRedoFragment extends RedoSimpleExerciseBaseFragment implements ChooseLayout.onItemClickListener {
    private MultiChoiceQuestion mData;
    private TextView mQuestionView;
    private ChooseLayout mAnswerView;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        mData = (MultiChoiceQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((MultiChoiceQuestion) savedInstanceState.getSerializable(KEY_NODE));
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
        View view = inflater.inflate(R.layout.fragment_choose, container, false);
        setQaNumber(view);
        setQaName(view);
        initView(view);
        initComplexStem(view,mData);
        listener();
        initData();
        return view;
    }

    private void initView(View view) {
        mQuestionView = (TextView) view.findViewById(R.id.tv_question);
        mAnswerView = (ChooseLayout) view.findViewById(R.id.cl_answer);
    }

    private void listener() {
        mAnswerView.setSelectItemListener(MultiChooseRedoFragment.this);
    }

    private void initData() {
        if (!TextUtils.isEmpty(mData.getStem())) {
            Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mQuestionView), null);
            mQuestionView.setText(string);
        }else {
            mQuestionView.setVisibility(View.GONE);
        }
        mAnswerView.setData(mData.getChoice());
        mAnswerView.setChooseType(ChooseLayout.TYPE_MULTI);
        List<String> datas = mData.getAnswerList();
        for (int i = 0; i < datas.size(); i++) {
            mAnswerView.setSelect(Integer.parseInt(datas.get(i)));
        }
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser       true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param invokeInResumeOrPause true：发生在onResume或onPause方法里；false：本次回调发生在setUserVisibleHintMethod方法里
     */
    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser,invokeInResumeOrPause);
    }

    @Override
    public void onClick(int position, boolean isSelected) {
        if (isSelected) {
            mData.getAnswerList().add(String.valueOf(position));
         } else {
            mData.getAnswerList().remove(String.valueOf(position));
        }
        if (mData.getAnswerList().size()>0){
            mData.setHasAnswered(true);
        }else {
            mData.setHasAnswered(false);
        }
        saveAnswer(mData);
        updateProgress();
    }
}
