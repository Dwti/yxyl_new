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
import com.yanxiu.gphone.student.common.eventbus.SingleChooseMessage;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.cloze.ClozeAnswerComplexFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class SingleChooseRedoFragment extends AnswerSimpleExerciseBaseFragment implements ChooseLayout.onItemClickListener {
    private SingleChoiceQuestion mData;
    private TextView mQuestionView;
    private ChooseLayout mAnswerView;
    private SingleChooseMessage mMessage;
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
        initComplexStem(view,mData);
        listener();
        initData();
        return view;
    }

    private void initView(View view) {
        mQuestionView= (TextView) view.findViewById(R.id.tv_question);
        mAnswerView= (ChooseLayout) view.findViewById(R.id.cl_answer);
        hiddenNumberBar(view);
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

    /**
     * 在完形填空里，单选子题EventBus需要的hashcode
     * @return
     */
    private int getClozeAnsweHashCode(){
        Fragment parentFragment = getParentFragment();
        if(null != parentFragment && parentFragment instanceof ClozeAnswerComplexFragment){
            return ((ClozeAnswerComplexFragment) parentFragment).mHashCode;
        }
        return -1;
    }

    private void listener() {
        mAnswerView.setSelectItemListener(SingleChooseRedoFragment.this);
    }

    private void initData() {
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),null);
        mQuestionView.setText(string);
        mAnswerView.setData(mData.getChoice());
        List<String> datas=mData.getAnswerList();
        if (datas.size()>0){
            mAnswerView.setSelect(Integer.parseInt(datas.get(datas.size()-1)));
        }
        mMessage = new SingleChooseMessage();
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
        super.onVisibilityChangedToUser(isVisibleToUser,invokeInResumeOrPause);
    }

    @Override
    public void onClick(int position, boolean isSelected) {
        mMessage.hascode=getClozeAnsweHashCode();
        if(isSelected){
            mData.setHasAnswered(true);
            mData.getAnswerList().clear();
            mData.getAnswerList().add(String.valueOf(position));
            mMessage.answer=mData.getChoice().get(position);
        }else{
            mData.setHasAnswered(false);
            mData.getAnswerList().remove(0);
            mMessage.answer="";
        }
        EventBus.getDefault().post(mMessage);
        saveAnswer(mData);
        updateProgress();
    }
}