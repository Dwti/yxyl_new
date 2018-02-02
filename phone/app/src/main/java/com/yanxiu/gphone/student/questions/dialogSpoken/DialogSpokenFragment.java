package com.yanxiu.gphone.student.questions.dialogSpoken;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.SpokenWaveView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.spoken.SpokenErrorDialog;
import com.yanxiu.gphone.student.questions.spoken.SpokenPermissionDialog;
import com.yanxiu.gphone.student.questions.spoken.SpokenQuestion;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/15 14:49.
 * Function :
 */
public class DialogSpokenFragment extends AnswerSimpleExerciseBaseFragment implements View.OnClickListener, DialogSpokenLinearLayout.DialogSpokenStatusChangeCallback, DialogSpokenResultDialog.onButtonClick {

    private DialogSpokenQuestion mData;

    private View rootView;
    private TextView mQuestionView;
    private ImageView mRecordView;
    private ImageView mPlayOrStopView;
    private ImageView mRecordAnimView;
    private SpokenWaveView mSpokenWaveView;
    private DialogSpokenLinearLayout mDialogSpokenLinearLayout;
    private RelativeLayout mRecordLayout;

    private DialogSpokenResultDialog mResultDialog;
    private SpokenErrorDialog mErrorDialog;
    private String mUrl;

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogspoken, container, false);
        mResultDialog=DialogSpokenResultDialog.create(inflater.getContext());
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
        mQuestionView = (TextView) view.findViewById(R.id.tv_question);
        mDialogSpokenLinearLayout = (DialogSpokenLinearLayout) view.findViewById(R.id.dsl_spoken);
        mRecordView = (ImageView) view.findViewById(R.id.iv_record);
        mRecordLayout= (RelativeLayout) view.findViewById(R.id.rl_record);
        mPlayOrStopView = (ImageView) view.findViewById(R.id.iv_play_stop);
        mSpokenWaveView = (SpokenWaveView) view.findViewById(R.id.sw_wave);
        mRecordAnimView = (ImageView) view.findViewById(R.id.iv_record_anim);
    }

    private void listener() {
        mPlayOrStopView.setOnClickListener(DialogSpokenFragment.this);
        mDialogSpokenLinearLayout.setStatusChangeCallback(this);
        mRecordView.setOnClickListener(this);
        mResultDialog.setDisMissCallback(this);
        mRecordLayout.setOnTouchListener(mTouchListener);
    }

    private void initData() {

        String stem = mData.getStem();

        String regex = "\\[\\[.*?\\]\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stem);
        stem = matcher.replaceAll("_____________");

        String regex2 = "<start>.*?</start>";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(stem);

        List<String> items = new ArrayList<>();
        while (matcher2.find()) {
            items.add(matcher2.group());
        }

        for (String s : items) {
            stem = stem.replaceAll(s, "");
        }
        if (TextUtils.isEmpty(stem)) {
            mQuestionView.setVisibility(View.GONE);
        } else {
            mQuestionView.setVisibility(View.VISIBLE);
            mQuestionView.setText(stem);
        }
        mDialogSpokenLinearLayout.setData(items);
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
    }

    @Override
    public void onAnswerCardVisibleToUser(boolean isVisibleToUser) {
        super.onAnswerCardVisibleToUser(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDialogSpokenLinearLayout.onDestory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_stop:
                break;
            case R.id.iv_record:
                mRecordView.setEnabled(false);
                mRecordView.setClickable(false);
                mDialogSpokenLinearLayout.play(0);
                break;
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDialogSpokenLinearLayout.record(mData.getSpokenAnswer());
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    mDialogSpokenLinearLayout.stopRecord();
                    break;
            }
            return true;
        }
    };

    @Override
    public void onDialogSpokenStart() {
        mRecordView.setEnabled(true);
        mRecordView.setClickable(true);
    }

    @Override
    public void onDialogSpokenEnd() {
        mRecordView.setEnabled(true);
        mRecordView.setClickable(true);
    }

    @Override
    public void onButtonAble() {
        mRecordView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.spoken_record_press));
    }

    @Override
    public void onButtonUnable() {
        mRecordView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.spoken_record_unable));
    }

    @Override
    public void onMediaPlayStart() {

    }

    @Override
    public void onMediaPlaying() {

    }

    @Override
    public void onMediaPlayEnd() {

    }

    @Override
    public void onRecordStart() {
        mSpokenWaveView.start();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_spoken_record);
        mRecordAnimView.startAnimation(animation);
        mSpokenWaveView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRecording(int volume) {
        mSpokenWaveView.setVolume(volume);
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onFinished() {
        mDialogSpokenLinearLayout.stopRecord();
        ToastManager.showMsg("录音不能超过3分钟哦");
    }

    @Override
    public void onRecorEnd() {

    }

    @Override
    public void onStartOralEval() {
        closeAnim();
    }

    @Override
    public void onStopOralEval(boolean isError) {
        if (!isError) {
            mPlayOrStopView.setVisibility(View.VISIBLE);
            //TODO 重播录音
        } else {
            closeAnim();
        }
    }

    @Override
    public void onResult(String result) {
//        mResultDialog.setScore();
        mResultDialog.show();
    }

    @Override
    public void onResultUrl(String filePath, String url) {
        this.mUrl = url;
    }

    @Override
    public void onNoPermission() {
        mSpokenWaveView.setVisibility(View.GONE);
        SpokenPermissionDialog permissionDialog = SpokenPermissionDialog.creat(getContext());
        permissionDialog.show();
    }

    @Override
    public void onError(String result) {
        mSpokenWaveView.setVisibility(View.GONE);
        if (mErrorDialog == null) {
            mErrorDialog = new SpokenErrorDialog(getContext());
        }
        mErrorDialog.show();
    }

    @Override
    public void onFailed(String text) {
        mSpokenWaveView.setVisibility(View.GONE);
        ToastManager.showMsg(text);
    }

    private void closeAnim() {
        try {
            mSpokenWaveView.stop();
            mRecordAnimView.clearAnimation();
            mSpokenWaveView.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResetClick() {

    }

    @Override
    public void onAgainClick() {

    }
}
