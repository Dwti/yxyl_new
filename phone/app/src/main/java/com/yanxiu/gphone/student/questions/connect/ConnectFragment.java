package com.yanxiu.gphone.student.questions.connect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

/**
 * Created by sunpeng on 2017/7/12.
 */

public class ConnectFragment extends AnswerSimpleExerciseBaseFragment {

    private ConnectQuestion mQuestion;
    private RecyclerView mRecyclerViewLeft, mRecyclerViewRight, mRecyclerViewResult;
    private ConnectResultAdapter mResultAdapter;
    private ConnectItemAdapter mLeftAdapter, mRightAdapter;
    private List<String> mChoicesLeft, mChoicesRight;
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
//        List<String> choices = mQuestion.getChoices();
//        mChoicesLeft = choices.subList(0,(choices.size() / 2) -1 );
//        mChoicesRight = choices.subList(choices.size() / 2, choices.size() -1);


        mChoicesLeft = new ArrayList<>();
        mChoicesRight = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mChoicesLeft.add("" + i + "" + i + "" + i + "" + "");
            mChoicesRight.add("" + i + "" + i + "" + i + "" + "");
        }

        mLeftAdapter = new ConnectItemAdapter(mChoicesLeft);
        mRightAdapter = new ConnectItemAdapter(mChoicesRight);

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
            mPopWindow = new PopupWindow(contentView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            View dismiss = contentView.findViewById(R.id.dismiss);
            mRecyclerViewResult = (RecyclerView) contentView.findViewById(R.id.recyclerView);
            mResultAdapter = new ConnectResultAdapter(mConnectedList);
            mRecyclerViewResult.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerViewResult.setAdapter(mResultAdapter);
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
                }
            });
        }
    }

    private void showResult() {
        initPopWindow();
        mPopWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER,0,0);
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
        super.saveAnswer(question);
    }
}
