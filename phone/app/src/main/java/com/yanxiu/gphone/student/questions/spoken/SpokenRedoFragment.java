package com.yanxiu.gphone.student.questions.spoken;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.SpokenSpanTextView;
import com.yanxiu.gphone.student.customviews.SpokenWaveView;
import com.yanxiu.gphone.student.db.SpManager;
import com.yanxiu.gphone.student.mistakeredo.MistakeRedoActivity;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.MediaPlayerUtil;
import com.yanxiu.gphone.student.util.NetWorkUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/16 9:43.
 * Function :
 */
public class SpokenRedoFragment extends RedoSimpleExerciseBaseFragment implements View.OnClickListener, ClickableImageSpan.onSpanClickListener, SpokenUtils.onBaseOralEvalCallback, SpokenResultDialog.onDisMissCallback, SpokenUtils.onOralEvaProgressCallback {

    private View rootView;
    private SpokenQuestion mData;
    private SpokenSpanTextView mQuestionView;
    private ImageView mRecordView;
    private ImageView mPlayOrStopView;
    private ImageView mRecordAnimView;
    private SpokenWaveView mSpokenWaveView;
    private AudioTagHandler mAudioTagHandler;
    private String mFilePath;

    private boolean isCanPlayQuestionViedio = true;
    private boolean isFirstPlay = false;
    private boolean isHasAudioPermission = false;
    //仅仅用于标识6.0及以上权限弹窗是否弹出
    private boolean isShowPermissionDialog = false;

    private SpokenResultDialog mResultDialog;
    private SpokenErrorDialog mErrorDialog;
    private MediaPlayerUtil mPlayerUtil;
    private MediaPlayerUtil mLocationUtil;
    private SpokenUtils mSpokenUtils;
    private SpokenFirstEnterDialog mFirstEnterDialog;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mData = (SpokenQuestion) node;
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
        View view = inflater.inflate(R.layout.fragment_spoken, container, false);
        mResultDialog = SpokenResultDialog.create(getContext());
        mPlayerUtil = MediaPlayerUtil.create();
        mErrorDialog = new SpokenErrorDialog(getContext());
        mSpokenUtils = SpokenUtils.create();
        mLocationUtil = MediaPlayerUtil.create();
        isHasAudioPermission = false;
        setQaNumber(view);
        setQaName(view);
        initComplexStem(view, mData);
        initView(view);
        listener();
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSpokenUtils.cancel();
        mPlayerUtil.destory();
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
        mResultDialog.setDisMissCallback(SpokenRedoFragment.this);
        mRecordView.setOnTouchListener(mTouchListener);
        mPlayOrStopView.setOnClickListener(SpokenRedoFragment.this);
    }

    private void initData() {
        mAudioTagHandler = new AudioTagHandler(getContext(), mQuestionView, SpokenRedoFragment.this);
        Spanned string = Html.fromHtml(mData.getStem(), new HtmlImageGetter(mQuestionView), mAudioTagHandler);
        mQuestionView.setData(string);
        mQuestionView.setHighlightColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        mQuestionView.setMovementMethod(SpokenLinkMovementMethod.getInstance());

//        mQuestionView.setText("akskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksaakskuksa");

        setAudioButtonCanClick();
    }

    private void setAudioButtonCanClick() {
        if ("27".equals(mData.getType_id()) || "28".equals(mData.getType_id())) {
//            mSpokenWaveView.setVisibility(View.GONE);
            mRecordView.setEnabled(false);
            mRecordAnimView.setVisibility(View.GONE);
        } else {
//            mSpokenWaveView.setVisibility(View.VISIBLE);
            mRecordView.setEnabled(true);
            mRecordAnimView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
        if (isVisibleToUser) {
            isCanPlayQuestionViedio = true;
            if (mData.getAnswerList() == null || mData.getAnswerList().isEmpty()) {
                mQuestionView.setClick();
            }
            setAudioButtonCanClick();

            if ("26".equals(mData.getType_id()) || "29".equals(mData.getType_id())) {
                if (SpManager.isFristEnterSpokenQuestion()) {
                    if (mFirstEnterDialog == null) {
                        mFirstEnterDialog = new SpokenFirstEnterDialog(getContext());
                    }
                    mFirstEnterDialog.show();
                    SpManager.setFristEnterSpokenQuestion(false);
                }
            }
            if (mData.getAnswerList() != null && !mData.getAnswerList().isEmpty()) {
                SpokenResponse response = SpokenQuestion.getBeanFromJson(mData.getAnswerList().get(0));
                mFilePath = response.url;
                setScore(SpokenQuestion.getScore((int) response.lines.get(0).score));

//                mSpokenWaveView.setVisibility(View.VISIBLE);
                mRecordView.setEnabled(true);
                mRecordAnimView.setVisibility(View.VISIBLE);
                mPlayOrStopView.setVisibility(View.VISIBLE);
            } else {
                mPlayOrStopView.setVisibility(View.GONE);
                setScore(-1);
            }
        } else {
            mPlayerUtil.playFinish();
            if (mAudioTagHandler != null) {
                mAudioTagHandler.stop();
            }
//            mSpokenUtils.playClear();
            mLocationUtil.playFinish();
            mPlayOrStopView.setImageResource(R.drawable.spoken_play_vedio);
            mSpokenUtils.cancel();
        }
    }

    @Override
    public void onAnswerCardVisibleToUser(boolean isVisibleToUser) {
        super.onAnswerCardVisibleToUser(isVisibleToUser);
        if (isVisibleToUser) {
            mPlayerUtil.playFinish();
            if (mAudioTagHandler != null) {
                mAudioTagHandler.stop();
            }
//            mSpokenUtils.playClear();
            mLocationUtil.playFinish();
            mPlayOrStopView.setImageResource(R.drawable.spoken_play_vedio);
            mSpokenUtils.cancel();
        }
    }

    private void setScore(int num) {
        switch (num) {
            case 0:
                rootView.findViewById(R.id.ll_hand).setVisibility(View.VISIBLE);
                ((ImageView) rootView.findViewById(R.id.iv_hand1)).setImageResource(R.drawable.spoken_like_gry);
                ((ImageView) rootView.findViewById(R.id.iv_hand2)).setImageResource(R.drawable.spoken_like_gry);
                ((ImageView) rootView.findViewById(R.id.iv_hand3)).setImageResource(R.drawable.spoken_like_gry);
                break;
            case 1:
                rootView.findViewById(R.id.ll_hand).setVisibility(View.VISIBLE);
                ((ImageView) rootView.findViewById(R.id.iv_hand1)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView) rootView.findViewById(R.id.iv_hand2)).setImageResource(R.drawable.spoken_like_gry);
                ((ImageView) rootView.findViewById(R.id.iv_hand3)).setImageResource(R.drawable.spoken_like_gry);
                break;
            case 2:
                rootView.findViewById(R.id.ll_hand).setVisibility(View.VISIBLE);
                ((ImageView) rootView.findViewById(R.id.iv_hand1)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView) rootView.findViewById(R.id.iv_hand2)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView) rootView.findViewById(R.id.iv_hand3)).setImageResource(R.drawable.spoken_like_gry);
                break;
            case 3:
                rootView.findViewById(R.id.ll_hand).setVisibility(View.VISIBLE);
                ((ImageView) rootView.findViewById(R.id.iv_hand1)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView) rootView.findViewById(R.id.iv_hand2)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView) rootView.findViewById(R.id.iv_hand3)).setImageResource(R.drawable.spoken_like_red);
                break;
            default:
                rootView.findViewById(R.id.ll_hand).setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //诡异的逻辑

            //这部分不管是否有权限都执行
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setIsCanClick(false);
                    mPlayerUtil.playFinish();
                    if (mAudioTagHandler != null) {
                        mAudioTagHandler.stop();
                    }
//                    mSpokenUtils.playClear();
                    mLocationUtil.playFinish();
                    mPlayOrStopView.setImageResource(R.drawable.spoken_play_vedio);
                    mPlayOrStopView.setEnabled(false);
                    mRecordView.setImageResource(R.drawable.spoken_record_press);
                    isCanPlayQuestionViedio = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    setIsCanClick(true);
                    mRecordView.setImageResource(R.drawable.spoken_record_normal);
                    mPlayOrStopView.setEnabled(true);
                    break;
            }

            //判断权限
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //6.0系统及以上
                    if (!isHasAudioPermission && !isShowPermissionDialog) {
                        isShowPermissionDialog = true;
                        isHasAudioPermission = YanxiuBaseActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, new OnPermissionCallback() {
                            @Override
                            public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                                isHasAudioPermission = true;
                                isShowPermissionDialog = false;
                            }

                            @Override
                            public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                                isHasAudioPermission = false;
                                isShowPermissionDialog = false;
                            }
                        });
                    }
                } else {
                    //6.0系统以下
                    //音频权限较为特殊，6.0以下判断依据为先录制3秒(时间可以自己设置，不一定非得3秒)左右，如果录制的音频文件大小为0K，即为没有权限
                    isHasAudioPermission = true;
                }
            }

            //有权限才执行
            if (isHasAudioPermission) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mSpokenUtils.start(getContext(), mData.getSpokenAnswer(), SpokenRedoFragment.this, SpokenRedoFragment.this);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mSpokenUtils.stop();
                        break;
                }
            }
            return true;
        }
    };

    private void setIsCanClick(boolean isCanClick) {
        /* *
         * 错题重做既然已经把activity和基类fragment都改了，为啥这里不改，留着坑人啊？
         * */
//        if (getActivity() instanceof AnswerQuestionActivity) {
//            ((MistakeRedoActivity) getActivity()).setCanClick(isCanClick);
//        }
//        Fragment fragment = getParentFragment();
//        if (fragment != null && fragment instanceof AnswerComplexExerciseBaseFragment) {
//            ((AnswerComplexExerciseBaseFragment) fragment).setCanScroll(isCanClick);
//        }
        if (getActivity() instanceof MistakeRedoActivity) {
            ((MistakeRedoActivity) getActivity()).setCanClick(isCanClick);
        }
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof RedoComplexExerciseBaseFragment) {
            ((RedoComplexExerciseBaseFragment) fragment).setCanScroll(isCanClick);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_stop:
                playLocationVideo(false);
                break;
        }
    }

    private void playLocationVideo(final boolean canShowDialog) {
        isCanPlayQuestionViedio = false;
        mPlayerUtil.playFinish();
        if (mAudioTagHandler != null) {
            mAudioTagHandler.stop();
        }

        if (mLocationUtil.isPlaying()) {
            mLocationUtil.playFinish();
            if (canShowDialog || isFirstPlay) {
                mResultDialog.show();
            }
            isFirstPlay = false;
            isCanPlayQuestionViedio = true;
            mPlayOrStopView.setImageResource(R.drawable.spoken_play_vedio);
        } else {
            mLocationUtil.addMediaPlayerCallBack(new MediaPlayerUtil.MediaPlayerCallBack() {
                @Override
                public void onStart(MediaPlayerUtil mpu, int duration) {
                    mPlayOrStopView.setImageResource(R.drawable.spoken_stop_vedio);
                }

                @Override
                public void onProgress(MediaPlayerUtil mu, int progress) {

                }

                @Override
                public void onCompletion(MediaPlayerUtil mu) {
                    if (canShowDialog || isFirstPlay) {
                        mResultDialog.show();
                    }
                    isFirstPlay = false;
                    isCanPlayQuestionViedio = true;
                    mPlayOrStopView.setImageResource(R.drawable.spoken_play_vedio);
                }

                @Override
                public void onError(MediaPlayerUtil mu) {
                    ToastManager.showMsg(R.string.voice_url_error);
                    if (canShowDialog || isFirstPlay) {
                        mResultDialog.show();
                    }
                    isFirstPlay = false;
                    isCanPlayQuestionViedio = true;
                    mPlayOrStopView.setImageResource(R.drawable.spoken_play_vedio);
                }
            });
            mLocationUtil.start(mFilePath);
        }
    }

    @Override
    public void onClick(String url) {
        if (!isCanPlayQuestionViedio) {
            return;
        }
//        mSpokenUtils.playClear();
        mLocationUtil.playFinish();
        mPlayOrStopView.setImageResource(R.drawable.spoken_play_vedio);
        mPlayerUtil.addMediaPlayerCallBack(new MediaPlayerUtil.MediaPlayerCallBack() {
            @Override
            public void onStart(MediaPlayerUtil mpu, int duration) {
                if (mAudioTagHandler != null) {
                    mAudioTagHandler.start();
                }
            }

            @Override
            public void onProgress(MediaPlayerUtil mu, int progress) {

            }

            @Override
            public void onCompletion(MediaPlayerUtil mu) {
                if (mAudioTagHandler != null) {
                    mAudioTagHandler.stop();
                }
                mRecordView.setEnabled(true);
                mRecordAnimView.setVisibility(View.VISIBLE);
                if (SpManager.isFristEnterSpokenQuestion()) {
                    if (mFirstEnterDialog == null) {
                        mFirstEnterDialog = new SpokenFirstEnterDialog(getContext());
                    }
                    mFirstEnterDialog.show();
                    SpManager.setFristEnterSpokenQuestion(false);
                }
            }

            @Override
            public void onError(MediaPlayerUtil mu) {
                if (mAudioTagHandler != null) {
                    mAudioTagHandler.stop();
                }
                if (NetWorkUtils.isNetAvailable()) {
                    ToastManager.showMsg(R.string.voice_url_error);
                } else {
                    ToastManager.showMsg(R.string.net_null);
                }
            }
        });
        mPlayerUtil.start(url);
//        mPlayerUtil.start("http://data.5sing.kgimg.com/G034/M05/16/17/ApQEAFXsgeqIXl7gAAVVd-n31lcAABOogKzlD4ABVWP363.mp3");
    }

    @Override
    public void onStartRecord(String filePath) {
        this.mFilePath = filePath;
        mSpokenWaveView.start();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_spoken_record);
        mRecordAnimView.startAnimation(animation);
        mSpokenWaveView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopOralEval(boolean isError) {
        if (!isError) {
            mPlayOrStopView.setVisibility(View.VISIBLE);
            isFirstPlay = true;
            playLocationVideo(true);
        } else {
            closeAnim();
        }
    }

    @Override
    public void onVolume(int volume) {
        mSpokenWaveView.setVolume(volume);
    }

    @Override
    public void onStartOralEval() {
        closeAnim();
    }

    @Override
    public void onResult(String result) {
        SpokenResponse response = SpokenQuestion.getBeanFromJson(result);
        mResultDialog.setScore(SpokenQuestion.getScore((int) response.lines.get(0).score));
        response.url = mFilePath;
        Gson gson = new Gson();
        mData.getAnswerList().clear();
        mData.getAnswerList().add(gson.toJson(response));
        mData.setHasAnswered(true);
        saveAnswer(mData);
        updateProgress();
    }

    @Override
    public void onResultUrl(String filePath, String url) {
//        ToastManager.showMsg(url);
        this.mFilePath = url;
    }

    @Override
    public void onNoPermission() {
        mSpokenWaveView.setVisibility(View.GONE);
        isCanPlayQuestionViedio = true;
        SpokenPermissionDialog permissionDialog = SpokenPermissionDialog.creat(getContext());
        permissionDialog.show();
    }

    @Override
    public void onError(String result) {
        mSpokenWaveView.setVisibility(View.GONE);
        isCanPlayQuestionViedio = true;
        if (mErrorDialog == null) {
            mErrorDialog = new SpokenErrorDialog(getContext());
        }
        mErrorDialog.show();
    }

    @Override
    public void onFailed(String text) {
        mSpokenWaveView.setVisibility(View.GONE);
        isCanPlayQuestionViedio = true;
        ToastManager.showMsg(text);
    }

    @Override
    public void disMissCallback() {
        if (mData.getAnswerList() != null && !mData.getAnswerList().isEmpty()) {
            SpokenResponse response = SpokenQuestion.getBeanFromJson(mData.getAnswerList().get(0));
            setScore(SpokenQuestion.getScore((int) response.lines.get(0).score));
        }
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onFinished() {
        mSpokenUtils.stop();
        ToastManager.showMsg("录音不能超过3分钟哦");
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
    public void onDestroy() {
        super.onDestroy();
        mPlayerUtil.playFinish();
        if (mAudioTagHandler != null) {
            mAudioTagHandler.stop();
        }
//            mSpokenUtils.playClear();
        mLocationUtil.playFinish();
        mPlayOrStopView.setImageResource(R.drawable.spoken_play_vedio);
        mSpokenUtils.cancel();
    }
}
