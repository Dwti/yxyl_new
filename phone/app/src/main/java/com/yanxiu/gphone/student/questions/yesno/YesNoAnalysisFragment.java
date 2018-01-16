package com.yanxiu.gphone.student.questions.yesno;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
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
        if (!TextUtils.isEmpty(mData.getStem())) {
            Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mQuestionView), null);
            mQuestionView.setText(string);
        }else {
            mQuestionView.setVisibility(GONE);
        }
        mChooseView.setIsClick(false);
        mChooseView.setYesyNoData(mData.getChoice());
        List<String> datas = mData.getAnswerList();
        String answer = mData.getYesNoAnswer();
        int answer_position;
        if(Integer.parseInt(answer) == 0){ //错误
            answer_position = 1;
        }else{ //正确
            answer_position = 0;
        }

        int count = mChooseView.getChildCount();
        for (int i = 0; i < count; i++) {
            View choleView = mChooseView.getChildAt(i);
            ChooseLayout.ViewHolder viewHolder = (ChooseLayout.ViewHolder) choleView.getTag();
            viewHolder.mQuestionIdView.setVisibility(GONE);
        }

        if (datas.size() > 0) {
            String select = datas.get(0);
            ChooseLayout.ViewHolder selectViewHolder = null;
            int result = Integer.parseInt(datas.get(0));
            int select_position;
            if (result == 1) { //正确
                select_position = 0;
                mChooseView.setSelect(0);
                selectViewHolder = (ChooseLayout.ViewHolder) mChooseView.getChildAt(0).getTag();
            } else if(result == 0){ //错误
                select_position = 1;
                mChooseView.setSelect(1);
                selectViewHolder = (ChooseLayout.ViewHolder) mChooseView.getChildAt(1).getTag();
            }else{
                select_position = -1;
            }

            if (count > select_position) {
                List<AnalysisBean> analysisBeans=mData.getPad().getAnalysis();
                if (analysisBeans!=null&&!analysisBeans.isEmpty()&&AnalysisBean.RIGHT.equals(analysisBeans.get(0).status)) {
                    selectViewHolder.mQuestionContentView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.choose_right));
                    selectViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ffffff));
                } else {
                    if (count > answer_position) {
                        ChooseLayout.ViewHolder answerViewHolder = (ChooseLayout.ViewHolder) mChooseView.getChildAt(answer_position).getTag();

                        selectViewHolder.mQuestionContentView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.choose_wrong));
                        selectViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ffffff));
                        selectViewHolder.mQuestionSelectView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.single_select_wrong));

                        answerViewHolder.mQuestionContentView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.choose_right));
                        answerViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ffffff));
                    }
                }
            }
        } else {
            if (count > answer_position) {
                ChooseLayout.ViewHolder answerViewHolder = (ChooseLayout.ViewHolder) mChooseView.getChildAt(answer_position).getTag();
                answerViewHolder.mQuestionContentView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.choose_right));
                answerViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ffffff));
            }
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
        if(Constants.HAS_FINISH_STATUS.equals(mData.getPaperStatus())){ //已完成
            if (mData.getStatus()== Constants.ANSWER_STATUS_RIGHT){
                showAnswerResultView(true, mData.getAnswerCompare(), null);
            }else {
                showAnswerResultView(false, mData.getAnswerCompare(), null);
            }
            showDifficultyview(mData.getStarCount());
            showAnalysisview(mData.getQuestionAnalysis());
            showPointView(mData.getPointList());
        }else{ //逾期未提交的作业 题目解析展示“难度”、“答案”、“题目解析”、“知识点”
            showDifficultyview(mData.getStarCount());
//            showAnswerView(mData.getYesNoAnswer());
            showAnalysisview(mData.getQuestionAnalysis());
            showPointView(mData.getPointList());
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
        super.onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
    }
}