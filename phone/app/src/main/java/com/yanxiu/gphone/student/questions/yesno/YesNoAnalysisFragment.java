package com.yanxiu.gphone.student.questions.yesno;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

import static android.view.View.GONE;


/**
 * Created by 戴延枫 on 2017/6/7.
 */

public class YesNoAnalysisFragment extends AnalysisSimpleExerciseBaseFragment {
    private YesNoQuestion mData;
    private View mAnswerView;
    private TextView mQuestionView;
    private ChooseLayout mChooseView;

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
    }

    private void initView() {
        mQuestionView = (TextView) mAnswerView.findViewById(R.id.tv_question);
        mChooseView = (ChooseLayout) mAnswerView.findViewById(R.id.cl_answer);

    }

    private void initData() {
        Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mQuestionView), null);
        mQuestionView.setText(string);
        mChooseView.setData(mData.getChoice());
        List<String> datas = mData.getAnswerList();
        if (datas.size() > 0) {
            int result = Integer.parseInt(datas.get(0));
            if(result == 1){ //正确
                mChooseView.setSelect(0);
            }else{ //错误
                mChooseView.setSelect(1);
            }
        }
        int count = mChooseView.getChildCount();
        for (int i = 0; i < count; i++) {
            View choleView = mChooseView.getChildAt(i);
            ChooseLayout.ViewHolder viewHolder = (ChooseLayout.ViewHolder) choleView.getTag();
            viewHolder.mQuestionIdView.setVisibility(GONE);
        }
    }

    /**
     * 添加答题view
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mAnswerView = inflater.inflate(R.layout.fragment_yesno, container, false);
        return mAnswerView;
    }

    /**
     * 初始化答题view
     *
     * @param inflater
     * @param container
     */
    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        initView();
        initData();
    }

    /**
     * 显示解析view
     */
    @Override
    public void initAnalysisView() {
        showAnswerResultView(true,"你的打哪是阿达，啊啥京东卡啥京东卡");
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
}