package com.yanxiu.gphone.student.questions.classify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.mistakeredo.MistakeRedoActivity;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.questions.choose.SingleChoiceQuestion;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 戴延枫 on 2017/8/9.
 */

public class ClassifyWrongFragment extends WrongSimpleExerciseBaseFragment {
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
        iniAnalysisData();
        mClassify_choice.setData(mAnalysisData);
        if (!TextUtils.isEmpty(mData.getStem())) {
            Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mStemView), null);
            mStemView.setText(string);
        }else {
            mStemView.setVisibility(View.GONE);
        }
    }

    //初始化mAnalysisData
    private void iniAnalysisData() {
        mAnalysisData = new ArrayList<>(mClassifyBasketList.size());

            List<AnalysisBean> analysisBeanList=mData.getPad().getAnalysis();
            for (AnalysisBean bean:analysisBeanList){
                ClassifyBean classifyBean=new ClassifyBean();
                String[] positions=bean.key.split(",");
                ArrayList<ClassifyItemBean> classifyItemBeenList = new ArrayList<>();
                for (int i=0;i<bean.subStatus.size();i++){
                    String position=positions[i];
                    String status=bean.subStatus.get(i);

                    ClassifyItemBean itemBean=new ClassifyItemBean();
                    itemBean.setRight(AnalysisBean.RIGHT.equals(status));
                    itemBean.setContent(getChoiceContent(position));
                    classifyItemBeenList.add(itemBean);
                }
                classifyBean.setTitle(bean.name);
                classifyBean.setClassifyBeanArrayList(classifyItemBeenList);
                mAnalysisData.add(classifyBean);
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
        if (mData.getStatus() == Constants.ANSWER_STATUS_RIGHT) {
            showAnswerResultView(true, mData.getAnswerCompare(), null,-1,getResources().getDimensionPixelSize(R.dimen.classify_choice_img_height));
        } else {
            showAnswerResultView(false, mData.getAnswerCompare(), null,-1,getResources().getDimensionPixelSize(R.dimen.classify_choice_img_height));
        }
        showDifficultyview(mData.getStarCount());
        showAnalysisview(mData.getQuestionAnalysis());
        showPointView(mData.getPointList());
        showNoteView(mData.getJsonNoteBean());

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