package com.yanxiu.gphone.student.questions.choose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.ChooseLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.cloze.ClozeAnswerComplexFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class MultiChooseAnalysisFragment extends AnalysisSimpleExerciseBaseFragment {

    private MultiChoiceQuestion mData;
    private View mAnswerView;
    private TextView mQuestionView;
    private ChooseLayout mChooseView;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        this.mData= (MultiChoiceQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((MultiChoiceQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mAnswerView=inflater.inflate(R.layout.fragment_choose,container,false);
        return mAnswerView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        initView();
        initData();
    }

    private void initView(){
        mQuestionView= (TextView) mAnswerView.findViewById(R.id.tv_question);
        mChooseView= (ChooseLayout) mAnswerView.findViewById(R.id.cl_answer);
    }

    private void initData(){
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),null);
        mQuestionView.setText(string);
        mChooseView.setIsClick(false);
        mChooseView.setChooseType(ChooseLayout.TYPE_MULTI);
        mChooseView.setData(mData.getChoice());
        List<String> datas = mData.getAnswerList();
        List<String> answers=mData.getMultianswer();
        int count=mChooseView.getChildCount();
        for (int i = 0; i < answers.size(); i++) {
            int answerPosition=Integer.parseInt(answers.get(i));
            if (count>answerPosition){
                ChooseLayout.ViewHolder answerViewHolder= (ChooseLayout.ViewHolder) mChooseView.getChildAt(answerPosition).getTag();
                answerViewHolder.mQuestionIdView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ffffff));
                answerViewHolder.mQuestionIdView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.choose_right));
                answerViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_89e00d));
            }
        }

        for (int i=0;i<datas.size();i++){
            int selectPosition=Integer.parseInt(datas.get(i));
            if (count>selectPosition){
                if (answers.contains(datas.get(i))){
                    mChooseView.setSelect(selectPosition);
                }else {
                    ChooseLayout.ViewHolder selectViewHolder= (ChooseLayout.ViewHolder) mChooseView.getChildAt(selectPosition).getTag();
                    selectViewHolder.mQuestionIdView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ffffff));
                    selectViewHolder.mQuestionIdView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.choose_wrong));
                    selectViewHolder.mQuestionContentView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ff7a05));
                    selectViewHolder.mQuestionSelectView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_launcher));
                }
            }
        }
    }

    @Override
    public void initAnalysisView() {
        if (mData.getStatus()== Constants.ANSWER_STATUS_RIGHT){
            showAnswerResultView(true, mData.getAnswerCompare(), null);
        }else {
            showAnswerResultView(false, mData.getAnswerCompare(), null);
        }

//        showAnswerView("你的打哪是阿达，啊啥京东卡啥京东卡");
//        List<VoiceScoldedLayoutView.ScoldedMessage> list=new ArrayList<>();
//        for (int i=0;i<5;i++){
//            VoiceScoldedLayoutView.ScoldedMessage message= VoiceScoldedLayoutView.ScoldedMessage.obtain();
//            message.time=2+i*10;
//            message.url="http://data.5sing.kgimg.com/G034/M05/16/17/ApQEAFXsgeqIXl7gAAVVd-n31lcAABOogKzlD4ABVWP363.mp3";
//            list.add(message);
//        }
//        showVoiceScoldedView(list);
        showDifficultyview(mData.getStarCount());
        showAnalysisview(mData.getQuestionAnalysis());
        showPointView(mData.getPointList());
    }
}