package com.yanxiu.gphone.student.questions.connect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.anim.AlphaAnimationUtil;

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
    private int[] mEndLocation = new int[2];
    private View mLeftSelectedItemView,mRightSelectedItemView;

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

    private int[] computeStartLocation(RecyclerView recyclerView, View itemView, int itemPosition){
        int[] location = new int[2];
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisiblePos = layoutManager.findFirstVisibleItemPosition();
        int lastVisiblePos = layoutManager.findLastVisibleItemPosition();
        if(itemPosition < firstVisiblePos){
            recyclerView.getLocationInWindow(location);
            location[0] = location[0] + recyclerView.getPaddingLeft();
            location[1] = location[1] - itemView.getHeight();
        }else if (itemPosition > lastVisiblePos){
            recyclerView.getLocationInWindow(location);
            location[0] = location[0] + recyclerView.getPaddingLeft();
            location[1] = location[1] + recyclerView.getHeight();
        }else {
            itemView.getLocationInWindow(location);
        }
        return location;
    }

    private void initListener() {
        mLeftAdapter.setOnItemClickListener(new ConnectItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, ConnectItemBean itemBean, int position) {
                mLeftSelectedItem = itemBean;
                mLeftSelectedItemView = itemView;
                if (mRightSelectedItem != null) {
                    mBasket.getLocationInWindow(mEndLocation);
                    mEndLocation[0] = mEndLocation[0] + mBasket.getWidth() / 2;
                    int[] start_location1 = computeStartLocation(mRecyclerViewLeft,mLeftSelectedItemView,position);
                    int[] start_location2 = computeStartLocation(mRecyclerViewRight,mRightSelectedItemView,mRightAdapter.getLastSelectedPosition());
                    mConnectedList.add(new ConnectedBean(mLeftSelectedItem, mRightSelectedItem));
                    mRightAdapter.remove(mRightAdapter.getLastSelectedPosition());
                    mLeftAdapter.remove(position);
                    ConnectAnimationHelper.startDropIntoBasketAnimation(getActivity(),mBasket,mLeftSelectedItemView,start_location1,mEndLocation);
                    ConnectAnimationHelper.startDropIntoBasketAnimation(getActivity(),mBasket,mRightSelectedItemView,start_location2,mEndLocation);
                    mLeftSelectedItemView = null ;
                    mRightSelectedItemView = null;
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
                mRightSelectedItemView = itemView;
                if (mLeftSelectedItem != null) {
                    mBasket.getLocationInWindow(mEndLocation);
                    mEndLocation[0] = mEndLocation[0] + mBasket.getWidth() / 2;
                    int[] start_location1 = computeStartLocation(mRecyclerViewLeft,mLeftSelectedItemView,mLeftAdapter.getLastSelectedPosition());
                    int[] start_location2 = computeStartLocation(mRecyclerViewRight,mRightSelectedItemView,position);
                    mConnectedList.add(new ConnectedBean(mLeftSelectedItem, mRightSelectedItem));
                    mLeftAdapter.remove(mLeftAdapter.getLastSelectedPosition());
                    mRightAdapter.remove(position);
                    ConnectAnimationHelper.startDropIntoBasketAnimation(getActivity(),mBasket,mLeftSelectedItemView,start_location1,mEndLocation);
                    ConnectAnimationHelper.startDropIntoBasketAnimation(getActivity(),mBasket,mRightSelectedItemView,start_location2,mEndLocation);
                    mLeftSelectedItemView = null ;
                    mRightSelectedItemView = null;
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

        mTextStem.post(new Runnable() {
            @Override
            public void run() {
                Spanned spanned = Html.fromHtml(mQuestion.getStem(),new HtmlImageGetter(mTextStem),null);
                mTextStem.setText(spanned);
            }
        });

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
        mBasket.post(new Runnable() {
            @Override
            public void run() {
                mBasket.getLocationInWindow(mEndLocation);
                mEndLocation[0] = mEndLocation[0] + mBasket.getWidth() / 2;
            }
        });
    }


    private void initPopWindow() {
        if (mPopWindow == null) {
            View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_connect_result, null);
            mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopWindow.setAnimationStyle(R.style.pop_anim);
            View pop_bg = contentView.findViewById(R.id.pop_bg);
            View dismiss = contentView.findViewById(R.id.dismiss);
            View btnClear = contentView.findViewById(R.id.tv_clear);
            FrameLayout animationLayout = (FrameLayout) contentView.findViewById(R.id.animation_layout);

            mRecyclerViewResult = (RecyclerView) contentView.findViewById(R.id.recyclerView);
            mResultAdapter = new ConnectResultAdapter(mConnectedList);
            mRecyclerViewResult.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerViewResult.setAdapter(mResultAdapter);

            mResultAdapter.setAnimationLayout(animationLayout);

            pop_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissResult();
                }
            });

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

//            ((AnswerQuestionActivity)getActivity()).getOverlayView().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismissResult();
//                }
//            });
        }
    }

    private void showResult() {
        initPopWindow();
        ((AnswerQuestionActivity)getActivity()).getOverlayView().setVisibility(View.VISIBLE);
        AlphaAnimationUtil.startPopBgAnimIn(((AnswerQuestionActivity)getActivity()).getOverlayView());
        mPopWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void dismissResult() {
        if (mResultAdapter!=null) {
            mResultAdapter.clearAnim();
        }
        AlphaAnimationUtil.startPopBgAnimExit(((AnswerQuestionActivity)getActivity()).getOverlayView());
        ((AnswerQuestionActivity)getActivity()).getOverlayView().setVisibility(View.GONE);
        if (mPopWindow.isShowing())
            mPopWindow.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBasket.clearAnimation();
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
        List<String> serverFilledAnswers = new ArrayList<>();
        List<String> localFilledAnswers = new ArrayList<>();
        for (ConnectedBean bean : mConnectedList) {
            int leftPos = bean.getLeftItem().getOriginPosition();
            int rightPos = bean.getRightItem().getOriginPosition();

            localFilledAnswers.add(leftPos + "," + rightPos);

            rightPos += mQuestion.getChoices().size() / 2;
            serverFilledAnswers.add(leftPos + "," + rightPos);
        }
        if (serverFilledAnswers.size() < mQuestion.getChoices().size() / 2) {
            int count = mQuestion.getChoices().size() / 2 - serverFilledAnswers.size();
            for (int i = 0; i < count; i++) {
                serverFilledAnswers.add("");
            }
        }

        mQuestion.setFilledAnswers(localFilledAnswers);
        mQuestion.setServerFilledAnswers(serverFilledAnswers);

        boolean hasAnswered = true;
        if (serverFilledAnswers.size() == 0) {
            hasAnswered = false;
        } else {
            for (String s : serverFilledAnswers) {
                if (TextUtils.isEmpty(s)) {
                    hasAnswered = false;
                    break;
                }
            }
        }
        mQuestion.setHasAnswered(hasAnswered);

        super.saveAnswer(question);
    }
}
