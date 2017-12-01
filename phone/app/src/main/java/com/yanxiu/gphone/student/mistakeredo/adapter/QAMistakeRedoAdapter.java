package com.yanxiu.gphone.student.mistakeredo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerStateChangedListener;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.user.mistake.response.MistakeDeleteMessage;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sunpeng
 */

public class QAMistakeRedoAdapter extends FragmentStatePagerAdapter {
    private String mSubjectId;
    private ArrayList<BaseQuestion> mDatas = new ArrayList<>();
    private ViewPager.OnPageChangeListener mOnInnerPageChangeListener;  //Fragment内部的ViewPager滑动改变事件
    private OnAnswerStateChangedListener mOnAnswerStateChangedListener;

    public QAMistakeRedoAdapter(FragmentManager fragmentManager,ViewPager.OnPageChangeListener onInnerPageChangeListener,OnAnswerStateChangedListener onAnswerStateChangedListener) {
        super(fragmentManager);
        this.mOnInnerPageChangeListener = onInnerPageChangeListener;
        this.mOnAnswerStateChangedListener = onAnswerStateChangedListener;
    }

    public QAMistakeRedoAdapter(FragmentManager fragmentManager, List<BaseQuestion> datas,ViewPager.OnPageChangeListener onInnerPageChangeListener) {
        super(fragmentManager);
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
        this.mOnInnerPageChangeListener = onInnerPageChangeListener;
    }


    public void setData(ArrayList<BaseQuestion> datas,int wrongNum) {
        if(datas == null){
            return;
        }
        this.mDatas = datas;
        Paper.generateUsedNumbersForWrongQuestion(mDatas, wrongNum);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<BaseQuestion> datas,int wrongNum) {
        if(datas == null){
            return;
        }
        this.mDatas.addAll(datas);
        Paper.generateUsedNumbersForWrongQuestion(mDatas, wrongNum);
        notifyDataSetChanged();
    }

    public String getQidByPosition(int position) {
        if (position > -1 && position < mDatas.size()) {
            BaseQuestion baseQuestion = mDatas.get(position);
            if (!TextUtils.isEmpty(baseQuestion.getTypeId_complexToSimple())){
                return  baseQuestion.getQid_ComplexToSimple();
            }else {
                return baseQuestion.getQid();
            }
        }
        return "";
    }


    public void deleteItem(int position, int wrongNum) {
        //TODO 需要考虑删除复合题小题的情况
        if (position > -1 && position < mDatas.size()) {
            BaseQuestion baseQuestion = mDatas.get(position);
            MistakeDeleteMessage deleteMessage = new MistakeDeleteMessage();
            deleteMessage.position = position;
            if (!TextUtils.isEmpty(baseQuestion.getTypeId_complexToSimple())){
                deleteMessage.questionId = baseQuestion.getQid_ComplexToSimple();
            }else {
                deleteMessage.questionId = baseQuestion.getQid();
            }
            deleteMessage.wrongNum=wrongNum;
            deleteMessage.subjectId=mSubjectId;
            EventBus.getDefault().post(deleteMessage);
        }
    }

    public void setSubjectId(String subjectId){
        this.mSubjectId=subjectId;
    }

    public List<BaseQuestion> getDatas() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ExerciseBaseFragment fragment = (ExerciseBaseFragment) super.instantiateItem(container, position);
        if(fragment instanceof RedoComplexExerciseBaseFragment && mOnInnerPageChangeListener != null){
            ((RedoComplexExerciseBaseFragment) fragment).addOnPageSelectedListener(mOnInnerPageChangeListener);
        }
        if(fragment instanceof RedoSimpleExerciseBaseFragment && mOnAnswerStateChangedListener != null){
            ((RedoSimpleExerciseBaseFragment)fragment).setAnswerStateChangedListener(mOnAnswerStateChangedListener);
        }
        if(fragment instanceof RedoComplexExerciseBaseFragment && mOnAnswerStateChangedListener != null){
            ((RedoComplexExerciseBaseFragment) fragment).setOnAnswerStateChangeListener(mOnAnswerStateChangedListener);
        }
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE;
    }

}


