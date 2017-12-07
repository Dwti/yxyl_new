package com.yanxiu.gphone.student.mistakeredo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseFragment;
import com.yanxiu.gphone.student.mistakeredo.adapter.RedoAnswerCardAdapter;
import com.yanxiu.gphone.student.questions.answerframe.adapter.GridSpacingItemDecoration;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by 戴延枫 on 2017/5/7.
 * 答题卡
 */

public class RedoAnswerCardFragment extends YanxiuBaseFragment{
    private final String TAG = RedoAnswerCardFragment.class.getSimpleName();
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
    private RedoAnswerCardAdapter mAnswerCardAdapter;
    private RedoAnswerCardAdapter.OnItemClickListener mListener;

    private int mSpanCount;
    private int mSpacing;

    private MistakeRedoActivity mActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_reodo_answer_card, container, false);
        initView();
        return mRootView;
    }

    public void setData(Paper paper, String title,int totalCount) {
        mPaper = paper;
//        mQuestions = QuestionUtil.allNodesThatHasNumber(paper.getQuestions());
        mQuestions = paper.getQuestions();
        for(int i = mQuestions.size(); i < totalCount; i++){
            //不够的用null填充
            mQuestions.add(null);
        }
        mTitleString = title;
    }

    public void initView() {
        if (isAdded() && getActivity() instanceof MistakeRedoActivity) {
            mActivity = (MistakeRedoActivity) getActivity();
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
        mAnswerCardAdapter = new RedoAnswerCardAdapter(getActivity(), mListener);
        mAnswerCardAdapter.setData(mQuestions);
        mRecyclerView.setAdapter(mAnswerCardAdapter);

        initListener();
    }

    public void initListener() {
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(RedoAnswerCardFragment.this).commit();
                mActivity.controlListenView(false);
            }
        });
    }

    public void setOnCardItemSelectListener(RedoAnswerCardAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 计算recyclerView的SpanCount
     */
    private void calculationSpanCount() {
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        int item_width = (int) getResources().getDimensionPixelSize(R.dimen.answer_card_item_width);
        int item_space = (int) getResources().getDimensionPixelSize(R.dimen.answer_card_item_space);
        mSpanCount = (screenWidth - item_space) / (item_width + item_space);
        mSpacing = (screenWidth - item_width * mSpanCount) / (mSpanCount + 1);
    }















}
