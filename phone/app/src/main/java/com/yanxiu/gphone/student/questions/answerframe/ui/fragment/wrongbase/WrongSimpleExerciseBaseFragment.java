package com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ListenerSeekBarLayout;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisAnsewrAnslysisView;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisDifficultyView;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisQuestionResultView;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisQuestionSpokenResultView;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisScoreView;
import com.yanxiu.gphone.student.customviews.analysis.AnswerLayoutView;
import com.yanxiu.gphone.student.customviews.analysis.NotesLayoutView;
import com.yanxiu.gphone.student.customviews.analysis.PointLayoutView;
import com.yanxiu.gphone.student.customviews.analysis.VoiceScoldedLayoutView;
import com.yanxiu.gphone.student.mistakeredo.MistakeRedoActivity;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.HomeEventMessage;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.NotesActicity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.questions.bean.JsonAudioComment;
import com.yanxiu.gphone.student.questions.bean.JsonNoteBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/18 15:28.
 * Function :
 */
public abstract class WrongSimpleExerciseBaseFragment extends WrongExercisbaseFragment implements View.OnClickListener {

    private BaseQuestion mData;

    public View mRootView, fl_analysis;
    public LinearLayout mAnsewr_container, mAnalysis_container;
    private AnalysisQuestionSpokenResultView mAnswerSpokenResultView;
    public AnalysisQuestionResultView mAnswerResultView;//答题结果view
    public ImageView mYesno_img;//答题结果对应的对错img，一定要和mAnswerResultView成对出现或隐藏
    public AnalysisScoreView mScoreView;//评分view
    public AnalysisDifficultyView mDifficultyview;//难度view
    private AnswerLayoutView mAnswerView;//答案
    public AnalysisAnsewrAnslysisView mAnalysisview;//解析view
    private PointLayoutView mPointView;//知识的view
    private VoiceScoldedLayoutView mVoiceScoldedView;//语音批注
    private NotesLayoutView mNoteView;//笔记
    private boolean isHideBottomView=false;
    private View mBottomView;
    protected boolean mToVoiceIsIntent = false;
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
        mRootView = inflater.inflate(R.layout.fragment_wrong_base, container, false);
        initView(inflater, container);
        return mRootView;
    }

    private void initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mAnsewr_container = (LinearLayout) mRootView.findViewById(R.id.ansewr_container);
        fl_analysis = mRootView.findViewById(R.id.fl_analysis);
        mAnalysis_container = (LinearLayout) mRootView.findViewById(R.id.analysis_container);
        mAnswerSpokenResultView= (AnalysisQuestionSpokenResultView) mRootView.findViewById(R.id.answerSpokenResult);

        mAnswerResultView = (AnalysisQuestionResultView) mRootView.findViewById(R.id.answerResult);
        mYesno_img = (ImageView) mRootView.findViewById(R.id.yesno_img);
        mScoreView = (AnalysisScoreView) mRootView.findViewById(R.id.scoreview);
        mDifficultyview = (AnalysisDifficultyView) mRootView.findViewById(R.id.difficultyview);
        mAnalysisview = (AnalysisAnsewrAnslysisView) mRootView.findViewById(R.id.analysisview);

        mPointView = (PointLayoutView) mRootView.findViewById(R.id.pointview);
        mAnswerView = (AnswerLayoutView) mRootView.findViewById(R.id.answerview);
        mVoiceScoldedView = (VoiceScoldedLayoutView) mRootView.findViewById(R.id.voicescoldedview);
        mNoteView = (NotesLayoutView) mRootView.findViewById(R.id.noteview);

        mBottomView=mRootView.findViewById(R.id.v_bottom);
        if (isHideBottomView){
            mBottomView.setVisibility(View.GONE);
        }else {
            mBottomView.setVisibility(View.VISIBLE);
        }

        View answerView = addAnswerView(inflater, container);
        mAnsewr_container.addView(answerView);
        initAnswerView(inflater, container);
        initAnalysisView();
        setQaNumber(mAnsewr_container);
        setQaName(mAnsewr_container);
        initComplexStem(mAnsewr_container, mData);
        hiddenBottomPaddinglayout(mAnsewr_container);
    }

    public void setAnalysisVisible(int visible) {
        if (fl_analysis != null)
            fl_analysis.setVisibility(visible);
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

    /**
     * 隐藏单题型底部设置的paddinglayout
     */
    private void hiddenBottomPaddinglayout(View view) {
        try {
            View bottomPaddingLayout = view.findViewById(R.id.bottompadding_layout);
            bottomPaddingLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void hideBottomView() {
        super.hideBottomView();
        isHideBottomView=true;
        if (mBottomView!=null) {
            mBottomView.setVisibility(View.GONE);
        }
    }

    /**
     * 添加答题view
     *
     * @param inflater
     * @param container
     * @return
     */
    public abstract View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 初始化答题view
     *
     * @param inflater
     * @param container
     */
    public abstract void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 显示解析view
     */
    public abstract void initAnalysisView();

    /**
     * 答题结果view
     *
     * @param isRight 是否正确
     * @param result  server返回的（没有该数据请传空）
     * @param textContent  给某些题型使用，传入“正确”或者“错误”；不需要请传入null
     */
    public void showAnswerResultView(boolean isRight, String result,String textContent) {
        showAnswerResultView(isRight, result, textContent,false);
    }

    /**
     * 答题结果view
     *
     * @param isRight     是否正确
     * @param result      server返回的（没有该数据请传空）
     * @param textContent 给某些题型使用，传入“正确”或者“错误”；不需要请传入null
     * @param isHalfRight 是否是半对，暂时填空题需要
     */
    public void showAnswerResultView(boolean isRight, String result, String textContent,boolean isHalfRight) {
        if(getActivity() instanceof MistakeRedoActivity){
            //错题重做里面的作答结果，展示不一样（应该展示拼起来的正确答案）
            //错题解析里面，主观题的result传的是null，但是主观题不显示作答结果，所以不影响
            result = mData.getMistakeRedoAnswerResult();
        }
        if (mAnswerResultView != null && mYesno_img != null) {
            int status = mData.getPad().getStatus();
            if (QuestionTemplate.ANSWER.equals(mData.getTemplate())) { //主观题
                if (status == 5) { //已批改
                    if (isRight) {
                        if (TextUtils.isEmpty(textContent)) {
                            mAnswerResultView.setText(getResources().getString(R.string.answer_yes), result);
                        } else {
                            mAnswerResultView.setText(textContent, result);
                        }
                        mYesno_img.setBackgroundResource(R.drawable.analysis_yes_img);

                    } else {
                        if (TextUtils.isEmpty(textContent)) {
                            mAnswerResultView.setText(getResources().getString(R.string.answer_no), result);
                        } else {
                            mAnswerResultView.setText(textContent, result);
                        }
                        mYesno_img.setBackgroundResource(R.drawable.analysis_wrong_img);
                    }
                    mYesno_img.setVisibility(View.VISIBLE);
                } else {
                    mAnswerResultView.setText(getResources().getString(R.string.answer_no_pigai), result);
                }
            } else {
                if (isHalfRight){
                    if (TextUtils.isEmpty(textContent)) {
                        mAnswerResultView.setText(getResources().getString(R.string.answer_half_right), result);
                    } else {
                        mAnswerResultView.setText(textContent, result);
                    }
                    mYesno_img.setBackgroundResource(R.drawable.analysis_half_right_img);
                }else {
                    if (isRight) {
                        if (TextUtils.isEmpty(textContent)) {
                            mAnswerResultView.setText(getResources().getString(R.string.answer_yes), result);
                        } else {
                            mAnswerResultView.setText(textContent, result);
                        }
                        mYesno_img.setBackgroundResource(R.drawable.analysis_yes_img);

                    } else {
                        if (TextUtils.isEmpty(textContent)) {
                            mAnswerResultView.setText(getResources().getString(R.string.answer_no), result);
                        } else {
                            mAnswerResultView.setText(textContent, result);
                        }
                        mYesno_img.setBackgroundResource(R.drawable.analysis_wrong_img);
                    }
                }
                mYesno_img.setVisibility(View.VISIBLE);

            }
            mAnswerResultView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 评分view
     *
     * @param score 评分
     */
    public void showScoreView(String score) {
        if (mScoreView != null && !TextUtils.isEmpty(score)) {
            mScoreView.setScore(score);
            mScoreView.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 难度view
     *
     * @param score 评分
     */
    public void showDifficultyview(int score) {
        if (mDifficultyview != null && score >= 0 && score <= 5) {
            mDifficultyview.setScore(score);
            mDifficultyview.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 解析view
     *
     * @param analysis 解析
     */
    public void showAnalysisview(String analysis) {
        if (mAnalysisview != null && !TextUtils.isEmpty(analysis)) {
            mAnalysisview.setText(analysis);
            mAnalysisview.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 答案
     */
    public void showAnswerView(String strem) {
        if (TextUtils.isEmpty(strem)) {
            return;
        }
        mAnswerView.setText(strem);
        mAnswerView.setVisibility(View.VISIBLE);
    }

    /**
     * 仅限于口语作答结果
     * */
    public void showAnswerSpokenResultView(int score){
        mAnswerSpokenResultView.setVisibility(View.VISIBLE);
        mAnswerSpokenResultView.setData(score);
        mYesno_img.setVisibility(View.VISIBLE);
        switch (score){
            case 0:
            case 1:
                mYesno_img.setBackgroundResource(R.drawable.analysis_wrong_img);
                break;
            case 2:
            case 3:
                mYesno_img.setBackgroundResource(R.drawable.analysis_yes_img);
                break;
            default:
                mYesno_img.setBackgroundResource(R.drawable.analysis_wrong_img);
                break;
        }

    }

    /**
     * 知识点
     */
    public void showPointView(List<PointBean> data) {
        if (data == null) {
            return;
        }
        for (PointBean pointBean : data) {
            mPointView.setData(pointBean.getName());
        }
        mPointView.setVisibility(View.VISIBLE);
    }

    /**
     * 语音批语
     */
    public void showVoiceScoldedView(List<JsonAudioComment> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mVoiceScoldedView.setData(list);
        mVoiceScoldedView.setVisibility(View.VISIBLE);
    }

    /**
     * 笔记
     */
    public void showNoteView(JsonNoteBean noteBean) {
        mNoteView.setData(noteBean);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.edit:
//                NotesActicity.invoke(getActivity(), edit.hashCode());
//                break;
        }
    }

    public void onEventMainThread(HomeEventMessage message) {
        mToVoiceIsIntent = false;
        setVoicePause();
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
        super.onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
        if (isVisibleToUser) {
            mToVoiceIsIntent = false;
            if (null != mListenView) {
                mListenView.setResume();
                if (mData.mIsShouldPlay && !mData.mIsPause) {
                    mListenView.setPlayToProgress(mData.mProgress, mData.mMax);
                    mData.mIsShouldPlay = false;
                } else if (mData.mIsShouldPlay && mData.mIsPause) {
                    mListenView.setPauseToProgress(mData.mProgress, mData.mMax);
                    mData.mIsShouldPlay = false;
                }
            }
        } else {
            if (null != mListenView)
                mListenView.setPause();
            setVoicePause();
        }
    }

    private void setVoicePause() {
        if (mVoiceScoldedView != null && !mToVoiceIsIntent) {
            mVoiceScoldedView.setStop();
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
        if (null != mListenView) {
            mData.mProgress = mListenView.getProgress();
            mData.mMax = mListenView.getMax();
            mData.mIsShouldPlay = mListenView.getIsPlaying();
            mData.mIsPause = mListenView.getIsPause();
            mListenView.setDestory();
        }
        if (mVoiceScoldedView != null) {
            mVoiceScoldedView.setDestory();
        }
    }

}
