package com.yanxiu.gphone.student.questions.yesno;

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
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by 戴延枫 on 2017/6/7.
 */

public class YesNoFragment extends AnswerSimpleExerciseBaseFragment implements ChooseLayout.onItemClickListener {
    private YesNoQuestion mData;
    private TextView mQuestionView;
    private ChooseLayout mAnswerView;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        mData = (YesNoQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((YesNoQuestion) savedInstanceState.getSerializable(KEY_NODE));
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
        View view = inflater.inflate(R.layout.fragment_yesno, container, false);
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
        mAnswerView.setSelectItemListener(YesNoFragment.this);
    }

    private void initData() {
        if (!TextUtils.isEmpty(mData.getStem())) {
            Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mQuestionView), null);
            mQuestionView.setText(string);
        }else {
            mQuestionView.setVisibility(GONE);
        }
        mAnswerView.setData(mData.getChoice());
        List<String> datas = mData.getAnswerList();
        if (datas.size() > 0) {
            int result = Integer.parseInt(datas.get(0));
            if(result == 1){ //正确
                mAnswerView.setSelect(0);
            }else{ //错误
                mAnswerView.setSelect(1);
            }

        }
        int count = mAnswerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View choleView = mAnswerView.getChildAt(i);
            ChooseLayout.ViewHolder viewHolder = (ChooseLayout.ViewHolder) choleView.getTag();
            viewHolder.mQuestionIdView.setVisibility(GONE);
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
            mData.setHasAnswered(true);
            mData.getAnswerList().clear();
            switch (position){
                case 0:
                    mData.getAnswerList().add(String.valueOf(1));//正确为1
                    break;
                case 1:
                    mData.getAnswerList().add(String.valueOf(0));//错误为0
                    break;
            }

        } else {
            mData.setHasAnswered(false);
            mData.getAnswerList().remove(0);
        }
        saveAnswer(mData);
        updateProgress();
    }
}