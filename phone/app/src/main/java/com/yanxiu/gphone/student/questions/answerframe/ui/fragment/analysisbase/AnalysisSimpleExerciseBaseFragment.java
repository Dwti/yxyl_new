package com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ListenerSeekBarLayout;
import com.yanxiu.gphone.student.customviews.analysis.AnswerLayoutView;
import com.yanxiu.gphone.student.customviews.analysis.PointLayoutView;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisDifficultyView;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisQuestionResultView;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisScoreView;
import com.yanxiu.gphone.student.customviews.analysis.VoiceScoldedLayoutView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.NotesActicity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.questions.bean.PointBean;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by 戴延枫 on 2017/6/8.
 * 单题型解析Frament基类
 */

public abstract class AnalysisSimpleExerciseBaseFragment extends AnalysisExerciseBaseFragment implements View.OnClickListener {
    private BaseQuestion mData;
    private TextView mQuestionView;

    public View mRootView;
    public LinearLayout mAnsewr_container, mAnalysis_container;
//    public TextView  edit, mNotesTextView;
    public AnalysisQuestionResultView mAnswerResultView;//答题结果view
    public ImageView mYeno_img;//答题结果对应的对错img，一定要和mAnswerResultView成对出现或隐藏
    public AnalysisScoreView mScoreView;//评分view
    public AnalysisDifficultyView mDifficultyview;//难度view
    private AnswerLayoutView mAnswerView;//答案
    private PointLayoutView mPointView;//知识的view
    private VoiceScoldedLayoutView mVoiceScoldedView;//语音批注

    private ListenerSeekBarLayout mListenView;//听力复合题只有一个子题时，题干的听力控件

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        mData = data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("dyf", "onCreateView:ppp");
        mRootView = inflater.inflate(R.layout.fragment_analysis_base, container, false);
        initView(inflater,container);
        return mRootView;
    }

    private void initView(LayoutInflater inflater,@Nullable ViewGroup container) {
        mAnsewr_container = (LinearLayout) mRootView.findViewById(R.id.ansewr_container);
        mAnalysis_container = (LinearLayout) mRootView.findViewById(R.id.analysis_container);

        mAnswerResultView = (AnalysisQuestionResultView) mRootView.findViewById(R.id.answerResult);
        mYeno_img = (ImageView) mRootView.findViewById(R.id.yesno_img);
        mScoreView = (AnalysisScoreView) mRootView.findViewById(R.id.scoreview);
        mDifficultyview = (AnalysisDifficultyView) mRootView.findViewById(R.id.difficultyview);
        mAnswerView= (AnswerLayoutView) mRootView.findViewById(R.id.answerview);
        mPointView= (PointLayoutView) mRootView.findViewById(R.id.pointview);
        mVoiceScoldedView= (VoiceScoldedLayoutView) mRootView.findViewById(R.id.voicescoldedview);

//        edit = (TextView) mRootView.findViewById(R.id.edit);
//        mNotesTextView = (TextView) mRootView.findViewById(R.id.notesContent);
        View answerView = addAnswerView(inflater,container);
        mAnsewr_container.addView(answerView);
        initAnswerView(inflater,container);
        initAnalysisView();
//        initListener();
        setQaNumber(mAnsewr_container);
        setQaName(mAnsewr_container);
        initComplexStem(mAnsewr_container,mData);
    }

    /**
     * 如果是只有一个子题的复合题，显示大题的题干
     * (所有单题型都需要支持该方法)
     *
     * @param view
     */
    public void initComplexStem(View view, BaseQuestion data) {
        //通用题干
        LinearLayout complex_stem_layout = (LinearLayout) view.findViewById(R.id.complex_stem_layout);
        TextView complex_stem = (TextView) view.findViewById(R.id.complex_stem);
        String complexStem = data.getStem_complexToSimple();
        if (!TextUtils.isEmpty(complexStem)) {
            Spanned spanned = Html.fromHtml(complexStem, new HtmlImageGetter(complex_stem), null);
            complex_stem.setText(spanned);
            complex_stem_layout.setVisibility(View.VISIBLE);
        }
        //听力题干的听力控件
        String template = data.getTemplate_complexToSimple();
        String url = data.getUrl_listenComplexToSimple();//听力url
        if (!TextUtils.isEmpty(template) && template.equals(QuestionTemplate.LISTEN)) {
            mListenView = (ListenerSeekBarLayout) view.findViewById(R.id.complex_stem_listen);
            mListenView.setVisibility(View.VISIBLE);
            mListenView.setUrl(url);
            mListenView.setVisibility(View.VISIBLE);
        }
    }


    private void initData() {

    }

//    private void initListener() {
//        edit.setOnClickListener(this);
//    }

    /**
     * 添加答题view
     * @param inflater
     * @param container
     * @return
     */
    public abstract View addAnswerView(LayoutInflater inflater,@Nullable ViewGroup container);

    /**
     * 初始化答题view
     * @param inflater
     * @param container
     */
    public abstract void initAnswerView(LayoutInflater inflater,@Nullable ViewGroup container);

    /**
     * 显示解析view
     */
    public abstract void initAnalysisView();

    /**
     * 答题结果view
     * @param isRight 是否正确
     * @param result server返回的（没有该数据请传空）
     */
    public void showAnswerResultView(boolean isRight,String result) {
        if(mAnswerResultView != null && mYeno_img != null){
            if(isRight){
                mAnswerResultView.setText(getResources().getString(R.string.answer_yes),result);
                mYeno_img.setBackgroundResource(R.drawable.analysis_yes_img);
            }else{
                mAnswerResultView.setText(getResources().getString(R.string.answer_no),result);
                mYeno_img.setBackgroundResource(R.drawable.analysis_wrong_img);
            }
            mAnswerResultView.setVisibility(View.VISIBLE);
            mYeno_img.setVisibility(View.VISIBLE);

        }
    }
    /**
     * 评分view
     * @param score 评分
     */
    public void showScoreView(String score) {
        if(mScoreView != null){
            mScoreView.setText(score);
            mScoreView.setVisibility(View.VISIBLE);

        }
    }
    /**
     * 难度view
     * @param score 评分
     */
    public void showmDifficultyview(int score) {
        if(mDifficultyview != null && score >= 0 && score <= 5){
            mDifficultyview.setScore(score);
            mDifficultyview.setVisibility(View.VISIBLE);

        }
    }

    public void showAnswerView(String strem){
        if (strem==null){
            return;
        }
        mAnswerView.setText(strem);
        mAnswerView.setVisibility(View.VISIBLE);
    }

    public void showPointView(List<PointBean> data){
        if (data==null){
            return;
        }
        for (PointBean pointBean:data) {
            mPointView.setData(pointBean.getName());
        }
        mPointView.setVisibility(View.VISIBLE);
    }

    public void showVoiceScoldedView(List<VoiceScoldedLayoutView.ScoldedMessage> list){
        mVoiceScoldedView.setData(list);
        mVoiceScoldedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.edit:
//                NotesActicity.invoke(getActivity(), edit.hashCode());
//                break;
        }
    }

    public void onEventMainThread(NotesActicity.NotesMessage notesMessage) {
//        int viewHashCode = notesMessage.mViewHashCode;
//        String notesContent = notesMessage.mNotesContent;
//        if (viewHashCode == edit.hashCode())
//            mNotesTextView.setText(notesContent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser,invokeInResumeOrPause);
        if (isVisibleToUser) {
            if (null != mListenView)
                mListenView.setResume();
        } else {
            if (null != mListenView)
                mListenView.setPause();
        }
        if (!invokeInResumeOrPause){
            if (isVisibleToUser){
                if (mVoiceScoldedView!=null) {
                    mVoiceScoldedView.setResume();
                }
            }else {
                if (mVoiceScoldedView!=null) {
                    mVoiceScoldedView.setPause();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mListenView)
            mListenView.setDestory();
        if (mVoiceScoldedView!=null){
            mVoiceScoldedView.setDestory();
        }
    }



}
