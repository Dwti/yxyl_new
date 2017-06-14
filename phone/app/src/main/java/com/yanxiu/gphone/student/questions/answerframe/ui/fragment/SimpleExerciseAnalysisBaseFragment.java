package com.yanxiu.gphone.student.questions.answerframe.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.NotesActicity;
import com.yanxiu.gphone.student.questions.yesno.YesNoQuestion;
import com.yanxiu.gphone.student.util.ToastManager;

import de.greenrobot.event.EventBus;

/**
 * Created by 戴延枫 on 2017/6/8.
 * 单题型解析Frament基类
 */

public abstract class SimpleExerciseAnalysisBaseFragment extends ExerciseAnalysisBaseFragment implements View.OnClickListener {
    private BaseQuestion mData;
    private TextView mQuestionView;

    public View mRootView;
    public LinearLayout mAnsewr_container, mAnalysis_container;
    public TextView v1, v2, v3, edit, mNotesTextView;

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
        initView();
        return mRootView;
    }

    private void initView() {
        mAnsewr_container = (LinearLayout) mRootView.findViewById(R.id.ansewr_container);
        mAnalysis_container = (LinearLayout) mRootView.findViewById(R.id.analysis_container);
        v1 = (TextView) mRootView.findViewById(R.id.v1);
        v2 = (TextView) mRootView.findViewById(R.id.v2);
        v3 = (TextView) mRootView.findViewById(R.id.v3);
        edit = (TextView) mRootView.findViewById(R.id.edit);
        mNotesTextView = (TextView) mRootView.findViewById(R.id.notesContent);

        initAnswerView();
        initAnalysisView();
        initListener();
    }


    private void initData() {

    }

    private void initListener() {
        edit.setOnClickListener(this);
    }

    public abstract void initAnswerView();

    public abstract void initAnalysisView();

    public void showView1() {
        v1.setVisibility(View.VISIBLE);
    }

    public void showView2() {
        v2.setVisibility(View.VISIBLE);
    }

    public void showView3() {
        v3.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                NotesActicity.invoke(getActivity(), edit.hashCode());
                break;
        }
    }

    public void onEventMainThread(NotesActicity.NotesMessage notesMessage) {
        int viewHashCode = notesMessage.mViewHashCode;
        String notesContent = notesMessage.mNotesContent;
        if (viewHashCode == edit.hashCode())
            mNotesTextView.setText(notesContent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
