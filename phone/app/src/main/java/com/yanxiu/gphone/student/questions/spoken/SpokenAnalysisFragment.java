package com.yanxiu.gphone.student.questions.spoken;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.MediaPlayerUtil;
import com.yanxiu.gphone.student.util.ToastManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/16 9:47.
 * Function :
 */
public class SpokenAnalysisFragment extends AnalysisSimpleExerciseBaseFragment implements ClickableImageSpan.onSpanClickListener {

    private SpokenQuestion mData;
    private View mAnswerView;
    private TextView mQuestionView;
    private AudioTagHandler mAudioTagHandler;

    private MediaPlayerUtil mPlayerUtil=MediaPlayerUtil.create();

    @Override
    public void setData(BaseQuestion data) {
        super.setData(data);
        mData= (SpokenQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SubjectiveQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
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
        mQuestionView= (TextView) mAnswerView.findViewById(R.id.tv_question);
    }

    private void listener(){

    }

    private void initData(){
        mAudioTagHandler=new AudioTagHandler(getContext(),mQuestionView,SpokenAnalysisFragment.this);
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),mAudioTagHandler);
        mQuestionView.setText(string);
        mQuestionView.setHighlightColor(ContextCompat.getColor(getContext(),android.R.color.transparent));
        mQuestionView.setMovementMethod(SpokenLinkMovementMethod.getInstance());

        setScore(0);
    }

    private void setScore(int num){
        switch (num){
            case 0:
                mAnswerView.findViewById(R.id.ll_hand).setVisibility(View.GONE);
                break;
            case 1:
                mAnswerView.findViewById(R.id.ll_hand).setVisibility(View.VISIBLE);
                mAnswerView.findViewById(R.id.iv_hand1).setVisibility(View.VISIBLE);
                mAnswerView.findViewById(R.id.iv_hand2).setVisibility(View.GONE);
                mAnswerView.findViewById(R.id.iv_hand3).setVisibility(View.GONE);
                break;
            case 2:
                mAnswerView.findViewById(R.id.ll_hand).setVisibility(View.VISIBLE);
                mAnswerView.findViewById(R.id.iv_hand1).setVisibility(View.VISIBLE);
                mAnswerView.findViewById(R.id.iv_hand2).setVisibility(View.VISIBLE);
                mAnswerView.findViewById(R.id.iv_hand3).setVisibility(View.GONE);
                break;
            case 3:
                mAnswerView.findViewById(R.id.ll_hand).setVisibility(View.VISIBLE);
                mAnswerView.findViewById(R.id.iv_hand1).setVisibility(View.VISIBLE);
                mAnswerView.findViewById(R.id.iv_hand2).setVisibility(View.VISIBLE);
                mAnswerView.findViewById(R.id.iv_hand3).setVisibility(View.VISIBLE);
                break;
            default:
                mAnswerView.findViewById(R.id.ll_hand).setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void initAnalysisView() {
//        showAnswerResultView();
        showDifficultyview(mData.getStarCount());
        showAnalysisview(mData.getQuestionAnalysis());
        showPointView(mData.getPointList());
    }

    @Override
    public void onClick(String url) {
        mPlayerUtil.addMediaPlayerCallBack(new MediaPlayerUtil.MediaPlayerCallBack() {
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
        });
        mPlayerUtil.start(url);
//        mPlayerUtil.start("http://data.5sing.kgimg.com/G034/M05/16/17/ApQEAFXsgeqIXl7gAAVVd-n31lcAABOogKzlD4ABVWP363.mp3");
    }
}
