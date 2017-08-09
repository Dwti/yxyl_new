package com.yanxiu.gphone.student.questions.answerframe.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseFragment;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.AnswerCardSubmitDialog;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.db.SpManager;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.answerframe.http.request.AnswerReportRequest;
import com.yanxiu.gphone.student.questions.answerframe.http.request.SubmitQuesitonTask;
import com.yanxiu.gphone.student.questions.answerframe.adapter.AnswerCardAdapter;
import com.yanxiu.gphone.student.questions.answerframe.adapter.GridSpacingItemDecoration;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.listener.SubmitAnswerCallback;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerReportActicity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;

import static com.yanxiu.gphone.student.customviews.AnswerCardSubmitDialog.SubmitState.STATE_PROGRESS;
import static com.yanxiu.gphone.student.customviews.AnswerCardSubmitDialog.SubmitState.STATE_RETRY;

/**
 * Created by 戴延枫 on 2017/5/7.
 * 答题卡
 */

public class AnswerCardFragment extends YanxiuBaseFragment implements View.OnClickListener, AnswerCardSubmitDialog.AnswerCardSubmitDialogClickListener {
    private final String TAG = AnswerCardFragment.class.getSimpleName();
    private View mRootView;
    private Paper mPaper;
    private ArrayList<BaseQuestion> mQuestions;
    private Paper mPaper_report;//答题报告返回的数据
    private ArrayList<BaseQuestion> mQuestions_report;//答题报告返回的数据
    private String mTitleString;
    private ImageView mBackView;
    private TextView mTitle;
    private Button mSubmiButton;
    private RecyclerView mRecyclerView;
    private AnswerCardAdapter mAnswerCardAdapter;
    private OnAnswerCardItemSelectListener mListener;

    private int mSpanCount;
    private int mSpacing;

    private AnswerQuestionActivity mActivity;
    private SubmitQuesitonTask mSubmitQuesitonTask;

    private AnswerReportRequest mRequest;//答题报告请求

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_answer_card, container, false);
        initView();
        return mRootView;
    }

    public void setData(Paper paper, String title) {
        mPaper = paper;
        mQuestions = QuestionUtil.allNodesThatHasNumber(paper.getQuestions());
        mTitleString = title;
    }

    public void initView() {
        if (isAdded() && getActivity() instanceof AnswerQuestionActivity) {
            mActivity = (AnswerQuestionActivity) getActivity();
        }
        mSubmiButton = (Button) mRootView.findViewById(R.id.submit_homework);
        mBackView = (ImageView) mRootView.findViewById(R.id.backview);
        mTitle = (TextView) mRootView.findViewById(R.id.title);
        if (!TextUtils.isEmpty(mTitleString))
            mTitle.setText(mTitleString);
        mBackView.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_recyclerView);
        calculationSpanCount();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mSpanCount));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(mSpanCount, mSpacing, true));
        mAnswerCardAdapter = new AnswerCardAdapter(getActivity(), mListener);
        mAnswerCardAdapter.setData(mQuestions);
        mRecyclerView.setAdapter(mAnswerCardAdapter);

        initListener();
    }

    public void initListener() {
        mSubmiButton.setOnClickListener(this);
        mBackView.setOnClickListener(this);
    }

    public void setOnCardItemSelectListener(OnAnswerCardItemSelectListener listener) {
        mListener = listener;
    }

    /**
     * 计算recyclerView的SpanCount
     */
    private void calculationSpanCount() {
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        int item_width = (int) getResources().getDimensionPixelSize(R.dimen.answer_card_item_width);
        int item_space = (int) getResources().getDimensionPixelSize(R.dimen.answer_card_item_space);
        Log.d(TAG, "screenWidth: " + screenWidth);
        Log.d(TAG, "item_width: " + item_width);
        Log.d(TAG, "item_space: " + item_space);
        //计算公式： item_width * X + （X+1）* item_space = screenWidth
//        mSpanCount = (screenWidth -30) / 150;
        mSpanCount = (screenWidth - item_space) / (item_width + item_space);
        Log.d(TAG, "mSpanCount: " + mSpanCount);
        mSpacing = (screenWidth - item_width * mSpanCount) / (mSpanCount + 1);
        Log.d(TAG, "other: " + mSpacing);
    }

    private AnswerCardSubmitDialog mDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_homework:
                mDialog = new AnswerCardSubmitDialog(getActivity());
                mDialog.setCancelable(false);
                mDialog.setData(mQuestions);
                mDialog.setAnswerCardSubmitDialogClickListener(AnswerCardFragment.this);
                switch (checkAnswerState()) {
                    case STATE_HAS_NO_ANSWERED:
                        mDialog.showConfirmView();
//                        mDialog.show();
                        break;
                    case STATE_PROGRESS:
                        mDialog.showProgressView();
//                        mDialog.show();
                        requestSubmmit();
                        break;
                    default:
                        mDialog.showLoadingView();
                        requestSubmmit();
                        break;
                }
                break;
            case R.id.backview:
                getActivity().getSupportFragmentManager().beginTransaction().remove(AnswerCardFragment.this).commit();
                break;
        }
    }

    private AnswerCardSubmitDialog.SubmitState checkAnswerState() {
        AnswerCardSubmitDialog.SubmitState state;
        for (int i = 0; i < mQuestions.size(); i++) {
            if (!mQuestions.get(i).getIsAnswer()) { //有未作答的
                state = AnswerCardSubmitDialog.SubmitState.STATE_HAS_NO_ANSWERED;
                return state;
            }
        }
        for (int i = 0; i < mQuestions.size(); i++) {
            if (QuestionTemplate.ANSWER.equals(mQuestions.get(i).getTemplate()) && mQuestions.get(i).getIsAnswer()) { //主观题且回答了，有图片
                state = STATE_PROGRESS;
                return state;
            }
        }
        return state = STATE_RETRY;
    }

    private AnswerCardSubmitDialog.SubmitState checkHasImgState() {
        AnswerCardSubmitDialog.SubmitState state;
        for (int i = 0; i < mQuestions.size(); i++) {
            if (QuestionTemplate.ANSWER.equals(mQuestions.get(i).getTemplate()) && mQuestions.get(i).getIsAnswer()) { //主观题且回答了，有图片
                state = STATE_PROGRESS;
                return state;
            }
        }
        return state = null;
    }

    private void requestSubmmit() {
        if (getActivity() == null) {
            return;
        }
        if (mSubmitQuesitonTask != null && !mSubmitQuesitonTask.isCancelled()) {
            mSubmitQuesitonTask.cancel(true);
        }
        String endtime = String.valueOf(System.currentTimeMillis());
        mPaper.getPaperStatus().setEndtime(endtime);
        mPaper.getPaperStatus().setCosttime(mActivity.getmTotalTime() + "");
        mSubmitQuesitonTask = new SubmitQuesitonTask(YanxiuApplication.getContext(), mPaper, mSubmitQuesitonTask.SUBMIT_CODE, new SubmitAnswerCallback() {

            @Override
            public void onSuccess() {
                //提交答案成功，直接请求答题报告
                questReportData();
            }

            @Override
            public void onFail() {
                initDialog();
                mDialog.showRetryView();
            }

            @Override
            public void onUpdate(int count, int index) {
                if (mDialog != null && mDialog.isShowing() && mDialog.getState() == STATE_PROGRESS) {
                    mDialog.setProgressbarMaxCount(count);
                    mDialog.updateProgress(index);
                }
            }

            @Override
            public void onDataError(String msg) {
                ToastManager.showMsg(msg);
                if (null != mDialog)
                    mDialog.dismiss();
            }
        });
        mSubmitQuesitonTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    @Override
    public void onDialogButtonClick(View v, AnswerCardSubmitDialog.SubmitState state) {
        switch (v.getId()) {
            case R.id.button_yes:
                initDialog();
                switch (state) {
                    case STATE_HAS_NO_ANSWERED:
                        if(AnswerCardSubmitDialog.SubmitState.STATE_PROGRESS == checkHasImgState()){
                            mDialog.showProgressView();
                        }else{
                            mDialog.showLoadingView();
                        }
                        requestSubmmit();
                        break;
                    case STATE_RETRY:
                        if (mDialog.isHasSubjectiveImg()) {
//                            mDialog.resetProgress();
                            mDialog.showProgressView();
                        }else{
                            mDialog.showLoadingView();
                        }
                        requestSubmmit();
                        break;
                    case STATE_PROGRESS:
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void initDialog() {
        if (mDialog == null) {
            mDialog = new AnswerCardSubmitDialog(getActivity());
            mDialog.setCancelable(true);
            mDialog.setData(mQuestions);
            mDialog.setAnswerCardSubmitDialogClickListener(AnswerCardFragment.this);
        }
    }

    //请求答题报告
    private void questReportData() {
        if (TextUtils.isEmpty(mPaper.getId())) {
            return;
        }
        if (mRequest != null) {
            mRequest.cancelRequest();
            mRequest = null;
        }
        mRequest = new AnswerReportRequest(mPaper.getId());
        mRequest.bodyDealer = new DESBodyDealer();
        mRequest.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {

            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                if (response.getStatus().getCode() == 0) {
                    if (response.getData().size() > 0) {
                        mPaper_report = new Paper(response.getData().get(0), QuestionShowType.ANALYSIS);
                        if (mPaper_report != null && mPaper_report.getQuestions() != null && mPaper_report.getQuestions().size() > 0) {
                            SaveAnswerDBHelper.deleteAllAnswer();
                            SpManager.clearAnswerTime();
                            String key = this.hashCode() + mPaper.getId();
                            DataFetcher.getInstance().save(key, mPaper_report);


                            String showna = mPaper_report.getShowana();
                            if (Constants.NOT_FINISH_STATUS.equals(showna)) {
                                long groupStartTime = Long.parseLong(mPaper_report.getBegintime());
                                long groupEndtime = Long.parseLong(mPaper_report.getEndtime());//作业练习截止时间

//                                if (groupEndtime > groupStartTime && ((groupEndtime - System.currentTimeMillis()) >= 3 * 60 * 1000)) { //作业截止时间判断，还未到截止时间不产生作业报告
//                                    ToastManager.showMsg("提交成功");
                                mDialog.showSuccessView(groupEndtime);
                                mDialog.show();
//                                } else {
//                                    AnswerReportActicity.invoke(getActivity(), key);
//                                    getActivity().finish();
//                                }

                            } else if (Constants.HAS_FINISH_CHECK_REPORT.equals(showna)) {
                                AnswerReportActicity.invoke(getActivity(), key);
                                getActivity().finish();
                            } else if (Constants.MAINAVTIVITY_FROMTYPE_EXERCISE.equals(mActivity.getFromType())) {
                                //练习过来的
                                AnswerReportActicity.invoke(getActivity(), key,Constants.MAINAVTIVITY_FROMTYPE_EXERCISE,mActivity.getGenQuesequest());
                                getActivity().finish();
                            } else {
                                ToastManager.showMsg("提交成功");
                                getActivity().finish();
                            }

                        }
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                initDialog();
//                mDialog.showRetryView();
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        if(mDialog != null){
            mDialog.dismiss();
        }
        super.onDestroy();
    }
}
