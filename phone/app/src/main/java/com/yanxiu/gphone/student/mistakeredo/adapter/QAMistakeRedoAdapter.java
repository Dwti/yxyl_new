package com.yanxiu.gphone.student.mistakeredo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
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

    public QAMistakeRedoAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public QAMistakeRedoAdapter(FragmentManager fragmentManager, List<BaseQuestion> datas) {
        super(fragmentManager);
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
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
        Object fragment = super.instantiateItem(container, position);
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}


