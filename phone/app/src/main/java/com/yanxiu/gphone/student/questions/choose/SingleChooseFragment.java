package com.yanxiu.gphone.student.questions.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.SimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class SingleChooseFragment extends SimpleExerciseBaseFragment implements ChooseLayout.onItemClickListener {
    private SingleChoiceQuestion mData;
    private TextView mQuestionView;
    private ChooseLayout mAnswerView;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        mData = (SingleChoiceQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData ==null) {
            setData((SingleChoiceQuestion) savedInstanceState.getSerializable(KEY_NODE));
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
        listener();
        initData();
        return view;
    }

    private void initView(View view) {
        mQuestionView= (TextView) view.findViewById(R.id.tv_question);
        mAnswerView= (ChooseLayout) view.findViewById(R.id.cl_answer);
    }

    private void listener() {
        mAnswerView.setSelectItemListener(SingleChooseFragment.this);
    }

    private void initData() {
        mQuestionView.setText(mData.getStem());
        mAnswerView.setData(mData.getChoice());
        List<String> datas=new ArrayList<>();
        datas.addAll(mData.getAnswerList());
        mData.getAnswerList().clear();
        if (datas.size()>0){
            mAnswerView.setSelect(Integer.parseInt(datas.get(datas.size()-1)));
        }
        Log.e("dyf", mData.numberStringForShow());
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser       true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param invokeInResumeOrPause true：发生在onResume或onPause方法里；false：本次回调发生在setUserVisibleHintMethod方法里
     */
    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {

    }

    @Override
    public void onClick(int position, boolean isSelected) {
        if(isSelected){
            mData.setAnswer(true);
            mData.getAnswerList().clear();
            mData.getAnswerList().add(String.valueOf(position));
        }else{
            mData.setAnswer(false);
            mData.getAnswerList().remove(0);
        }
        saveAnswer(mData);
        updateProgress();
    }
}