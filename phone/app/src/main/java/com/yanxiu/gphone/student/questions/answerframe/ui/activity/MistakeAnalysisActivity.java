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
import com.yanxiu.gphone.student.mistakeredo.request.FinishReDoWorkRequest;
import com.yanxiu.gphone.student.mistakeredo.request.WrongQByQidsRequest;
import com.yanxiu.gphone.student.mistakeredo.response.FinishReDoWorkResponse;
import com.yanxiu.gphone.student.user.mistake.request.MistakeAllRequest;
import com.yanxiu.gphone.student.user.mistake.request.MistakeDeleteQuestionRequest;
import com.yanxiu.gphone.student.questions.answerframe.adapter.QAWrongViewPagerAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.HomeEventMessage;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase.WrongExercisbaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.view.QAViewPager;
import com.yanxiu.gphone.student.user.mistake.response.MistakeDeleteMessage;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/18 10:08.
 * Function :错题解析界面
 */
public class MistakeAnalysisActivity extends YanxiuBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TITLE = "title";
    private static final String SUBJECTID = "subjectId";
    private static final String STAGEID = "stageId";
    private static final String WRONG_NUM = "wrongNum";
    private static final String SELECT_INDEX = "select";
    private static final String QIDS = "qids";

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
    private ArrayList<String> mQids;
    private ArrayList<Integer> mDeletedPos = new ArrayList<>();
    private String mQidsToRemove = "";   //待删除的题

    private int mCurrentPos;
    private int mPageSize = 20;

    private boolean isOnLoadMore = false;
    private MistakeDeleteQuestionRequest mDeleteQuestionRequest;
    private MistakeAllRequest mCompleteRequest;

    public static void LuanchActivity(Context context, String key, String title, String subjectId, String stageId, int wrongNum, int selectIndex, ArrayList<String> qids) {
        Intent intent = new Intent(context, MistakeAnalysisActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        intent.putExtra(TITLE, title);
        intent.putExtra(SUBJECTID, subjectId);
        intent.putExtra(STAGEID, stageId);
        intent.putExtra(WRONG_NUM, wrongNum);
        intent.putExtra(SELECT_INDEX, selectIndex);
        intent.putStringArrayListExtra(QIDS,qids);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MistakeAnalysisActivity.this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_wrongquestion);
        setContentView(rootView);
        EventBus.getDefault().register(mContext);
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
        EventBus.getDefault().unregister(mContext);
        unregisterReceiver(mHomeKeyEventBroadCastReceiver);

    }

    @Override
    public void onBackPressed() {
        if(!TextUtils.isEmpty(mQidsToRemove)){
            deleteQuestions(mQidsToRemove);
        }else {
            super.onBackPressed();
        }
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
        mLastQuestionView.setOnClickListener(MistakeAnalysisActivity.this);
        mNextQuestionView.setOnClickListener(MistakeAnalysisActivity.this);
        mBackView.setOnClickListener(MistakeAnalysisActivity.this);
        mDeleteView.setOnClickListener(MistakeAnalysisActivity.this);
        mQaView.addOnPageChangeListener(MistakeAnalysisActivity.this);
    }

    private void initData() {
        String key = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        mSubjectId = getIntent().getStringExtra(SUBJECTID);
        mStageId = getIntent().getStringExtra(STAGEID);
        mWrongNum = getIntent().getIntExtra(WRONG_NUM, 0);
        int selectIndex = getIntent().getIntExtra(SELECT_INDEX, 0);
        mQids = getIntent().getStringArrayListExtra(QIDS);
        if (TextUtils.isEmpty(key))
            finish();
        Paper paper = DataFetcher.getInstance().getPaper(key);
        mQaAdapter.setData(paper.getQuestions(), mWrongNum);
        mQaAdapter.setSubjectId(mSubjectId);
        mQaView.setCurrentItem(selectIndex);
        mCurrentPos = mQaAdapter.getCount();
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
                if (!isOnLoadMore) {
                    //此处需要记录删除的qid 然后在退出时统一删除
                    ToastManager.showMsg(getString(R.string.question_deleted));
                    String qid = mQaAdapter.getQidByPosition(mQaView.getCurrentItem());
                    if(mQidsToRemove.length() == 0){
                        mQidsToRemove += qid;
                    }else {
                        mQidsToRemove = mQidsToRemove + "," + qid;
                    }

                    mDeletedPos.add(mQaView.getCurrentItem());
//                    setDeleteQuestion();
                }
                break;
        }
    }

    public void onEventMainThread(MistakeDeleteMessage message) {
        if (message == null) {
            return;
        }
        int totalNum = mQaAdapter.getCount();
        if (totalNum < mWrongNum) {
            if (mQaView.getCurrentItem() + 1 > totalNum - 3 && !isOnLoadMore) {
                requestData();
            }
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
        if (TextUtils.isEmpty(qid)) {
            rootView.hiddenLoadingView();
            return;
        }
        mDeleteQuestionRequest = new MistakeDeleteQuestionRequest();
        mDeleteQuestionRequest.questionId = qid;
        mDeleteQuestionRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                mWrongNum -= 1;
                mQaAdapter.deleteItem(index, mWrongNum);
                if (mWrongNum == 0) {
                    MistakeAnalysisActivity.this.finish();
                }
                rootView.hiddenLoadingView();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    public void hiddenSwitchQuestionView() {
        if (mWrongNum != 1) {
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
        } else {
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
                requestData();
            }
        }
    }

    /**
     * load more
     */
    private void requestData() {
        isOnLoadMore = true;
        WrongQByQidsRequest request = new WrongQByQidsRequest();
        request.setSubjectId(mSubjectId);
        request.setQids(getQids());
        request.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                if (response.getStatus().getCode() == 0) {
                    String qids = ((WrongQByQidsRequest)request).getQids();
                    int count = qids.split(",").length;
                    mCurrentPos += count;
                    Paper paper = new Paper(response.getData().get(0), QuestionShowType.MISTAKE_ANALYSIS);
                    mQaAdapter.addData(paper.getQuestions(), mWrongNum);
                } else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
                isOnLoadMore = false;
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                isOnLoadMore = false;
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    private void deleteQuestions(String qids){
        rootView.showLoadingView();
        FinishReDoWorkRequest request = new FinishReDoWorkRequest();
        request.setDeleteWqidList(qids);
        request.startRequest(FinishReDoWorkResponse.class, new EXueELianBaseCallback<FinishReDoWorkResponse>() {
            @Override
            protected void onResponse(RequestBase request, FinishReDoWorkResponse response) {
                rootView.hiddenLoadingView();
                if(response.getStatus().getCode() == 0){
                    for(Integer pos : mDeletedPos){
                        mWrongNum -= 1;
                        mQaAdapter.deleteItem(pos, mWrongNum);
                    }
                }
                finish();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
                finish();
            }
        });
    }

    private String getQids(){
        String qids = "";
        List<String> result;
        if(mCurrentPos == mQids.size() - 1){
            return mQids.get(mCurrentPos);
        }
        result = mQids.size() > (mCurrentPos + mPageSize ) ? mQids.subList(mCurrentPos,mCurrentPos + mPageSize) : mQids.subList(mCurrentPos,mQids.size());
        for(String qid : result){
            if(qids.length() == 0){
                qids += qid;
            }else {
                qids = qids + "," + qid;
            }
        }
        return qids;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
