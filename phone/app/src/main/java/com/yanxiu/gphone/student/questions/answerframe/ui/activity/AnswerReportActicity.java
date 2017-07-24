package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.AnswerReportTitleView;
import com.yanxiu.gphone.student.customviews.UnMoveGridView;
import com.yanxiu.gphone.student.questions.answerframe.adapter.AnswerReportAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;
import com.yanxiu.gphone.student.util.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 答题报告
 * Created by 戴延枫 on 2017/6/9.
 */

public class AnswerReportActicity extends YanxiuBaseActivity implements OnAnswerCardItemSelectListener,View.OnClickListener {


    private String mKey;//获取数据的key
    private Paper mPaper;//试卷数据
    private ArrayList<BaseQuestion> mQuestions;
    private String mPPid;
    private String mTitleString;

    private RelativeLayout mNopigai_layout, mPigai_layout;
    private TextView mTitle;
    private ImageView mBackview;
    //批改view
    private TextView mTextview_correct;//正确率
    private TextView mTextview_correct_shadow;//正确率的阴影
    private TextView mTotalnumber;//总题数
    private TextView mYesnumber;//答对题数
    private TextView mTime;//用时

    private LinearLayout mCardGrid;
    private int mSpanCount;
    private int mSpacing;

    private String mStatus;//试卷状态 0未批改 1已经批改
    private int mSccuracy = 0;  //正确率
    private int mRightCount;//做对的题数
    private int mTotalCount;//总题数
    private String mCostTime = "0";//用时

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_report);
        initData();
        initView();
    }

    private void initData() {
        mKey = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        if (TextUtils.isEmpty(mKey))
            finish();
        mPaper = DataFetcher.getInstance().getPaper(mKey);
        QuestionUtil.initDataWithAnswer(mPaper);
        mQuestions = new ArrayList<>();
        mQuestions.addAll(mPaper.getQuestions());//为了不修改paper里的题目数据Questions
        Paper.generateUsedNumbersForNodes(mQuestions);
        mQuestions = QuestionUtil.allNodesThatHasNumber(mQuestions);
        mPPid = mPaper.getId();
        mStatus = mPaper.getPaperStatus().getCheckStatus();
//        mSccuracy = (int) (QuestionUtil.calculateRightRate(mQuestions) * 100);
        mSccuracy = (int)(mPaper.getPaperStatus().getScoreRate() * 100);

        mRightCount = QuestionUtil.calculateRightCount(mQuestions);
        mCostTime = mPaper.getPaperStatus().getCosttime();
        try {
            mCostTime = TimeUtils.formatTime(Integer.valueOf(mCostTime));
//            mSccuracy = Integer.parseInt(scoreRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTotalCount = QuestionUtil.getTotalCount(mQuestions);
        mTitleString = mPaper.getName();
        calculationSpanCount();
    }

    private void initView() {
        mNopigai_layout = (RelativeLayout) findViewById(R.id.nopigai_layout);
        mPigai_layout = (RelativeLayout) findViewById(R.id.pigai_layout);
        mTitle = (TextView) findViewById(R.id.report_title);
        mTitle.setText(mTitleString);
        mBackview = (ImageView) findViewById(R.id.backview);
        mBackview.setOnClickListener(this);
        mTextview_correct = (TextView) findViewById(R.id.textview_correct);
        mTextview_correct_shadow = (TextView) findViewById(R.id.textview_correct_shadow);
        mTotalnumber = (TextView) findViewById(R.id.totalnumber);
        mYesnumber = (TextView) findViewById(R.id.yesnumber);
        mTime = (TextView) findViewById(R.id.time);

        mCardGrid = (LinearLayout) findViewById(R.id.card_grid);

        if ("1".equals(mStatus)) {
            //已经批改
            mTextview_correct.setText(mSccuracy+"");
            mTextview_correct_shadow.setText(mSccuracy+"");
            mTotalnumber.setText("共" + mTotalCount + "题");
            mYesnumber.setText("答对" + mRightCount + "题");
            mTime.setText(mCostTime);
            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_DEMI_BOLD,mTextview_correct,mTextview_correct_shadow);
            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_MEDIUM_PLAY,mTime);
            mNopigai_layout.setVisibility(View.GONE);
            mPigai_layout.setVisibility(View.VISIBLE);
        } else {
            mNopigai_layout.setVisibility(View.VISIBLE);
            mPigai_layout.setVisibility(View.GONE);
        }
        addGridView(QuestionUtil.classifyQuestionByType(mQuestions));
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

        }
    }

    /**
     * 计算gridView的SpanCount
     */
    private void calculationSpanCount() {
        int screenWidth = ScreenUtils.getScreenWidth(this);
        int item_width = getResources().getDimensionPixelSize(R.dimen.answer_card_item_width);
        int item_space = getResources().getDimensionPixelSize(R.dimen.answer_card_item_space);
        //计算公式： item_width * X + （X+1）* item_space = screenWidth
//        mSpanCount = (screenWidth -30) / 150;
        mSpanCount = (screenWidth - item_space) / (item_width + item_space);
        mSpacing = (screenWidth - item_width * mSpanCount) / (mSpanCount + 1);
    }


    /**
     * 跳转AnswerQuestionActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String key) {
        Intent intent = new Intent(activity, AnswerReportActicity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        activity.startActivity(intent);
    }

    @Override
    public void onItemSelect(BaseQuestion question) {
        String key = this.hashCode() + mPPid;
        DataFetcher.getInstance().save(key, mPaper);
        AnalysisQuestionActivity.invoke(this, key, question.getLevelPositions());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backview:
                finish();
                break;
        }
    }
}
