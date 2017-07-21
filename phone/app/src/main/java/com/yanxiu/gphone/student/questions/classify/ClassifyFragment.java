package com.yanxiu.gphone.student.questions.classify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ClassifyChoice;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.choose.SingleChoiceQuestion;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.CENTER_HORIZONTAL;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class ClassifyFragment extends AnswerSimpleExerciseBaseFragment implements ClassifyAdapter.OnItemClickLitener, ClassifyChoice.OnClassifyChoiceItemLitener {
    private ClassifyQuestion mData;
    private ArrayList<String> mChoiceList;
    private ArrayList<String> mHasChoosedChoiceList;//用户选择后的list，用来刷新choiceView
    private ArrayList<String> mHasChoosedChoiceListForNotifyDataChange = new ArrayList<>();//用户选择后的list，用来刷新choiceView
    private List<String> mClassifyBasketList;
    private TextView mQuestionView;
    private ClassifyChoice mClassify_choice;
    private RecyclerView mClassify_recyclerview;
    private ClassifyAdapter mClassifyAdapter;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        setQaNumber(view);
        setQaName(view);
        initView(view);
        initComplexStem(view, mData);
        listener();
        initData();
        return view;
    }

    private void initView(View view) {
        mQuestionView = (TextView) view.findViewById(R.id.tv_question);
        mClassify_choice = (ClassifyChoice) view.findViewById(R.id.classify_choice);
        mClassify_recyclerview = (RecyclerView) view.findViewById(R.id.classify_recyclerview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mClassify_recyclerview.setLayoutManager(linearLayoutManager);
        mClassifyAdapter = new ClassifyAdapter(getActivity());
    }


    private void listener() {
        mClassifyAdapter.setOnItemClickLitener(this);
    }

    private void initData() {
        mChoiceList = mData.getChoice();
        mHasChoosedChoiceList = (ArrayList<String>) mChoiceList.clone();
        mClassifyBasketList = mData.getClassifyBasket();
        Log.e("dyf", "----------->1" + mData.getAnswerList().toString());
        if (mData.getAnswerList().size() == 0) {
            for (int i = 0; i < mClassifyBasketList.size(); i++) {
                mData.getAnswerList().add(new ArrayList<String>());//初始化答案list,设置空数据占位，否则无法使用mData.getAnswerList().add(position, copyList);
            }
            mClassify_choice.setData(mChoiceList, this);
        } else { //恢复答案
            List<List<String>> answerList = mData.getAnswerList();
            if (answerList.size() > 0) {
                for (int j = 0; j < answerList.size(); j++) {
                    List<String> childList = answerList.get(j);
                    for (int k = 0; k < childList.size(); k++) {
                        int id = -1;
                        String chioce = null;
                        try {
                            id = Integer.parseInt(childList.get(k));//获取id
                            chioce = mChoiceList.get(id);//获取chioce的name
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mHasChoosedChoiceList.contains(chioce)) {
                            mHasChoosedChoiceList.set(id, null);
                        }
                    }
                }
                mClassify_choice.setData(mHasChoosedChoiceList, this);//刷新chioceview
            } else {
                mClassify_choice.setData(mChoiceList, this);
            }
        }
        Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mQuestionView), null);
        mQuestionView.setText(string);
        mClassifyAdapter.setData(mData);
        mClassify_recyclerview.setAdapter(mClassifyAdapter);
        if(mData.getClassifyBasket().size() <= 2){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mClassify_recyclerview.getLayoutParams();
            lp.width = WRAP_CONTENT;
            lp.addRule(CENTER_HORIZONTAL);
        }
    }

    /**
     * 归类题，归类篮子ItemClick的回调接口
     */
    @Override
    public void onItemClick(View view, int position) {
        if (hasChoiceData) { //选项内容被点击过了，选项添加进篮子
            hasChoiceData = false;
            //保存答案逻辑
            List<String> answerList = mData.getAnswerList().get(position);//现有的list，同新传过来的数据去重并合并
            for (int i = 0; i < tempList.size(); i++) {
                String id = (-1 != mChoiceList.indexOf(tempList.get(i))) ? mChoiceList.indexOf(tempList.get(i)) + "" : "-1";
                if (null != tempList.get(i) && !answerList.contains(id)) {
                    answerList.add(id);
                }
            }
            //选择后，移除choice，并刷新view
            for (int j = 0; j < answerList.size(); j++) { //choice被选中后，要在mHasChoosedChoiceList中被移除
                int id = -1;
                String chioce = null;
                try {
                    id = Integer.parseInt(answerList.get(j));//获取id
                    chioce = mChoiceList.get(id);//获取chioce的name
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mHasChoosedChoiceList.contains(chioce)) {
                    mHasChoosedChoiceList.set(id, null);
                }
                mHasChoosedChoiceListForNotifyDataChange.add(chioce);
            }
            mClassify_choice.refreshData(mHasChoosedChoiceListForNotifyDataChange);
//            mClassify_choice.setData(mHasChoosedChoiceList, this);//刷新chioceview
            mData.getAnswerList().set(position, answerList);//保存答案数据
            mClassifyAdapter.notifyDataSetChanged();//刷新篮子
            tempList.clear();//清空临时list
            mPosition = -1;
            answerComplete();
            saveAnswer(mData);
        } else { //展示篮子
            mPosition = position;
            List<String> answerList = mData.getAnswerList().get(position);//获取篮子里的数据
            ClassifyDrawerLayout classifyDrawerLayout = new ClassifyDrawerLayout(getActivity());
            classifyDrawerLayout.show(mClassifyBasketList.get(position), mChoiceList, answerList, ClassifyFragment.this);
        }
    }

    private ArrayList<String> tempList = new ArrayList<>();//临时存放选择的chioce
    private boolean hasChoiceData = false;//是否有选中数据--点击篮子时，是否有效
    private int mPosition = -1;

    /**
     * 归类题，选项内容点击回调
     *
     * @param view
     */
    @Override
    public void onClassifyChoiceItemClick(View view) {
        String name = view.getTag().toString();
        if (view.isSelected()) {
            view.setSelected(false); //取消选中
            if (tempList.contains(name))
                tempList.remove(name);
        } else {
            view.setSelected(true); //选中
            if (!tempList.contains(name))
                tempList.add(name);
        }
        if (!tempList.isEmpty()) {// 有选中数据
            hasChoiceData = true;
        } else {
            hasChoiceData = false;
        }
    }

    /**
     * 归类题，选项内容取消按钮点击回调
     *
     * @param view
     */
    @Override
    public void onClassifyChoiceItemCloseClick(View view) {
        ToastManager.showMsg(view.getTag().toString());
        int id = -1;
        String chioce = view.getTag().toString();
        try {
            id = mChoiceList.indexOf(chioce);//获取id
            chioce = view.getTag().toString();//获取chioce的name
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!mHasChoosedChoiceList.contains(chioce)) { //已选择的list里没有这个数据，那么应该把点击取消返回的这个数据，重新放进已选择list里
            mHasChoosedChoiceList.set(id, chioce);
        }
        mClassify_choice.setData(mHasChoosedChoiceList, this);//刷新chioceview
        //保存答案逻辑
        if (mPosition != -1) {
            List<String> answerList = mData.getAnswerList().get(mPosition);
            if (-1 != id && answerList.contains(id + "")) {
                answerList.remove(id + "");
            }
            mData.getAnswerList().set(mPosition, answerList);//保存答案数据
            mClassifyAdapter.notifyDataSetChanged();//刷新篮子
            answerComplete();
            saveAnswer(mData);
        }
    }

    /**
     * 获取已归类的待选内容的数量
     * @return
     */
    private int getAnswerListCount() {
        int totalSize = 0;
        List<List<String>> answerList = mData.getAnswerList();
        if (answerList == null || answerList.isEmpty()) {
            return totalSize;
        }
        for (int i = 0; i < answerList.size(); i++) {
            List<String> childList = answerList.get(i);
            for (int j = 0; j < childList.size(); j++) {
                if(!TextUtils.isEmpty(childList.get(j))){
                    totalSize++;
                }
            }
        }
        return totalSize;
    }

    /**
     * 判断是否回答完毕答案
     * @return
     */
    private void answerComplete() {
        int hasAnswerCount = getAnswerListCount();//已选答案数量
        int totalCount = mChoiceList.size();//所有待选项目内容数量
        if(totalCount == hasAnswerCount){ //都回答完毕
            mData.setIsAnswer(true);
        }else{
            mData.setIsAnswer(false);
        }
        updateProgress();
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