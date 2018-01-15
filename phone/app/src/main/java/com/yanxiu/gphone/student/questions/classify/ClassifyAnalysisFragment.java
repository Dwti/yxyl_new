package com.yanxiu.gphone.student.questions.classify;

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
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.ClassifyChoice;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.questions.choose.SingleChoiceQuestion;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by 戴延枫 on 2017/7/20.
 */

public class ClassifyAnalysisFragment extends AnalysisSimpleExerciseBaseFragment {
    private ClassifyQuestion mData;
    private View mAnswerView;
    private TextView mStemView;
    private ClassifyAnslysisItemView mClassify_choice;
    private ArrayList<ClassifyBean> mAnalysisData;//解析数据
    private ArrayList<String> mChoiceList;//选项list
    private List<String> mClassifyBasketList;//归类的类别list

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        mData = (ClassifyQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SingleChoiceQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
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
        mAnswerView = inflater.inflate(R.layout.fragment_classify_analysis, container, false);
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

    private void initView() {
        mStemView = (TextView) mAnswerView.findViewById(R.id.tv_question);
        mClassify_choice = (ClassifyAnslysisItemView) mAnswerView.findViewById(R.id.classify_choice);
    }

    private void initData() {
        mChoiceList = mData.getChoice();
        mClassifyBasketList = mData.getClassifyBasket();
        Log.e("dyf", "----------->正确答案" + mData.getClassifyAnswer().toString());
        Log.e("dyf", "----------->自己答案" + mData.getAnswerList().toString());
        iniAnalysisData();
        mClassify_choice.setData(mAnalysisData);
        Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mStemView), null);
        mStemView.setText(string);
    }

    //初始化mAnalysisData
    private void iniAnalysisData() {
        mAnalysisData = new ArrayList<>(mClassifyBasketList.size());

        //逾期未提交没有pad
        List<AnalysisBean> analysisBeanList=null;
        if (mData.getPad()!=null){
            analysisBeanList=mData.getPad().getAnalysis();
        }
        if (analysisBeanList!=null) {
            for (AnalysisBean bean : analysisBeanList) {
                ClassifyBean classifyBean = new ClassifyBean();
                String[] positions = bean.key.split(",");
                ArrayList<ClassifyItemBean> classifyItemBeenList = new ArrayList<>();
                for (int i = 0; i < bean.subStatus.size(); i++) {
                    String position = positions[i];
                    String status = bean.subStatus.get(i);

                    ClassifyItemBean itemBean = new ClassifyItemBean();
                    itemBean.setRight(AnalysisBean.RIGHT.equals(status));
                    itemBean.setContent(getChoiceContent(position));
                    classifyItemBeenList.add(itemBean);
                }
                classifyBean.setTitle(bean.name);
                classifyBean.setClassifyBeanArrayList(classifyItemBeenList);
                mAnalysisData.add(classifyBean);
            }
        }else {
            ArrayList<ClassifyItemBean> weiguileiList = new ArrayList<ClassifyItemBean>();//未作答选项list
            for (int i = 0; i < mChoiceList.size(); i++) {
                String content = mChoiceList.get(i);
                ClassifyItemBean classifyItemBean = new ClassifyItemBean();
                classifyItemBean.setContent(content);
                classifyItemBean.setRight(false);
                weiguileiList.add(classifyItemBean);
            }
            ClassifyBean weiguileiBean = new ClassifyBean();//封装的数据
            weiguileiBean.setTitle(getString(R.string.classify_drawer_noClassify));
            weiguileiBean.setClassifyBeanArrayList(weiguileiList);
            mAnalysisData.add(weiguileiBean);
        }
    }

    /**
     * 通过index，获取对应的choice的字符串内容
     *
     * @param index
     * @return
     */
    private String getChoiceContent(String index) {
        int id = -1;
        String chioce = null;
        try {
            id = Integer.parseInt(index);//获取id
            chioce = mChoiceList.get(id);//获取chioce的name
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chioce;
    }

    /**
     * 显示解析view
     */
    @Override
    public void initAnalysisView() {
        if (Constants.HAS_FINISH_STATUS.equals(mData.getPaperStatus())) { //已完成
            if (mData.getStatus() == Constants.ANSWER_STATUS_RIGHT) {
                showAnswerResultView(true, mData.getAnswerCompare(), null);
            } else {
                showAnswerResultView(false, mData.getAnswerCompare(), null);
            }
            showDifficultyview(mData.getStarCount());
            showAnalysisview(mData.getQuestionAnalysis());
            showPointView(mData.getPointList());
        } else { //逾期未提交的作业 题目解析展示“难度”、“答案”、“题目解析”、“知识点”
            showDifficultyview(mData.getStarCount());
            showAnswerView(mData.getAnalysisviewAnswer());
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