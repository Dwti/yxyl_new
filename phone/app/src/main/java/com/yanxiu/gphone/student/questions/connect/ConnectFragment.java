package com.yanxiu.gphone.student.questions.connect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sunpeng on 2017/7/12.
 */

public class ConnectFragment extends AnswerSimpleExerciseBaseFragment {

    private ConnectQuestion mQuestion;
    private RecyclerView mRecyclerViewLeft, mRecyclerViewRight, mRecyclerViewResult;
    private ConnectResultAdapter mResultAdapter;
    private ConnectItemAdapter mLeftAdapter, mRightAdapter;
    private List<ConnectItemBean> mLeftChoices = new ArrayList<>();
    private List<ConnectItemBean> mRightChoices = new ArrayList<>();
    private List<ConnectedBean> mConnectedList = new ArrayList<>();
    private ConnectItemBean mLeftSelectedItem;
    private ConnectItemBean mRightSelectedItem;
    private PopupWindow mPopWindow;
    private TextView mTextStem;
    private View mBasket;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_connect, container, false);
        setQaNumber(root);
        setQaName(root);
        initComplexStem(root, mQuestion);
        initView(root);
        initData();
        initListener();
        return root;
    }

    private void initListener() {
        mLeftAdapter.setOnItemClickListener(new ConnectItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, ConnectItemBean itemBean, int position) {
                mLeftSelectedItem = itemBean;
                if (mRightSelectedItem != null) {
                    mConnectedList.add(new ConnectedBean(mLeftSelectedItem, mRightSelectedItem));
                    mLeftAdapter.remove(position);
                    mRightAdapter.remove(mRightAdapter.getLastSelectedPosition());
                    mLeftSelectedItem = null;
                    mRightSelectedItem = null;
                    saveAnswer(mQuestion);
                    updateProgress();
                }
            }
        });

        mRightAdapter.setOnItemClickListener(new ConnectItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, ConnectItemBean itemBean, int position) {
                mRightSelectedItem = itemBean;
                if (mLeftSelectedItem != null) {
                    mConnectedList.add(new ConnectedBean(mLeftSelectedItem, mRightSelectedItem));
                    mLeftAdapter.remove(mLeftAdapter.getLastSelectedPosition());
                    mRightAdapter.remove(position);
                    mLeftSelectedItem = null;
                    mRightSelectedItem = null;
                    saveAnswer(mQuestion);
                    updateProgress();
                }
            }
        });

        mBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResult();
            }
        });
    }

    private void initData() {

        List<String> leftTexts = mQuestion.getLeftChoices();
        List<String> rightTexts = mQuestion.getRightChoices();

        for (int i = 0; i < leftTexts.size(); i++) {
            mLeftChoices.add(new ConnectItemBean(leftTexts.get(i), i));
        }

        for (int i = 0; i < rightTexts.size(); i++) {
            mRightChoices.add(new ConnectItemBean(rightTexts.get(i), i));
        }

        List<String> filledAnswers = mQuestion.getFilledAnswers();
        for(String str: filledAnswers){
            if(!TextUtils.isEmpty(str) && str.contains(",")){
                int left = Integer.parseInt(str.split(",")[0]);
                int right = Integer.parseInt(str.split(",")[1]);
                ConnectItemBean leftItem = new ConnectItemBean(leftTexts.get(left),left);
                ConnectItemBean rightItem = new ConnectItemBean(rightTexts.get(right),right);

                mConnectedList.add(new ConnectedBean(leftItem,rightItem));


                Iterator<ConnectItemBean> leftIterator = mLeftChoices.iterator();
                while (leftIterator.hasNext()){
                    if(leftIterator.next().getOriginPosition() == left){
                        leftIterator.remove();
                    }
                }

                Iterator<ConnectItemBean> rightIterator = mRightChoices.iterator();
                while (rightIterator.hasNext()){
                    if(rightIterator.next().getOriginPosition() == right){
                        rightIterator.remove();
                    }
                }

            }
        }

        mLeftAdapter = new ConnectItemAdapter(mLeftChoices);
        mRightAdapter = new ConnectItemAdapter(mRightChoices);

        mRecyclerViewLeft.setAdapter(mLeftAdapter);
        mRecyclerViewRight.setAdapter(mRightAdapter);

    }

    private void initView(View root) {
        mRecyclerViewLeft = (RecyclerView) root.findViewById(R.id.recyclerView_left);
        mRecyclerViewRight = (RecyclerView) root.findViewById(R.id.recyclerView_right);
        mRecyclerViewLeft.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewRight.setLayoutManager(new LinearLayoutManager(getContext()));
        mTextStem = (TextView) root.findViewById(R.id.stem);
        mBasket = root.findViewById(R.id.basket);
    }

    private void initPopWindow() {
        if (mPopWindow == null) {
            View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_connect_result, null);
            mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            View dismiss = contentView.findViewById(R.id.dismiss);
            View btnClear = contentView.findViewById(R.id.tv_clear);
            mRecyclerViewResult = (RecyclerView) contentView.findViewById(R.id.recyclerView);
            mResultAdapter = new ConnectResultAdapter(mConnectedList);
            mRecyclerViewResult.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerViewResult.setAdapter(mResultAdapter);

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mConnectedList.size() > 0) {
                        List<ConnectItemBean> leftToAdd = new ArrayList<>();
                        List<ConnectItemBean> rightToAdd = new ArrayList<>();
                        for (ConnectedBean bean : mConnectedList) {
                            leftToAdd.add(bean.getLeftItem());
                            rightToAdd.add(bean.getRightItem());
                        }
                        mResultAdapter.clear();
                        mLeftAdapter.addAll(leftToAdd);
                        mRightAdapter.addAll(rightToAdd);
                        saveAnswer(mQuestion);
                        updateProgress();
                    }
                }
            });
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissResult();
                }
            });
            mResultAdapter.setOnItemDeteleListener(new ConnectResultAdapter.OnItemDeletedListener() {
                @Override
                public void onDeleted(ConnectedBean bean) {
                    mLeftAdapter.add(bean.getLeftItem());
                    mRightAdapter.add(bean.getRightItem());
                    saveAnswer(mQuestion);
                    updateProgress();
                }
            });
        }
    }

    private void showResult() {
        initPopWindow();
        mPopWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void dismissResult() {
        if (mPopWindow.isShowing())
            mPopWindow.dismiss();
    }

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mQuestion = (ConnectQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setData((ConnectQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    @Override
    public void saveAnswer(BaseQuestion question) {
        List<String> answers = new ArrayList<>();
        for (ConnectedBean bean : mConnectedList) {
            int leftPos = bean.getLeftItem().getOriginPosition();
            int rightPos = bean.getRightItem().getOriginPosition();
            rightPos += mQuestion.getChoices().size() / 2;

            answers.add(leftPos + "," + rightPos);
        }
        if (answers.size() < mQuestion.getChoices().size() / 2) {
            int count = mQuestion.getChoices().size() / 2 - answers.size();
            for (int i = 0; i < count; i++) {
                answers.add("");
            }
        }

        mQuestion.setServerFilledAnswers(answers);

        boolean hasAnswered = true;
        if (answers.size() == 0) {
            hasAnswered = false;
        } else {
            for (String s : answers) {
                if (TextUtils.isEmpty(s)) {
                    hasAnswered = false;
                    break;
                }
            }
        }
        mQuestion.setIsAnswer(hasAnswered);

        super.saveAnswer(question);
    }
}