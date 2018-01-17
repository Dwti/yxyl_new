package com.yanxiu.gphone.student.questions.dialogSpoken;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.SpokenSpanTextView;
import com.yanxiu.gphone.student.customviews.SpokenWaveView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.spoken.AudioTagHandler;
import com.yanxiu.gphone.student.questions.spoken.ClickableImageSpan;
import com.yanxiu.gphone.student.questions.spoken.SpokenLinkMovementMethod;
import com.yanxiu.gphone.student.questions.spoken.SpokenQuestion;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/15 14:49.
 * Function :
 */
public class DialogSpokenFragment extends AnswerSimpleExerciseBaseFragment implements View.OnClickListener, ClickableImageSpan.onSpanClickListener {

    private DialogSpokenQuestion mData;

    private View rootView;
    private SpokenSpanTextView mQuestionView;
    private ImageView mRecordView;
    private ImageView mPlayOrStopView;
    private ImageView mRecordAnimView;
    private SpokenWaveView mSpokenWaveView;
    private AudioTagHandler mAudioTagHandler;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mData = (DialogSpokenQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SpokenQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dialogspoken,container,false);
        setQaNumber(view);
        setQaName(view);
        initComplexStem(view, mData);
        initView(view);
        listener();
        initData();
        return view;
    }

    private void initView(View view) {
        this.rootView = view;
        mQuestionView = (SpokenSpanTextView) view.findViewById(R.id.tv_question);
        mRecordView = (ImageView) view.findViewById(R.id.iv_record);
        mPlayOrStopView = (ImageView) view.findViewById(R.id.iv_play_stop);
        mSpokenWaveView = (SpokenWaveView) view.findViewById(R.id.sw_wave);
        mRecordAnimView = (ImageView) view.findViewById(R.id.iv_record_anim);
    }

    private void listener() {
        mPlayOrStopView.setOnClickListener(DialogSpokenFragment.this);
    }

    private void initData() {
        mAudioTagHandler = new AudioTagHandler(getContext(), mQuestionView, DialogSpokenFragment.this);
        Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mQuestionView), mAudioTagHandler);
        mQuestionView.setData(string);
        mQuestionView.setHighlightColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        mQuestionView.setMovementMethod(SpokenLinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play_stop:
                break;
        }
    }

    @Override
    public void onClick(String url) {

    }
}
