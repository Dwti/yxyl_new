package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.user.mistake.request.MistakeAllRequest;
import com.yanxiu.gphone.student.user.mistake.request.MistakeDeleteQuestionRequest;
import com.yanxiu.gphone.student.questions.answerframe.adapter.QAWrongViewPagerAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.HomeEventMessage;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongExercisbaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.view.QAViewPager;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ToastManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/18 10:08.
 * Function :
 */
public class WrongQuestionActivity extends YanxiuBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String SUBJECTID = "subjectId";
    private static final String STAGEID = "stageId";
    private static final String WRONG_NUM = "wrongNum";
    private static final String SELECT_INDEX = "select";

    private Context mContext;

    private PublicLoadLayout rootView;
    private LinearLayout mLastQuestionView;
    private LinearLayout mNextQuestionView;
    private ImageView mBackView;
    private ImageView mDeleteView;
    private QAViewPager mQaView;
    private QAWrongViewPagerAdapter mQaAdapter;

    private HomeEventMessage mHomeEventMessage = new HomeEventMessage();
    private HomeKeyEventBroadCastReceiver mHomeKeyEventBroadCastReceiver = new HomeKeyEventBroadCastReceiver();

    private String mStageId;
    private String mSubjectId;
    private int mWrongNum;

    private boolean isOnLoadMore = false;

    private MistakeDeleteQuestionRequest mDeleteQuestionRequest;
    private MistakeAllRequest mCompleteRequest;

    public static void LuanchActivity(Context context, String key, String subjectId, String stageId, int wrongNum, int selectIndex) {
        Intent intent = new Intent(context, WrongQuestionActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        intent.putExtra(SUBJECTID, subjectId);
        intent.putExtra(STAGEID, stageId);
        intent.putExtra(WRONG_NUM, wrongNum);
        intent.putExtra(SELECT_INDEX, selectIndex);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = WrongQuestionActivity.this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_wrongquestion);
        setContentView(rootView);
        initView();
        listener();
        initData();
        registerReceiver(mHomeKeyEventBroadCastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void setRequestCancle() {
        if (mDeleteQuestionRequest != null) {
            mDeleteQuestionRequest.cancelRequest();
            mDeleteQuestionRequest = null;
        }
        if (mCompleteRequest != null) {
            mCompleteRequest.cancelRequest();
            mCompleteRequest = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRequestCancle();
        unregisterReceiver(mHomeKeyEventBroadCastReceiver);
    }

    private class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            EventBus.getDefault().post(mHomeEventMessage);
        }
    }

    private void initView() {
        mLastQuestionView = (LinearLayout) findViewById(R.id.ll_last);
        mNextQuestionView = (LinearLayout) findViewById(R.id.ll_next);
        mBackView = (ImageView) findViewById(R.id.backview);
        mDeleteView = (ImageView) findViewById(R.id.tv_delete);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mQaView = (QAViewPager) findViewById(R.id.vp_viewPager);
        mQaView.setOffscreenPageLimit(1);
        mQaAdapter = new QAWrongViewPagerAdapter(fragmentManager);
        mQaView.setAdapter(mQaAdapter);
    }

    private void listener() {
        mLastQuestionView.setOnClickListener(WrongQuestionActivity.this);
        mNextQuestionView.setOnClickListener(WrongQuestionActivity.this);
        mBackView.setOnClickListener(WrongQuestionActivity.this);
        mDeleteView.setOnClickListener(WrongQuestionActivity.this);
        mQaView.addOnPageChangeListener(WrongQuestionActivity.this);
    }

    private void initData() {
        String key = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        mSubjectId = getIntent().getStringExtra(SUBJECTID);
        mStageId = getIntent().getStringExtra(STAGEID);
        mWrongNum = getIntent().getIntExtra(WRONG_NUM, 0);
        int selectIndex = getIntent().getIntExtra(SELECT_INDEX, 0);
        if (TextUtils.isEmpty(key))
            finish();
        Paper paper = DataFetcher.getInstance().getPaper(key);
        mQaAdapter.setData(paper.getQuestions(), mWrongNum);
        mQaAdapter.setSubjectId(mSubjectId);
        mQaView.setCurrentItem(selectIndex);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_last:
                setLastQuestion();
                break;
            case R.id.ll_next:
                setNextQuestion();
                break;
            case R.id.backview:
                this.finish();
                break;
            case R.id.tv_delete:
                setDeleteQuestion();
                break;
        }
    }

    private void setLastQuestion() {
        int index = mQaView.getCurrentItem();
        WrongExercisbaseFragment currentFramgent = (WrongExercisbaseFragment) mQaAdapter.instantiateItem(mQaView, index);
        if (!currentFramgent.setViewPagerMove(WrongExercisbaseFragment.TYPE_VIEWPAGER_LAST)) {
            try {
                mQaView.setCurrentItem(index - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setNextQuestion() {
        int index = mQaView.getCurrentItem();
        WrongExercisbaseFragment currentFramgent = (WrongExercisbaseFragment) mQaAdapter.instantiateItem(mQaView, index);
        if (!currentFramgent.setViewPagerMove(WrongExercisbaseFragment.TYPE_VIEWPAGER_NEXT)) {
            try {
                mQaView.setCurrentItem(index + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setDeleteQuestion() {
        rootView.showLoadingView();
        final int index = mQaView.getCurrentItem();
        String qid = mQaAdapter.getQidByPosition(index);
        mDeleteQuestionRequest = new MistakeDeleteQuestionRequest();
        mDeleteQuestionRequest.questionId = qid;
        mDeleteQuestionRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                mWrongNum -= 1;
                mQaAdapter.deleteItem(index, mWrongNum);
                if (mWrongNum==0){
                    WrongQuestionActivity.this.finish();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    public void hiddenSwitchQuestionView() {
        if (mWrongNum!=1) {
            int index = mQaView.getCurrentItem();
            WrongExercisbaseFragment currentFramgent = (WrongExercisbaseFragment) mQaAdapter.instantiateItem(mQaView, index);
            if (index == 0) {
                if (currentFramgent.getIsFirst()) {
                    mLastQuestionView.setVisibility(View.GONE);
                    mNextQuestionView.setVisibility(View.VISIBLE);
                    return;
                }
            } else if (index == mWrongNum - 1) {
                if (currentFramgent.getIsEnd()) {
                    mLastQuestionView.setVisibility(View.VISIBLE);
                    mNextQuestionView.setVisibility(View.GONE);
                    return;
                }
            }
            mLastQuestionView.setVisibility(View.VISIBLE);
            mNextQuestionView.setVisibility(View.VISIBLE);
        }else {
            mLastQuestionView.setVisibility(View.GONE);
            mNextQuestionView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int totalNum = mQaAdapter.getCount();
        if (totalNum < mWrongNum) {
            if (mQaView.getCurrentItem() + 1 > totalNum - 3 && !isOnLoadMore) {
                String wqid = mQaAdapter.getLastItemWqid();
                requestData(wqid);
            }
        }
    }

    /**
     * load more
     */
    private void requestData(final String currentId) {
        isOnLoadMore = true;
        mCompleteRequest = new MistakeAllRequest();
        mCompleteRequest.bodyDealer = new DESBodyDealer();
        mCompleteRequest.currentId = currentId;
        mCompleteRequest.stageId = mStageId;
        mCompleteRequest.subjectId = mSubjectId;
        mCompleteRequest.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                isOnLoadMore = false;
                if (response.getStatus().getCode() == 0) {
                    Paper paper = new Paper(response.getData().get(0), QuestionShowType.MISTAKE_ANALYSIS);
                    mQaAdapter.addData(paper.getQuestions(), mWrongNum);
                } else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                isOnLoadMore = false;
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
