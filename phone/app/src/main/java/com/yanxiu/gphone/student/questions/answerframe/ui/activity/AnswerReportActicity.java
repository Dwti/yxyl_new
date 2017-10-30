package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bcresource.request.ResetTopicPaperHistoryRequest;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.AnswerCardSubmitDialog;
import com.yanxiu.gphone.student.customviews.AnswerReportTitleView;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.customviews.UnMoveGridView;
import com.yanxiu.gphone.student.customviews.vieweffect.GradientEffect;
import com.yanxiu.gphone.student.customviews.vieweffect.GradientEffectImpl;
import com.yanxiu.gphone.student.exercise.request.GenQuesRequest;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.answerframe.adapter.AnswerReportAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;
import com.yanxiu.gphone.student.util.TimeUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 答题报告
 * Created by 戴延枫 on 2017/6/9.
 */

public class AnswerReportActicity extends YanxiuBaseActivity implements OnAnswerCardItemSelectListener, View.OnClickListener {

    private String mFromType;//从哪个页面跳转过来的（练习页面）
    private GenQuesRequest mGenQuesequest;//从选择章节只是点进入答题页，章节知识点的请求Request
    private LoadingView mLoading_view;//loadingview

    private String mKey;//获取数据的key
    private Paper mPaper;//试卷数据
    private ArrayList<BaseQuestion> mQuestions;
    private String mPPid;
    private String mTitleString;
    private ScrollView mScrollView;
    private RelativeLayout mNopigai_layout, mPigai_layout;

    private GradientEffectImpl mGradientEffect;
    private View mTitle_bar, layout_reset_answer;
    private TextView mTitle;
    private ImageView mBackview;
    private View mOnceagainLayout;
    private Button mOnceagain,mBtnResetAnswer;//再练一组
    //批改view
    private TextView mTextview_correct;//正确率
    private TextView mTextview_correct_shadow;//正确率的阴影
    private TextView mTotalnumber;//总题数
    private TextView mYesnumber;//答对题数
    private TextView mTime;//用时
    private String mRmsPaperId;
    private LinearLayout mCardGrid;
    private int mSpanCount;
    private int mSpacing;

    private String mStatus;//试卷状态 0未批改 1已经批改
    private String mSccuracy = "0";  //正确率
    private int mRightCount;//做对的题数
    private int mTotalCount;//总题数
    private String mCostTime = "0";//用时

    private AnswerCardSubmitDialog mDialog;

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
        mFromType = getIntent().getStringExtra(Constants.EXTRA_FROMTYPE);
        if(Constants.FROM_BC_RESOURCE.equals(mFromType)){
            mRmsPaperId = getIntent().getStringExtra(Constants.EXTRA_RMSPAPER);
        }
        initExerciseData();
        mPaper = DataFetcher.getInstance().getPaper(mKey);
        QuestionUtil.initDataWithAnswer(mPaper);
        mQuestions = new ArrayList<>();
        mQuestions.addAll(mPaper.getQuestions());//为了不修改paper里的题目数据Questions
        Paper.generateUsedNumbersForNodes(mQuestions);
        mQuestions = QuestionUtil.allNodesThatHasNumber(mQuestions);
        mPPid = mPaper.getId();
        mStatus = mPaper.getPaperStatus().getCheckStatus();
//        mSccuracy = (int) (QuestionUtil.calculateRightRate(mQuestions) * 100);
        mSccuracy = NumberFormat.getPercentInstance().format(mPaper.getPaperStatus().getScoreRate()).replace("%","");

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

    /**
     * 从练习页面跳转过来的，初始化相关数据
     */
    private void initExerciseData(){
        if(Constants.MAINAVTIVITY_FROMTYPE_EXERCISE.equals(mFromType)){
            mGenQuesequest = (GenQuesRequest)getIntent().getSerializableExtra(Constants.EXTRA_REQUEST);
            mOnceagainLayout = findViewById(R.id.wavesLayout);
            mOnceagain = (Button)findViewById(R.id.onceagain);
            mOnceagain.setText(getText(R.string.onceagain));
            mOnceagainLayout.setVisibility(View.VISIBLE);
            mOnceagain.setOnClickListener(this);
        }
    }

    private void initView() {
        mLoading_view = (LoadingView) findViewById(R.id.loading_view);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mNopigai_layout = (RelativeLayout) findViewById(R.id.nopigai_layout);
        mPigai_layout = (RelativeLayout) findViewById(R.id.pigai_layout);
        mTitle_bar = findViewById(R.id.title_bar);
        mTitle = (TextView) findViewById(R.id.report_title);
        mTitle.setText(mTitleString);
        mBackview = (ImageView) findViewById(R.id.backview);
        mBackview.setOnClickListener(this);
        mTextview_correct = (TextView) findViewById(R.id.textview_correct);
        mTextview_correct_shadow = (TextView) findViewById(R.id.textview_correct_shadow);
        mTotalnumber = (TextView) findViewById(R.id.totalnumber);
        mYesnumber = (TextView) findViewById(R.id.yesnumber);
        mTime = (TextView) findViewById(R.id.time);
        layout_reset_answer = findViewById(R.id.reset_answer);
        mBtnResetAnswer = (Button) findViewById(R.id.btn_reset_answer);

        mBtnResetAnswer.setOnClickListener(this);

        mCardGrid = (LinearLayout) findViewById(R.id.card_grid);

        if(!Constants.FROM_BC_RESOURCE.equals(mFromType)){
            layout_reset_answer.setVisibility(View.GONE);
        }

        if ("1".equals(mStatus)) {
            //已经批改
            mTextview_correct.setText(mSccuracy);
            mTextview_correct_shadow.setText(mSccuracy);
            mTotalnumber.setText("共" + mTotalCount + "题");
            mYesnumber.setText("答对" + mRightCount + "题");
            mTime.setText(mCostTime);
            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_DEMI_BOLD, mTextview_correct, mTextview_correct_shadow);
            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_MEDIUM_PLAY, mTime);
            mNopigai_layout.setVisibility(View.GONE);
            mPigai_layout.setVisibility(View.VISIBLE);
        } else {
            mNopigai_layout.setVisibility(View.VISIBLE);
            mPigai_layout.setVisibility(View.GONE);
        }
        addGridView(QuestionUtil.classifyQuestionByType(mQuestions));
        setDragAmin();
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
     * 设置拖拽渐变动画回调
     */
    private void setDragAmin() {
        mTitle_bar.getBackground().mutate();//因为加了alpha渐变，不加该方法，会在个别手机上造成闪屏。
        View sliderView;
        if ("1".equals(mStatus)) {
            //已经批改
            sliderView = mPigai_layout;
        }else{
            sliderView = mNopigai_layout;
        }
        mGradientEffect = new GradientEffectImpl(mTitle_bar, sliderView, mScrollView);
        mGradientEffect.setOnGradientEffectListener(new GradientEffect.OnGradientEffectListener() {
            @Override
            public void onGrade(float ratio) {
                //设置导航条背景透明度
                int alpha = (int) (ratio * 255);
                mTitle_bar.getBackground().setAlpha(alpha);
            }
        });
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
     * 跳转AnswerReportActicity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String key) {
        Intent intent = new Intent(activity, AnswerReportActicity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        activity.startActivity(intent);
    }

    public static void invoke(Activity activity, String key, String rmsPaperId, String fromType,int flag) {
        Intent intent = new Intent(activity, AnswerReportActicity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        intent.putExtra(Constants.EXTRA_RMSPAPER,rmsPaperId);
        intent.putExtra(Constants.EXTRA_FROMTYPE, fromType);
        activity.startActivity(intent);
    }

    /**
     * 练习跳转AnswerReportActicity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String key,String fromType, GenQuesRequest request) {
        Intent intent = new Intent(activity, AnswerReportActicity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        intent.putExtra(Constants.EXTRA_FROMTYPE, fromType);
        intent.putExtra(Constants.EXTRA_REQUEST,request);
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
        switch (v.getId()) {
            case R.id.backview:
                finish();
                break;
            case R.id.onceagain:
                 //练习里，再练一组的请求
                if(mGenQuesequest != null){
                    mLoading_view.showLoadingView();
                    mGenQuesequest.bodyDealer = new DESBodyDealer();
                    mGenQuesequest.startRequest(PaperResponse.class,new EXueELianBaseCallback<PaperResponse>() {
                        @Override
                        public void onResponse(RequestBase request, PaperResponse ret) {
                            mLoading_view.hiddenLoadingView();
                            if(ret.getStatus().getCode() == 0){
                                Paper paper = new Paper(ret.getData().get(0), QuestionShowType.ANSWER);
                                DataFetcher.getInstance().save(paper.getId(),paper);
                                AnswerQuestionActivity.invoke(AnswerReportActicity.this,paper.getId(), Constants.MAINAVTIVITY_FROMTYPE_EXERCISE,mGenQuesequest);
                                finish();
                            }else {
                                ToastManager.showMsg(ret.getStatus().getDesc());
                            }
                        }

                        @Override
                        public void onFail(RequestBase request, Error error) {
                            mLoading_view.hiddenLoadingView();
                            ToastManager.showMsg(error.getLocalizedMessage());
                        }
                    });
                }
                break;
            case R.id.btn_reset_answer:
                showResetHistoryDialog();
                break;
        }
    }

    private void openAnswerQuestionUI(String paperId){
        AnswerQuestionActivity.invoke(this,paperId,mRmsPaperId,Constants.FROM_BC_RESOURCE,0);
    }

    private void showResetHistoryDialog(){
        if(mDialog == null){
            mDialog = new AnswerCardSubmitDialog(this);
            mDialog.setCancelable(true);
            mDialog.setAnswerCardSubmitDialogClickListener(new AnswerCardSubmitDialog.AnswerCardSubmitDialogClickListener() {
                @Override
                public void onDialogButtonClick(View v, AnswerCardSubmitDialog.SubmitState state) {
                    resetTopicPaperHistory();
                    mDialog.dismiss();
                }
            });
        }
        mDialog.showResetConfirmView();
    }

    private void resetTopicPaperHistory(){
        ResetTopicPaperHistoryRequest request = new ResetTopicPaperHistoryRequest();
        request.bodyDealer = new DESBodyDealer();
        request.setPaperId(mKey);
        request.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                if(response.getStatus().getCode() == 0){
                    QuestionShowType type = QuestionShowType.ANSWER;
                    Paper paper = new Paper(response.getData().get(0), type);
                    DataFetcher.getInstance().save(paper.getId(),paper);
                    openAnswerQuestionUI(paper.getId());
                    finish();
                }else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                    ToastManager.showMsg(error.getLocalizedMessage());
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (mGradientEffect != null) {
            mGradientEffect.releaseObserver();
            mGradientEffect = null;
        }
        super.onDestroy();
    }
}
