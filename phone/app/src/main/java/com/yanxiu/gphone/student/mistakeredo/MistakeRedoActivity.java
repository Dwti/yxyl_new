package com.yanxiu.gphone.student.mistakeredo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.db.SpManager;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.mistakeredo.adapter.QAMistakeRedoAdapter;
import com.yanxiu.gphone.student.mistakeredo.adapter.RedoAnswerCardAdapter;
import com.yanxiu.gphone.student.mistakeredo.request.FinishReDoWorkRequest;
import com.yanxiu.gphone.student.mistakeredo.request.WrongQByQidsRequest;
import com.yanxiu.gphone.student.mistakeredo.response.CheckAnswerResponse;
import com.yanxiu.gphone.student.mistakeredo.response.FinishReDoWorkResponse;
import com.yanxiu.gphone.student.mistakeredo.utils.CheckAnswerManager;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerStateChangedListener;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.questions.answerframe.view.QAViewPager;
import com.yanxiu.gphone.student.questions.bean.PadBean;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.KeyboardObserver;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-11-24.
 */

/* *
 * 为啥乱分包，这样搞还叫项目么，敢不敢不要瞎整
 * */
public class MistakeRedoActivity extends YanxiuBaseActivity implements View.OnClickListener, RedoAnswerCardAdapter.OnItemClickListener, CheckAnswerManager.onCheckAnswerListener {

    private static final String TITLE = "title";
    private static final String SUBJECTID = "subjectId";
    private static final String STAGEID = "stageId";
    private static final String WRONG_NUM = "wrongNum";
    private static final String QIDS = "qids";

    private static final int SUBMIT_ABLE = 0;
    private static final int SUBMIT_UNABLE = 1;
    private static final int DELETE_ABLE = 2;
    private static final int DELETED = 3;

    private FragmentManager mFragmentManager;
    private QAViewPager mViewPager;
    private QAMistakeRedoAdapter mAdapter;
    private String mKey;//获取数据的key
    private String mTitleString;//试卷的title-答题卡需要
    private Paper mPaper;//试卷数据
    private ArrayList<BaseQuestion> mQuestions;//题目数据
    private RedoAnswerCardFragment mAnswerCardFragment;

    private View mResultCard;
    private KeyboardObserver mKeyboardObserver;
    private LinearLayout mPrevious_question, mNext_question;//上一题，下一题
    private TextView mNext_text;//下一题textview
    private ImageView mBackView;//返回按钮
    private ImageView mShowAnswerCardView,mAnswerResult;//显示答题卡
    private View mRootView, mOverlay;
    private LoadingView mLoadingView;
    private Button btn_submit;
    private int mBottomBtnState = -1;

    private int mNextQidPos;  //下一次请求的qids在mQids中起始的位置
    private static final int PAGE_SIZE = 5;
    private int mAnsCardClickPos;
    private String mStageId;
    private String mSubjectId;
    private String mTitle;
    private int mWrongNum;
    private ArrayList<String> mQids;
    private ArrayList<Integer> mDeletedPos = new ArrayList<>();
    private ArrayList<String> mDeleteQidsList = new ArrayList<>();
    private String mQidsToRemove = "";   //待删除的题

    private boolean isOnLoadMore = false;

    private InputMethodManager mInputMethodManager;

    private boolean isCanClick = true;

    private CheckAnswerManager mAnswerManager=CheckAnswerManager.create();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mistake_redo);
        isCanClick = true;
        initData();
        initView();
    }

    private void initData() {
        mKey = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        if (TextUtils.isEmpty(mKey))
            finish();
        mPaper = DataFetcher.getInstance().getPaper(mKey);
        if (mPaper == null) {
            this.finish();
            return;
        }
        mQuestions = mPaper.getQuestions();
//        mTitleString = mPaper.getName();
        mTitleString = getIntent().getStringExtra(TITLE);
        mSubjectId = getIntent().getStringExtra(SUBJECTID);
        mStageId = getIntent().getStringExtra(STAGEID);
        mWrongNum = getIntent().getIntExtra(WRONG_NUM, 0);
        mQids = getIntent().getStringArrayListExtra(QIDS);

    }

    private void initView() {
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mRootView = findViewById(R.id.fl_qa);
        mOverlay = findViewById(R.id.overlay);
        btn_submit = (Button) findViewById(R.id.submit);
        mPrevious_question = (LinearLayout) findViewById(R.id.previous_question);
        mNext_question = (LinearLayout) findViewById(R.id.next_question);
        mNext_text = (TextView) findViewById(R.id.next_text);
        mBackView = (ImageView) findViewById(R.id.backview);
        mResultCard = findViewById(R.id.answer_result);
        mAnswerResult = (ImageView) findViewById(R.id.iv_result);
        mShowAnswerCardView = (ImageView) findViewById(R.id.answercardview);

        initViewPager();
        setListener();
    }

    private void setListener() {
        mPrevious_question.setOnClickListener(this);
        mKeyboardObserver = new KeyboardObserver(mRootView);
        mNext_question.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mShowAnswerCardView.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        mAnswerManager.setCheckAnswerListener(this);
    }

    public void addKeyboardVisibleChangeListener(KeyboardObserver.KeyBoardVisibleChangeListener listener) {
        mKeyboardObserver.addKeyBoardVisibleChangeListener(listener);
    }

    public void removeKeyBoardVisibleChangeListener(KeyboardObserver.KeyBoardVisibleChangeListener listener) {
        mKeyboardObserver.removeKeyBoardVisibleChangeListener(listener);
    }

    private void initViewPager() {
        mFragmentManager = getSupportFragmentManager();
        mViewPager = (QAViewPager) findViewById(R.id.vp_viewPager);
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new QAMistakeRedoAdapter(mFragmentManager, null, mOnAnswerStateChangedListener);
        mAdapter.setSubjectId(mSubjectId);
        mAdapter.setData(mQuestions, mWrongNum);
        mViewPager.setAdapter(mAdapter);
        setCurrentState(mQuestions.get(0));
        mNextQidPos = mAdapter.getCount();

        mViewPager.setOnSwipeOutListener(new QAViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtEnd() {
//                showAnswerCardFragment();
            }
        });

        //外层viewpager
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int totalNum = mAdapter.getCount();
                if (totalNum < mWrongNum) {
                    if (mViewPager.getCurrentItem() + 1 > totalNum - 3 && !isOnLoadMore) {
                        requestData(getQids(mNextQidPos, PAGE_SIZE), false);
                    }
                }
                BaseQuestion baseQuestion = ((ExerciseBaseFragment) mAdapter.getItem(position)).mBaseQuestion;
                setCurrentState(baseQuestion);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //内层的viewpager
    private ViewPager.OnPageChangeListener mOnInnerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            BaseQuestion parentQuestion = mAdapter.getDatas().get(mViewPager.getCurrentItem());
            //此处完形填空是个特例:如果外层是完形填空的话，就以大题为准，不以小题为准,(外层已经处理过)
            if (parentQuestion.getTemplate().equals(QuestionTemplate.CLOZE)) {
                return;
            }
            //这是正常的通用逻辑
            BaseQuestion childQuestion = ((ExerciseBaseFragment) mAdapter.getItem(mViewPager.getCurrentItem())).mBaseQuestion.getChildren().get(position);
            boolean hasAnswered = childQuestion.getHasAnswered();
            if (mDeleteQidsList.contains(childQuestion.getQid())) {
                setBottomButtonState(DELETED);
            } else {
                if (childQuestion.getShowType().equals(QuestionShowType.MISTAKE_ANALYSIS)) {
                    setBottomButtonState(DELETE_ABLE);
                } else {
                    if (childQuestion.getTemplate().equals(QuestionTemplate.ANSWER)||childQuestion.getTemplate().equals(QuestionTemplate.OPERATION)) {
//                        setBottomButtonState(CHECK_ANALYSIS);
                    } else {
                        if (hasAnswered) {
                            setBottomButtonState(SUBMIT_ABLE);
                        } else {
                            setBottomButtonState(SUBMIT_UNABLE);
                        }
                    }
                }

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private OnAnswerStateChangedListener mOnAnswerStateChangedListener = new OnAnswerStateChangedListener() {
        @Override
        public void onAnswerStateChanged(BaseQuestion baseQuestion) {
            //此处肯定是个单题,需要先判断外层是不是复合题
            boolean hasAnswered = true;
            BaseQuestion question = mAdapter.getDatas().get(mViewPager.getCurrentItem());
            if (question.isComplexQuestion()) {
                List<BaseQuestion> children = question.getChildren();
                for (BaseQuestion q : children) {
                    if (q.getTemplate().equals(QuestionTemplate.ANSWER)||q.getTemplate().equals(QuestionTemplate.OPERATION)) {
                        continue;
                    } else {
                        if (!q.getHasAnswered()) {
                            hasAnswered = false;
                            break;
                        }
                    }
                }
            } else {
                hasAnswered = baseQuestion.getHasAnswered();
            }
            if (hasAnswered) {
                setBottomButtonState(SUBMIT_ABLE);
            } else {
                setBottomButtonState(SUBMIT_UNABLE);
            }
        }
    };

    private void setCurrentState(BaseQuestion baseQuestion) {
        boolean hasAnswered = true;
        boolean hasDeleted = false;
        if (baseQuestion.isComplexQuestion()) {
            List<BaseQuestion> children = baseQuestion.getChildren();
            for (BaseQuestion q : children) {
                if (q.getTemplate().equals(QuestionTemplate.ANSWER)||q.getTemplate().equals(QuestionTemplate.OPERATION)) {
                    continue;
                } else {
                    if (!q.getHasAnswered()) {
                        hasAnswered = false;
                        break;
                    }
                }
            }

        } else {
            hasAnswered = baseQuestion.getHasAnswered();
        }
        hasDeleted = mDeletedPos.contains(mViewPager.getCurrentItem());
        if (hasDeleted) {
            setBottomButtonState(DELETED);
        } else {
            if (isAllSubjective(baseQuestion)) {
                setBottomButtonState(DELETE_ABLE);
            } else {
                if (baseQuestion.getShowType().equals(QuestionShowType.MISTAKE_ANALYSIS)) {
                    setBottomButtonState(DELETE_ABLE);
                } else {
                    if (hasAnswered) {
                        setBottomButtonState(SUBMIT_ABLE);
                    } else {
                        setBottomButtonState(SUBMIT_UNABLE);
                    }
                }
            }
        }
    }

    private void showResultCard(boolean isRight){
        mResultCard.setVisibility(View.VISIBLE);
        if(isRight){
            mAnswerResult.setImageResource(R.drawable.answer_right);
        }else {
            mAnswerResult.setImageResource(R.drawable.answer_wrong);
        }
    }

    private void hideResultCard(){
        mResultCard.setVisibility(View.GONE);
    }

    private void bottomBtnClick() {
        BaseQuestion question = mAdapter.getDatas().get(mViewPager.getCurrentItem());
        switch (mBottomBtnState) {
            //可提交
            case SUBMIT_ABLE:
                //TODO 重做答案判断移交sever，暂时不改
                mAnswerManager.start(question);

//                question.setShowType(QuestionShowType.MISTAKE_ANALYSIS);
//                question.setMisTakeRedo(true);
//                boolean isRight = true;
//                if (question.isComplexQuestion()) {
//                    List<BaseQuestion> children = question.getChildren();
//                    for(BaseQuestion child : children){
//                        if(!child.getTemplate().equals(QuestionTemplate.ANSWER)&&!child.getTemplate().equals(QuestionTemplate.OPERATION)){
//                            //主观题的解析 不变 还按照当前的错题重做的界面展示（没变化，都一样）
//                            child.setShowType(QuestionShowType.MISTAKE_ANALYSIS);
//                            child.setMisTakeRedo(true);
//                        }
//                        if(!child.getTemplate().equals(QuestionTemplate.ANSWER)&&!child.getTemplate().equals(QuestionTemplate.OPERATION) && child.getStatus() == Constants.ANSWER_STATUS_WRONG){
//                            isRight = false;
//                        }
//                    }
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    if(!question.getTemplate().equals(QuestionTemplate.ANSWER)&&!question.getTemplate().equals(QuestionTemplate.OPERATION) && question.getStatus() == Constants.ANSWER_STATUS_WRONG){
//                        isRight = false;
//                    }
//                    mAdapter.notifyDataSetChanged();
//                }
//                showResultCard(isRight);
//                setBottomButtonState(DELETE_ABLE);
//                //1秒后隐藏
//                mViewPager.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        hideResultCard();
//                    }
//                },1000);
//                mAnswerManager.start(question);

                break;
            //不可提交
            case SUBMIT_UNABLE:
                break;
            //已删除
            case DELETED:
                break;
            //可删除
            case DELETE_ABLE:
                if (!isOnLoadMore) {
                    //此处需要记录删除的qid 然后在退出时统一删除
                    ToastManager.showMsg(getString(R.string.question_deleted));
                    if (mDeletedPos.contains(mViewPager.getCurrentItem())) {
                        return;
                    } else {
                        mDeletedPos.add(mViewPager.getCurrentItem());
                    }
                    String qid = mAdapter.getQidByPosition(mViewPager.getCurrentItem());
                    if (mQidsToRemove.length() == 0) {
                        mQidsToRemove += qid;
                    } else {
                        mQidsToRemove = mQidsToRemove + "," + qid;
                    }
                    setBottomButtonState(DELETED);
                }
                break;
        }
    }

    private void setBottomButtonState(int state) {
        if (mBottomBtnState == state)
            return;
        mBottomBtnState = state;
        switch (state) {
            //可提交
            case SUBMIT_ABLE:
                btn_submit.setEnabled(true);
                btn_submit.setText(R.string.submit);
                break;
            //不可提交
            case SUBMIT_UNABLE:
                btn_submit.setEnabled(false);
                btn_submit.setText(R.string.submit);
                break;
            //已删除
            case DELETED:
                btn_submit.setEnabled(false);
                btn_submit.setText(R.string.question_deleted);
                break;
            //可删除
            case DELETE_ABLE:
                btn_submit.setEnabled(true);
                btn_submit.setText(R.string.delete_question);
                break;
        }
    }

    private boolean isAllSubjective(BaseQuestion baseQuestion) {
        boolean b = true;
        if (baseQuestion == null) {
            b = false;
            return b;
        }
        if (baseQuestion.isComplexQuestion()) {
            List<BaseQuestion> children = baseQuestion.getChildren();
            for (BaseQuestion child : children) {
                if (!child.getTemplate().equals(QuestionTemplate.ANSWER)&&!child.getTemplate().equals(QuestionTemplate.OPERATION)) {
                    b = false;
                    break;
                }
            }
        } else {
            b = baseQuestion.getTemplate().equals(QuestionTemplate.ANSWER)||baseQuestion.getTemplate().equals(QuestionTemplate.OPERATION);
        }
        return b;
    }

    private void deleteQuestions(String qids) {
        mLoadingView.showLoadingView();
        FinishReDoWorkRequest request = new FinishReDoWorkRequest();
        request.setDeleteWqidList(qids);
        request.startRequest(FinishReDoWorkResponse.class, new EXueELianBaseCallback<FinishReDoWorkResponse>() {
            @Override
            protected void onResponse(RequestBase request, FinishReDoWorkResponse response) {
                mLoadingView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    for (Integer pos : mDeletedPos) {
                        mWrongNum -= 1;
                        mAdapter.deleteItem(pos, mWrongNum);
                    }
                }
                finish();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mLoadingView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
                finish();
            }
        });
    }

    /**
     * load more
     * isFromAnswerCard:是否是由点击答题卡跳转产生的数据加载请求
     */
    private void requestData(String qids, final boolean isFromAnswerCard) {
        if (isFromAnswerCard) {
            mLoadingView.showLoadingView();
        }
        isOnLoadMore = true;
        WrongQByQidsRequest request = new WrongQByQidsRequest();
        request.setSubjectId(mSubjectId);
        request.setQids(qids);
        request.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                if (response.getStatus().getCode() == 0) {
                    Paper paper = new Paper(response.getData().get(0), QuestionShowType.MISTAKE_REDO);
                    mAdapter.addData(paper.getQuestions(), mWrongNum);
                    mNextQidPos = mAdapter.getCount();
                } else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
                isOnLoadMore = false;
                if (isFromAnswerCard) {
                    mLoadingView.hiddenLoadingView();
                    if (mAnswerCardFragment != null) {
                        mFragmentManager.beginTransaction().remove(mAnswerCardFragment).commit();
                    }
                    if (mAnsCardClickPos < mAdapter.getCount()) {
                        FragmentStatePagerAdapter a1 = (FragmentStatePagerAdapter) mViewPager.getAdapter();
                        mViewPager.setCurrentItem(mAnsCardClickPos, false);
                        ExerciseBaseFragment currentFragment = (ExerciseBaseFragment) a1.instantiateItem(mViewPager, mAnsCardClickPos);
                        currentFragment.setUserVisibleHin2(true);
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                isOnLoadMore = false;
                ToastManager.showMsg(error.getMessage());
                if (isFromAnswerCard) {
                    mLoadingView.hiddenLoadingView();
                    if (mAnswerCardFragment != null) {
                        mFragmentManager.beginTransaction().remove(mAnswerCardFragment).commit();
                    }
                }
            }
        });
    }

    private String getQids(int fromPosition, int count) {
        String qids = "";
        if (fromPosition < 0 || fromPosition > mQids.size() - 1 || count < 1) {
            return qids;
        }
        int boundary = fromPosition + count;
        if (boundary > mQids.size() - 1) {
            boundary = mQids.size() - 1;
        }
        for (int i = fromPosition; i <= boundary; i++) {
            String qid = mQids.get(i);
            if (qids.length() == 0) {
                qids += qid;
            } else {
                qids = qids + "," + qid;
            }
        }
        return qids;
    }

    public View getRootView() {
        return mRootView;
    }

    /**
     * 显示答题卡
     */
    private void showAnswerCardFragment() {
        mInputMethodManager.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
        // 可以在这里打个断点，所有Fill Blank的答案均已存入nodes里
        if (mAnswerCardFragment == null) {
            mAnswerCardFragment = new RedoAnswerCardFragment();
            mAnswerCardFragment.setData(mPaper, mTitleString, mQids.size());
            mAnswerCardFragment.setOnCardItemSelectListener(MistakeRedoActivity.this);
        }
        if (mFragmentManager.findFragmentById(R.id.fragment_answercard) == null) {
            mFragmentManager.beginTransaction().add(R.id.fragment_answercard, mAnswerCardFragment).commit();
        }
        controlListenView(true);
    }

    /**
     * 切换下一题目
     */
    public void nextQuestion() {
        BaseQuestion currentQuestion;
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentQuestion = mAdapter.getDatas().get(index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (index < 0 || size < 1 || mViewPager == null || currentQuestion == null)
            return;

        if (currentQuestion.isComplexQuestion()) {
            ViewPager innerViewPager;
            ExerciseBaseFragment currentFramgent = (ExerciseBaseFragment) adapter.instantiateItem(mViewPager, index);
            if(currentFramgent instanceof WrongComplexExerciseBaseFragment){
                WrongComplexExerciseBaseFragment wrongComplexExerciseBaseFragment = (WrongComplexExerciseBaseFragment) currentFramgent;
                innerViewPager = wrongComplexExerciseBaseFragment.getmViewPager();
            }else if(currentFramgent instanceof RedoComplexExerciseBaseFragment){
                RedoComplexExerciseBaseFragment redoComplexExerciseBaseFragment = (RedoComplexExerciseBaseFragment) currentFramgent;
                innerViewPager = redoComplexExerciseBaseFragment.getmViewPager();
            }else {
                return;
            }
            int innerIndex = innerViewPager.getCurrentItem();
            int innerSize = innerViewPager.getAdapter().getCount();

            if (innerIndex < 0 || innerSize < 1)
                return;
            /**
             * 复合题型，切换下一题，共有三种状态：
             * 1.正常切换下一小题
             * 2.处在最后一个小题时，且外部大题不是最后一道题，那么外部大题进入下一题
             * 3.处在最后一个小题，且外部大题也是最后一题，那么判断为是最后一道题，展现答题卡
             */
            if (innerIndex == (innerSize - 1) && index == (size - 1)) {
                //状态3
                showAnswerCardFragment();
            } else if (innerIndex == (innerSize - 1)) {
                //状态2
                mViewPager.setCurrentItem(index + 1);
            } else {
                //状态1
                innerViewPager.setCurrentItem(innerIndex + 1);
            }

        } else{
            if (index == (size - 1)) {
                //最后一题,展示答题卡
                showAnswerCardFragment();
            } else {
                //下一题
                mViewPager.setCurrentItem(index + 1);
            }
        }
    }

    /**
     * 切换上一题目
     */
    public void previousQuestion() {
        BaseQuestion currentQuestion;
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentQuestion = mAdapter.getDatas().get(index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (currentQuestion == null || index < 0 || size < 1 || mViewPager == null )
            return;

        if (currentQuestion.isComplexQuestion()) {
            ViewPager innerViewPager;
            ExerciseBaseFragment currentFramgent = (ExerciseBaseFragment) adapter.instantiateItem(mViewPager, index);
            if(currentFramgent instanceof WrongComplexExerciseBaseFragment){
                WrongComplexExerciseBaseFragment wrongComplexExerciseBaseFragment = (WrongComplexExerciseBaseFragment) currentFramgent;
                innerViewPager = wrongComplexExerciseBaseFragment.getmViewPager();
            }else if(currentFramgent instanceof RedoComplexExerciseBaseFragment){
                RedoComplexExerciseBaseFragment redoComplexExerciseBaseFragment = (RedoComplexExerciseBaseFragment) currentFramgent;
                innerViewPager = redoComplexExerciseBaseFragment.getmViewPager();
            }else {
                return;
            }

            int innerIndex = innerViewPager.getCurrentItem();
            int innerSize = innerViewPager.getAdapter().getCount();
            if ( innerIndex < 0 || innerSize < 1)
                return;

            if (innerIndex >= 1) {
                //小题不是第一题时,有上一小题
                innerViewPager.setCurrentItem(innerIndex - 1);
            } else if (index >= 1) {
                //外部大题不是第一题时,有上一大题
                mViewPager.setCurrentItem(index - 1);
            }

        } else {
            if (index >= 1) {
                //不是第一大题时,有上一题
                mViewPager.setCurrentItem(index - 1);
            }
        }
    }

    public View getOverlayView() {
        return mOverlay;
    }

    /**
     * 当处在第一题和最后一题时，隐藏相应切换题目按钮
     */
    public void hiddenSwitchQuestionView() {
        BaseQuestion currentQuestion;
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentQuestion = mAdapter.getDatas().get(index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (currentQuestion == null || index < 0 || size < 1 || mViewPager == null )
            return;

        if (currentQuestion.isComplexQuestion()) {
            ViewPager innerViewPager;
            ExerciseBaseFragment currentFramgent = (ExerciseBaseFragment) adapter.instantiateItem(mViewPager, index);
            if(currentFramgent instanceof WrongComplexExerciseBaseFragment){
                WrongComplexExerciseBaseFragment wrongComplexExerciseBaseFragment = (WrongComplexExerciseBaseFragment) currentFramgent;
                innerViewPager = wrongComplexExerciseBaseFragment.getmViewPager();
            }else if(currentFramgent instanceof RedoComplexExerciseBaseFragment){
                RedoComplexExerciseBaseFragment redoComplexExerciseBaseFragment = (RedoComplexExerciseBaseFragment) currentFramgent;
                innerViewPager = redoComplexExerciseBaseFragment.getmViewPager();
            }else {
                return;
            }

            int innerIndex = innerViewPager.getCurrentItem();
            int innerSize = innerViewPager.getAdapter().getCount();

            if (innerIndex < 0 || innerSize < 1)
                return;
            /**
             * 复合题型，切换下一题，共有三种状态：
             * 3.处在最后一个小题，且外部大题也是最后一题，那么判断为是最后一道题，展现答题卡
             */
            //最后一题
            if (innerIndex == (innerSize - 1) && index == (size - 1)) { //状态3
//                mNext_text.setText(R.string.complete);
                mNext_text.setText("");
                mNext_question.setVisibility(View.INVISIBLE);
            } else {
                mNext_text.setText(R.string.next_question);
                mNext_question.setVisibility(View.VISIBLE);
            }

            //第一题
            if (innerIndex == 0 && index == 0) {
                mPrevious_question.setVisibility(View.GONE);
            } else {
                mPrevious_question.setVisibility(View.VISIBLE);
            }

        } else {
            //最后一题
            if (index == (size - 1)) {
//                mNext_text.setText(R.string.complete);
                mNext_text.setText("");
                mNext_question.setVisibility(View.INVISIBLE);
            } else {
                mNext_text.setText(R.string.next_question);
                mNext_question.setVisibility(View.VISIBLE);
            }

            if (index == 0) { //第一题
                mPrevious_question.setVisibility(View.GONE);
            } else {
                mPrevious_question.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 答题卡显示或隐藏，回调给fragment，用来控制听力播放控件
     */
    public void controlListenView(boolean answerCardFragmentIsShwon) {
        ExerciseBaseFragment currentFramgent = null;//当前的Fragment
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentFramgent = (ExerciseBaseFragment) adapter.instantiateItem(mViewPager, index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (index < 0 || size < 1 || mViewPager == null || currentFramgent == null)
            return;

        currentFramgent.onAnswerCardVisibleToUser(answerCardFragmentIsShwon);
    }

    public Paper getPaper() {
        return mPaper;
    }


    /**
     * 因口语题需求更改，故通过这个方法来设置当前界面上下题、返回、答题卡等按钮是否可以点击
     */
    public void setCanClick(boolean isCanClick) {
        this.isCanClick = isCanClick;
        mViewPager.setScanScroll(isCanClick);
    }

    @Override
    public void onClick(View v) {
        if (!isCanClick) {
            return;
        }
        switch (v.getId()) {
            case R.id.previous_question:
                previousQuestion();
                break;
            case R.id.next_question:
                nextQuestion();
                break;
            case R.id.backview:
                SpManager.setCompleteQuestionCount(mPaper.getId(), QuestionUtil.calculateCompleteCount(mQuestions));
                if (!TextUtils.isEmpty(mQidsToRemove)) {
                    deleteQuestions(mQidsToRemove);
                }else {
                    finish();
                }
                break;
            case R.id.answercardview:
                showAnswerCardFragment();
                break;
            case R.id.submit:
                bottomBtnClick();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mKeyboardObserver.destroy();
        mOverlay.clearAnimation();
        clearAllSubjectExpandState();
        super.onDestroy();
    }

    private void clearAllSubjectExpandState(){
        for(BaseQuestion question : mAdapter.getDatas()){
            if(question.isComplexQuestion()){
                for(BaseQuestion child : question.getChildren()){
                    if(child instanceof SubjectiveQuestion){
                        ((SubjectiveQuestion)child).setMistakeRedoAnalysisExpand(false);
                    }
                }
            }else {
                if(question instanceof SubjectiveQuestion){
                    ((SubjectiveQuestion)question).setMistakeRedoAnalysisExpand(false);
                }
            }
        }
    }

    public static void LuanchActivity(Context context, String key, String title, String subjectId, String stageId, int wrongNum, ArrayList<String> qids) {
        Intent intent = new Intent(context, MistakeRedoActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        intent.putExtra(TITLE, title);
        intent.putExtra(SUBJECTID, subjectId);
        intent.putExtra(STAGEID, stageId);
        intent.putExtra(WRONG_NUM, wrongNum);
        intent.putStringArrayListExtra(QIDS, qids);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mAnswerCardFragment != null && mAnswerCardFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(mAnswerCardFragment).commit();
            controlListenView(false);
            return;
        }
        SpManager.setCompleteQuestionCount(mPaper.getId(), QuestionUtil.calculateCompleteCount(mQuestions));
        if (!TextUtils.isEmpty(mQidsToRemove)) {
            deleteQuestions(mQidsToRemove);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(BaseQuestion question, int position) {
        //答题卡跳转逻辑 以及分页加载逻辑
        mAnsCardClickPos = position;
        // 2, 跳转
        if (question != null) {
            if (mAnswerCardFragment != null) {
                mFragmentManager.beginTransaction().remove(mAnswerCardFragment).commit();
            }
            FragmentStatePagerAdapter a1 = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            mViewPager.setCurrentItem(position, false);
            ExerciseBaseFragment currentFragment = (ExerciseBaseFragment) a1.instantiateItem(mViewPager, position);
            currentFragment.setUserVisibleHin2(true);
        } else {
            //分页加载数据
            requestData(getQids(mNextQidPos, position + 3), true);
        }

//        ArrayList<Integer> remainPositions = new ArrayList<>(question.getLevelPositions());
//        remainPositions.remove(0);
//        if (remainPositions.size() > 0) { // 表明这层依然是 复合题
//            FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) mViewPager.getAdapter();
//            AnswerComplexExerciseBaseFragment f = (AnswerComplexExerciseBaseFragment) a.instantiateItem(mViewPager, index);
//            f.setChildrenPositionRecursively(remainPositions);
//        }

        controlListenView(false);
    }

    @Override
    public void onCheckAnswerStart() {
        mLoadingView.showLoadingView();
    }

    @Override
    public void onCheckAnswerSuccess(BaseQuestion question,CheckAnswerResponse response) {
        question.setShowType(QuestionShowType.MISTAKE_ANALYSIS);
        question.setMisTakeRedo(true);
        boolean isRight = true;
        if (question.isComplexQuestion()) {
            List<BaseQuestion> children = question.getChildren();
            int i=0;
            for(BaseQuestion child : children){
                if(!child.getTemplate().equals(QuestionTemplate.ANSWER)&&!child.getTemplate().equals(QuestionTemplate.OPERATION)){
                    //主观题的解析 不变 还按照当前的错题重做的界面展示（没变化，都一样）
                    child.setShowType(QuestionShowType.MISTAKE_ANALYSIS);
                    child.setMisTakeRedo(true);
                }
                if(!child.getTemplate().equals(QuestionTemplate.ANSWER)&&!child.getTemplate().equals(QuestionTemplate.OPERATION) && child.getStatus() == Constants.ANSWER_STATUS_WRONG){
                    isRight = false;
                }
                if(response!=null){
                    PadBean padBean=child.getBean().getQuestions().getPad();
                    padBean.setStatus(response.data.get(i).status);
                    padBean.setObjectiveScore(response.data.get(i).objectiveScore);
                    padBean.setAnalysis(response.data.get(i).analysis);
                    //主观题的解析 不变 还按照当前的错题重做的界面展示（没变化，都一样）
                    child.setShowType(QuestionShowType.MISTAKE_ANALYSIS);
                    child.setMisTakeRedo(true);
                    if(padBean.getStatus() != Constants.ANSWER_STATUS_RIGHT){
                        isRight = false;
                    }
                }else {
                    if(!child.getTemplate().equals(QuestionTemplate.ANSWER) && child.getStatus() == Constants.ANSWER_STATUS_WRONG){
                        isRight = false;
                    }
                }
                i++;
            }
            mAdapter.notifyDataSetChanged();
        } else {
            if(!question.getTemplate().equals(QuestionTemplate.ANSWER)&&!question.getTemplate().equals(QuestionTemplate.OPERATION) && question.getStatus() == Constants.ANSWER_STATUS_WRONG){
                isRight = false;
            }
            if(response!=null) {
                PadBean padBean = question.getBean().getQuestions().getPad();
                padBean.setStatus(response.data.get(0).status);
                padBean.setObjectiveScore(response.data.get(0).objectiveScore);
                padBean.setAnalysis(response.data.get(0).analysis);
                if (!question.getTemplate().equals(QuestionTemplate.ANSWER)&&!question.getTemplate().equals(QuestionTemplate.OPERATION) && padBean.getStatus() != Constants.ANSWER_STATUS_RIGHT) {
                    isRight = false;
                }
            }else {
                if(!question.getTemplate().equals(QuestionTemplate.ANSWER) && question.getStatus() == Constants.ANSWER_STATUS_WRONG){
                    isRight = false;
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        showResultCard(isRight);
        setBottomButtonState(DELETE_ABLE);
        //1秒后隐藏
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideResultCard();
            }
        },1000);
    }

    @Override
    public void onCheckAnswerError(String error) {
        ToastManager.showMsg(error);
    }

    @Override
    public void onCheckAnswerEnd() {
        mLoadingView.hiddenLoadingView();
    }
}
