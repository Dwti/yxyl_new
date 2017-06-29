package com.yanxiu.gphone.student.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.util.TimeUtils;

import java.util.ArrayList;

/**
 * 答题卡提交过程Dialog,该Dialog不具有扩展性，只适合答题卡提交进度显示
 * 2017/6/20
 * create by dyf
 */
public class AnswerCardSubmitDialog extends Dialog implements View.OnClickListener, AnswercardSubmitProgressView.DisplacementListener {

    private Context mContext;
    protected RelativeLayout mRootView;//根view
    private RelativeLayout mSubmiting_layout, mState_layout,mSuccess_layout;

    //progress
    private AnswercardSubmitProgressView mProgressbar;
    private ImageView mPincil;

    //state
    private TextView mState_title, mState_content;
    private Button mButton_yes, mButton_no;

    //success
    private TextView mSuccess_content;
    private Button mSuccess_layout_button;

    private ArrayList<BaseQuestion> mQuestions;
    private SubmitState state;

    private boolean hasSubjectiveImg;

    public enum SubmitState {
        STATE_HAS_NO_ANSWERED,
        STATE_RETRY,
        STATE_PROGRESS,
        STATE_SUCCESS;
    }

    public AnswerCardSubmitDialog(Context context) {
        this(context, R.style.AnswerCarDialog);
        init(context);
    }

    public AnswerCardSubmitDialog(Context context, int theme) {
        super(context, R.style.AnswerCarDialog);
        init(context);
    }

    private AnswerCardSubmitDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(mRootView);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(mRootView);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(mRootView, params);
    }

    private void init(Context context) {
        mContext = context;
        mRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.answercard_dialog, null);
        setContentView(mRootView);
        mSubmiting_layout = (RelativeLayout) mRootView.findViewById(R.id.submiting_layout);
        mState_layout = (RelativeLayout) mRootView.findViewById(R.id.state_layout);

        mProgressbar = (AnswercardSubmitProgressView) mRootView.findViewById(R.id.progressbar);
        mPincil = (ImageView) mRootView.findViewById(R.id.pincil);
        mProgressbar.setDisplacementListener(this);

        initStateView();
        initSuccessView();

        hasSubjectiveImg = false;
    }

    private void initStateView() {
        mState_title = (TextView) mRootView.findViewById(R.id.state_title);
        mState_content = (TextView) mRootView.findViewById(R.id.state_content);
        mButton_yes = (Button) mRootView.findViewById(R.id.button_yes);
        mButton_no = (Button) mRootView.findViewById(R.id.button_no);
        mButton_yes.setOnClickListener(this);
        mButton_no.setOnClickListener(this);
    }
    private void initSuccessView() {
        mSuccess_layout = (RelativeLayout) mRootView.findViewById(R.id.success_layout);
        mSuccess_content = (TextView) mRootView.findViewById(R.id.success_content2);
        mSuccess_layout_button = (Button) mRootView.findViewById(R.id.success_layout_button);
        mSuccess_layout_button.setOnClickListener(this);
    }

    public void setData(ArrayList<BaseQuestion> questions) {
        mQuestions = questions;
    }

    public void setProgressbarMaxCount(int count){
        mProgressbar.setMaxCount(count);
    }
    public void updateProgress(int progress){
        mProgressbar.updateProgress(progress);
    }

    /**
     * 有未达单独题目，确认提交界面
     */
    public void showConfirmView() {
        state = SubmitState.STATE_HAS_NO_ANSWERED;
        mState_layout.setVisibility(View.VISIBLE);
        mSubmiting_layout.setVisibility(View.GONE);
        mState_title.setText("还有未答完的题目");
        mState_content.setText("确定要提交吗？");
        mButton_yes.setText("提交");
    }

    /**
     * 上传失败，重试界面
     */
    public void showRetryView() {
        state = SubmitState.STATE_RETRY;
        mState_layout.setVisibility(View.VISIBLE);
        mSubmiting_layout.setVisibility(View.GONE);
        mState_title.setText("作业上传失败");
        mState_content.setText("请检查网络是否异常后重试");
        mButton_yes.setText("再试一次");
    }

    /**
     * 上传进度界面
     */
    public void showProgressView() {
        state = SubmitState.STATE_PROGRESS;
        hasSubjectiveImg = true;
        mSubmiting_layout.setVisibility(View.VISIBLE);
        mState_layout.setVisibility(View.GONE);

    }

    /**
     * 提交成功，但是不能进入答题报告界面
     */
    public void showSuccessView(long endTime) {
        state = SubmitState.STATE_SUCCESS;
        mSuccess_content.setText(TimeUtils.getTimeLongYMD(endTime));
        mSuccess_layout.setVisibility(View.VISIBLE);
        mSubmiting_layout.setVisibility(View.GONE);
        mState_layout.setVisibility(View.GONE);

    }

    public SubmitState getState() {
        return state;
    }

    public boolean isHasSubjectiveImg() {
        return hasSubjectiveImg;
    }

    @Override
    public void xPositionChange(int x) {
        mPincil.setX(mPincil.getX() + x);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_yes:
                mClickListener.onDialogButtonClick(v,state);
                break;
            case R.id.button_no:
                dismiss();
                break;
            case R.id.success_layout_button:
                ((Activity)mContext).finish();
                break;
        }
    }

    @Override
    public void dismiss() {
        mClickListener = null;
        mContext = null;
        super.dismiss();
    }

    public interface AnswerCardSubmitDialogClickListener {
        void onDialogButtonClick(View v , SubmitState state);
    }

    private AnswerCardSubmitDialog.AnswerCardSubmitDialogClickListener mClickListener;

    public void setAnswerCardSubmitDialogClickListener(AnswerCardSubmitDialog.AnswerCardSubmitDialogClickListener listener) {
        mClickListener = listener;
    }
}