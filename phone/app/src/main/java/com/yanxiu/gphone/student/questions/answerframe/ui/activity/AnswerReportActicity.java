package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.AnswerReportTitleView;
import com.yanxiu.gphone.student.customviews.UnMoveGridView;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.answerframe.adapter.AnswerReportAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.bean.ReportAnswerBean;
import com.yanxiu.gphone.student.questions.answerframe.http.request.AnswerReportRequest;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;
import com.yanxiu.gphone.student.util.TimeUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yanxiu.gphone.student.constant.Constants.EXTRA_TITLE;
import static com.yanxiu.gphone.student.constant.Constants.PPID_KEY;

/**
 * Created by 戴延枫 on 2017/6/9.
 */

public class AnswerReportActicity extends YanxiuBaseActivity implements OnAnswerCardItemSelectListener {

    private Paper mPaper;
    private ArrayList<BaseQuestion> mQuestions;
    private String mPPid;
    private String mTitleString;
    private AnswerReportRequest mRequest;

    private RelativeLayout mNopigai_layout, mPigai_layout;
    //批改view
    private TextView mTextview_correct;//正确率
    private TextView mTotalnumber;//总题数
    private TextView mYesnumber;//答对题数
    private TextView mTime;//用时

    private LinearLayout mCardGrid;
    private int mSpanCount;
    private int mSpacing;

    private String mStatus;//试卷状态 0未批改 1已经批改
    private int mSccuracy;  //正确率
    private int mRightCount;//做对的题数
    private int mTotalCount;//总题数
    private String mCostTime = "0";//用时

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_report);
        questData();
    }


    private void questData() {
        mPPid = getIntent().getStringExtra(PPID_KEY);
//        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        calculationSpanCount();
        requestData();
    }

    private void initView() {
        initData();
        mNopigai_layout = (RelativeLayout) findViewById(R.id.nopigai_layout);
        mPigai_layout = (RelativeLayout) findViewById(R.id.pigai_layout);
        mTextview_correct = (TextView) findViewById(R.id.textview_correct);
        mTotalnumber = (TextView) findViewById(R.id.totalnumber);
        mYesnumber = (TextView) findViewById(R.id.yesnumber);
        mTime = (TextView) findViewById(R.id.time);

        mCardGrid = (LinearLayout) findViewById(R.id.card_grid);

        if ("1".equals(mStatus)) {
            //已经批改
            mTextview_correct.setText(mSccuracy + "");
            mTotalnumber.setText("共" + mTotalCount + "题");
            mYesnumber.setText("答对" + mRightCount + "题");
            mTime.setText(mCostTime);

            mNopigai_layout.setVisibility(View.GONE);
            mPigai_layout.setVisibility(View.VISIBLE);
        } else {
            mNopigai_layout.setVisibility(View.VISIBLE);
            mPigai_layout.setVisibility(View.GONE);
        }
    }

    private void initData() {
        mStatus = mPaper.getStatus();
        mSccuracy = (int) QuestionUtil.calculateRightRate(mQuestions) * 100;
        mRightCount = QuestionUtil.calculateRightCount(mQuestions);
        mCostTime = mPaper.getPaperStatus().getCosttime();
        try {
            mCostTime = TimeUtils.formatTime(Integer.valueOf(mCostTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mQuestions.size() > 0) {
            mTotalCount = mQuestions.size();
        }
        mTitleString = mPaper.getName();
    }

    private void requestData() {
        if (TextUtils.isEmpty(mPPid)) {
            return;
        }
        if (mRequest != null) {
            mRequest.cancelRequest();
            mRequest = null;
        }
        mRequest = new AnswerReportRequest(mPPid);
        mRequest.bodyDealer = new DESBodyDealer();
        mRequest.startRequest(PaperResponse.class, new HttpCallback<PaperResponse>() {
            @Override
            public void onSuccess(RequestBase request, PaperResponse ret) {
                if (ret.getStatus().getCode() == 0) {
                    if (ret.getData().size() > 0) {
                        mPaper = new Paper(ret.getData().get(0), QuestionShowType.ANALYSIS);
                        if (mPaper != null && mPaper.getQuestions() != null && mPaper.getQuestions().size() > 0) {
                            QuestionUtil.initDataWithAnswer(mPaper);
                            mQuestions = new ArrayList<>();
                            mQuestions.addAll(mPaper.getQuestions());//为了不修改paper里的题目数据Questions
                            Paper.generateUsedNumbersForNodes(mQuestions);
                            mQuestions = QuestionUtil.allNodesThatHasNumber(mQuestions);

                            initView();

                            addGridView(QuestionUtil.classifyQuestionByType(mQuestions));

                        }
                    } else {
                        ToastManager.showMsg("ffffff");
                    }
                } else {
                    ToastManager.showMsg("666");
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
            }
        });
    }

    private void addGridView(Map<String, List<BaseQuestion>> map) {
        if (map == null || map.size() == 0)
            return;
        Set<Map.Entry<String, List<BaseQuestion>>> set = map.entrySet();
        Iterator<Map.Entry<String, List<BaseQuestion>>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<BaseQuestion>> entry = iterator.next();

            AnswerReportTitleView titleView = new AnswerReportTitleView(this);
            titleView.setTitleText(entry.getKey());

            LinearLayout.LayoutParams layoutParamsGrid = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsGrid.topMargin = 0;
            layoutParamsGrid.bottomMargin = 0;
            layoutParamsGrid.leftMargin = mSpacing;
            layoutParamsGrid.rightMargin = mSpacing;
            UnMoveGridView gridView = new UnMoveGridView(this);
            Drawable itemDrawable = getResources().getDrawable(R.drawable.report_grid_item_selector);
            gridView.setNumColumns(mSpanCount);
            gridView.setHorizontalSpacing(mSpacing);
            gridView.setSelector(itemDrawable);
            gridView.setVerticalSpacing(mSpacing);
//            //gridView.setStretchMode(GridView.STRETCH_SPACING);
            gridView.setLayoutParams(layoutParamsGrid);
            gridView.setAdapter(new AnswerReportAdapter(AnswerReportActicity.this, entry.getValue(), this));
            mCardGrid.addView(titleView);
            mCardGrid.addView(gridView);

//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    BaseQuestion questionEntity = (BaseQuestion) parent.getAdapter().getItem(position);
////                    jumpType(questionEntity, dataList);
//                }
//            });
        }
    }

    /**
     * 计算gridView的SpanCount
     */
    private void calculationSpanCount() {
        int screenWidth = ScreenUtils.getScreenWidth(this);
        int item_width = getResources().getDimensionPixelSize(R.dimen.answer_card_item_width);
        int item_space = getResources().getDimensionPixelSize(R.dimen.answer_card_item_space);
//        Log.d(TAG, "screenWidth: " + screenWidth);
//        Log.d(TAG, "item_width: " + item_width);
//        Log.d(TAG, "item_space: " + item_space);
        //计算公式： item_width * X + （X+1）* item_space = screenWidth
//        mSpanCount = (screenWidth -30) / 150;
        mSpanCount = (screenWidth - item_space) / (item_width + item_space);
//        Log.d(TAG, "mSpanCount: " + mSpanCount);
        mSpacing = (screenWidth - item_width * mSpanCount) / (mSpanCount + 1);
//        Log.d(TAG, "other: " + mSpacing);
    }

    /**
     * 跳转AnswerReportActicity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String ppid) {
        Intent intent = new Intent(activity, AnswerReportActicity.class);
        intent.putExtra(PPID_KEY, ppid);
        activity.startActivity(intent);
    }

    @Override
    public void onItemSelect(BaseQuestion question) {
        String key = this.hashCode()+mPPid;
        DataFetcher.getInstance().save(key,mPaper);
        AnalysisQuestionActivity.invoke(this,key,question.getLevelPositions());
    }
}
