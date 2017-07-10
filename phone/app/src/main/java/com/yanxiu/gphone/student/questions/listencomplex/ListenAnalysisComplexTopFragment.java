package com.yanxiu.gphone.student.questions.listencomplex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ListenerSeekBarLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.TopBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/9 14:47.
 * Function :
 */
public class ListenAnalysisComplexTopFragment extends TopBaseFragment {

    private ListenComplexQuestion mData;
    private TextView mQuestionView;
    private ListenerSeekBarLayout mListenView;

    @Override
    public void setData(BaseQuestion data) {
        mData= (ListenComplexQuestion) data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_listencomplextop, container, false);
        initView(mRootView);
        initData();
        return mRootView;
    }

    private void initView(View root) {
        mQuestionView= (TextView) root.findViewById(R.id.tv_question);
        mListenView= (ListenerSeekBarLayout) root.findViewById(R.id.lsb_listen);
    }

    private void initData() {
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),null);
        mQuestionView.setText(string);
        mListenView.setUrl(mData.url);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mData.mProgress=mListenView.getProgress();
        mData.mMax=mListenView.getMax();
        mData.mIsShouldPlay=mListenView.getIsPlaying();
        mData.mIsPause=mListenView.getIsPause();
        mListenView.setDestory();
    }

    public void setVisibleToUser(boolean isVisibleToUser){
        if (isVisibleToUser){
            mListenView.setResume();
            if (mData.mIsShouldPlay&&!mData.mIsPause){
                mListenView.setPlayToProgress(mData.mProgress,mData.mMax);
                mData.mIsShouldPlay=false;
            }else if (mData.mIsShouldPlay&&mData.mIsPause){
                mListenView.setPauseToProgress(mData.mProgress,mData.mMax);
                mData.mIsShouldPlay=false;
            }
        }else {
            mListenView.setPause();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mData = (ListenComplexQuestion) savedInstanceState.getSerializable(ExerciseBaseFragment.KEY_NODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mData);
    }
}
