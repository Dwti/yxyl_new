package com.yanxiu.gphone.student.questions.answerframe.ui.fragment;

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

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.adapter.AnswerCardAdapter;
import com.yanxiu.gphone.student.questions.answerframe.adapter.GridSpacingItemDecoration;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;

/**
 * Created by 戴延枫 on 2017/5/7.
 * 答题卡
 */

public class AnswerCardFragment extends Fragment implements View.OnClickListener {
    private final String TAG = AnswerCardFragment.class.getSimpleName();
    private View mRootView;
    private ArrayList<BaseQuestion> mQuestions;
    private String mTitleString;
    private ImageView mBackView;
    private TextView mTitle;
    private Button mSubmiButton;
    private RecyclerView mRecyclerView;
    private AnswerCardAdapter mAnswerCardAdapter;
    private OnAnswerCardItemSelectListener mListener;

    private int mSpanCount;
    private int mSpacing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_answer_card, container, false);
        initView();
        return mRootView;
    }

    public void setData(ArrayList<BaseQuestion> questions,String title) {
        mQuestions = allNodesThatHasNumber(questions);
        mTitleString = title;
    }

    public void initView() {
        mSubmiButton = (Button) mRootView.findViewById(R.id.submit_homework);
        mBackView = (ImageView) mRootView.findViewById(R.id.backview);
        mTitle = (TextView) mRootView.findViewById(R.id.title);
        if(!TextUtils.isEmpty(mTitleString))
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

    /**
     * 给答题卡设置题号
     *
     * @return
     */
    private ArrayList<BaseQuestion> allNodesThatHasNumber(ArrayList<BaseQuestion> questions) {
        ArrayList<BaseQuestion> retNodes = new ArrayList<>();
        for (BaseQuestion node : questions) {
            retNodes.addAll(node.allNodesThatHasNumber());
        }

        return retNodes;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_homework:
//            QALevelSingleton.bus.post("save answer event");
                ToastManager.showMsg("提交");
                break;
            case R.id.backview:
                getActivity().getSupportFragmentManager().beginTransaction().remove(AnswerCardFragment.this).commit();
                break;
        }
    }
}
