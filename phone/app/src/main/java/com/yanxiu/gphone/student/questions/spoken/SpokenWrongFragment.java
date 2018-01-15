package com.yanxiu.gphone.student.questions.spoken;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.SpokenSpanTextView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.MediaPlayerUtil;
import com.yanxiu.gphone.student.util.ToastManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/16 9:49.
 * Function :
 */
public class SpokenWrongFragment extends WrongSimpleExerciseBaseFragment implements ClickableImageSpan.onSpanClickListener, MediaPlayerUtil.MediaPlayerCallBack {

    private SpokenQuestion mData;
    private View mAnswerView;
    private SpokenSpanTextView mQuestionView;
    private AudioTagHandler mAudioTagHandler;

    private MediaPlayerUtil mPlayerUtil;

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        mData= (SpokenQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SpokenQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
        mPlayerUtil=MediaPlayerUtil.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayerUtil.destory();
    }

    @Override
    public View addAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mAnswerView=inflater.inflate(R.layout.fragment_spokenanalysis,container,false);
        return mAnswerView;
    }

    @Override
    public void initAnswerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        initView();
        listener();
        initData();
    }

    private void initView(){
        mQuestionView= (SpokenSpanTextView) mAnswerView.findViewById(R.id.tv_question);
    }

    private void listener(){
        mPlayerUtil.addMediaPlayerCallBack(this);
    }

    private void initData(){
        mAudioTagHandler=new AudioTagHandler(getContext(),mQuestionView,SpokenWrongFragment.this);
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),mAudioTagHandler);
        mQuestionView.setText(string);
        mQuestionView.setHighlightColor(ContextCompat.getColor(getContext(),android.R.color.transparent));
        mQuestionView.setMovementMethod(SpokenLinkMovementMethod.getInstance());
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
        if (!isVisibleToUser){
            mPlayerUtil.playFinish();
            mAudioTagHandler.stop();
        }
    }

    @Override
    public void onAnswerCardVisibleToUser(boolean isVisibleToUser) {
        super.onAnswerCardVisibleToUser(isVisibleToUser);
        if (isVisibleToUser){
            mPlayerUtil.playFinish();
            mAudioTagHandler.stop();
        }
    }

    @Override
    public void initAnalysisView() {
        if (mData.getAnswerList()!=null&&!mData.getAnswerList().isEmpty()) {
            SpokenResponse response = SpokenQuestion.getBeanFromJson(mData.getAnswerList().get(0));
            showAnswerSpokenResultView(SpokenQuestion.getScore((int) response.lines.get(0).score));
        }else {
            showAnswerSpokenResultView(-1);
        }
        //29  口语作文
        if ("29".equals(mData.getType_id())){
            showAnswerView(mData.getAnswerResult());
        }
        showDifficultyview(mData.getStarCount());
        showAnalysisview(mData.getQuestionAnalysis());
        showPointView(mData.getPointList());
        showNoteView(mData.getJsonNoteBean());
    }

    @Override
    public void onClick(String url) {
        mPlayerUtil.start(url);
//        mPlayerUtil.start("http://data.5sing.kgimg.com/G034/M05/16/17/ApQEAFXsgeqIXl7gAAVVd-n31lcAABOogKzlD4ABVWP363.mp3");
    }

    @Override
    public void onStart(MediaPlayerUtil mpu, int duration) {
        mAudioTagHandler.start();
    }

    @Override
    public void onProgress(MediaPlayerUtil mu, int progress) {

    }

    @Override
    public void onCompletion(MediaPlayerUtil mu) {
        mAudioTagHandler.stop();
    }

    @Override
    public void onError(MediaPlayerUtil mu) {
        mAudioTagHandler.stop();
        ToastManager.showMsg(R.string.net_null);
    }
}
