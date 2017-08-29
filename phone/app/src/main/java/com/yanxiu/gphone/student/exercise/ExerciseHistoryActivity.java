package com.yanxiu.gphone.student.exercise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srt.refresh.EXueELianRefreshLayout;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.ChapterSwitchBar;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.customviews.PickerViewEx;
import com.yanxiu.gphone.student.exercise.adapter.PopListAdapter;
import com.yanxiu.gphone.student.exercise.bean.EditionBeanEx;
import com.yanxiu.gphone.student.exercise.bean.EditionChildBean;
import com.yanxiu.gphone.student.exercise.bean.ExerciseBean;
import com.yanxiu.gphone.student.exercise.request.EditionRequest;
import com.yanxiu.gphone.student.exercise.request.ExerciseHistoryByChapterRequest;
import com.yanxiu.gphone.student.exercise.request.ExerciseHistoryByKnowRequest;
import com.yanxiu.gphone.student.exercise.response.EditionResponse;
import com.yanxiu.gphone.student.exercise.response.ExerciseHistoryResponse;
import com.yanxiu.gphone.student.homework.request.HomeworkReportRequest;
import com.yanxiu.gphone.student.homework.request.PaperRequest;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerReportActicity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-8-2.
 */

public class ExerciseHistoryActivity extends YanxiuBaseActivity {

    public static final String EXTRA_SUBJECT_ID = "SUBJECT_ID";
    public static final String EXTRA_SUBJECT_NAME = "SUBJECT_NAME";
    public static final String EXTRA_EDITION_ID = "EDITION_ID";

    private List<ExerciseBean> mExerciseListChapter = new ArrayList<>();
    private List<ExerciseBean> mExerciseListKnow = new ArrayList<>();
    private ExerciseAdapter mChapterAdapter,mKnowAdapter;
    private RecyclerView mRecyclerView;
    private EXueELianRefreshLayout mRefreshLayout;
    private LoadingView mLoadingView;
    private View mRootView,mTipsView, mBack,mLayoutStage,mToolBar;
    private String mSubjectId,mEditionId,mVolume;
    private TextView mTitle, mTips,mStage;
    private ImageView mTipsImg;
    private Button mRefreshBtn;
    private List<EditionChildBean> mEditionChildBeanList;
    private ChapterSwitchBar mSwitchBar;
    private PopupWindow popupWindow;
    private int mLastSelectedPos = 0;
    private int mCurrSelectedPos = 0;
    private int mChapterCurrentPage =1;
    private int mKnowCurrentPage = 1;
    private int mChapterTotalPage = 0;
    private int mKnowTotalPage = 0;
    private boolean mIsChapterMode = true;  //当前选中的是否是章节
    private boolean mNoEditions = true; //第一次进来是否请求Editions数据失败
    private boolean mEnteredAnswerQueUI = false;


    public static void invoke(Context context,String subjectId, String subjectName,String editionId){
        Intent intent = new Intent(context,ExerciseHistoryActivity.class);
        intent.putExtra(EXTRA_SUBJECT_ID,subjectId);
        intent.putExtra(EXTRA_SUBJECT_NAME,subjectName);
        intent.putExtra(EXTRA_EDITION_ID,editionId);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mEnteredAnswerQueUI){
            loadData();
        }
    }

    private void initView(){
        mRootView = findViewById(R.id.root);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = findViewById(R.id.iv_back);
        mToolBar = findViewById(R.id.rl_tool_bar);
        mSwitchBar = (ChapterSwitchBar) findViewById(R.id.switchBar);
        mLayoutStage = findViewById(R.id.ll_stage);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRefreshLayout = (EXueELianRefreshLayout) findViewById(R.id.refreshLayout);
        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mTipsView = findViewById(R.id.tips_layout);
        mTipsImg = (ImageView) findViewById(R.id.iv_tips);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mStage = (TextView) findViewById(R.id.tv_stage);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);

        mChapterAdapter = new ExerciseAdapter(mExerciseListChapter,mItemClickListener);
        mKnowAdapter = new ExerciseAdapter(mExerciseListKnow,mItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mChapterAdapter);

        mRefreshLayout.setLoadMoreEnable(true);

    }


    private void initData() {
        mSubjectId = getIntent().getStringExtra(EXTRA_SUBJECT_ID);
        String subjectName = getIntent().getStringExtra(EXTRA_SUBJECT_NAME);
        mEditionId = getIntent().getStringExtra(EXTRA_EDITION_ID);
        mTitle.setText(subjectName);

        setSwitchBarVisibility(mSubjectId);

        getEditionList(mSubjectId);
    }

    private void initListener(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishUI();
            }
        });

        mSwitchBar.setOnCheckedChangedListener(new ChapterSwitchBar.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(boolean isOff) {
                if(isOff){
                    mIsChapterMode = true;
                    showContentView();
                    mLayoutStage.setVisibility(View.VISIBLE);
                    mRecyclerView.setAdapter(mChapterAdapter);
                    setLoadMoreEnable(canLoadMore(mIsChapterMode));
                    if(mChapterAdapter.getItemCount() == 0){
                        if(mEditionChildBeanList == null || mEditionChildBeanList.size() == 0){
                            getEditionList(mSubjectId);
                        }else {
                            getExercisesByChapter(1);
                        }
                    }
                }else {
                    mIsChapterMode = false;
                    showContentView();
                    mLayoutStage.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(mKnowAdapter);
                    setLoadMoreEnable(canLoadMore(mIsChapterMode));
                    if(mKnowAdapter.getItemCount() == 0){
                        getExercisesByKnow(1);
                    }
                }
            }
        });

        mLayoutStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });

        mRefreshLayout.setRefreshListener(new EXueELianRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh(EXueELianRefreshLayout refreshLayout) {
                loadData();
            }

            @Override
            public void onLoadMore(EXueELianRefreshLayout refreshLayout) {
                loadMoreData();
            }
        });
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mLoadingView.showLoadingView();
                if(mNoEditions){
                    getEditionList(mSubjectId);
                }else {
                    loadData();
                }
            }
        });
    }

    private void getEditionList(String subjectId){
        EditionRequest request = new EditionRequest();
        request.setSubjectId(subjectId);
        request.startRequest(EditionResponse.class,mEditionCallback);
    }

    private void getExercisesByChapter(int nextPage){
        mChapterCurrentPage = nextPage;
        ExerciseHistoryByChapterRequest request = new ExerciseHistoryByChapterRequest();
        request.setSubjectId(mSubjectId);
        request.setBeditionId(mEditionId);
        request.setVolume(mVolume);
        request.setNextPage(String.valueOf(nextPage));
        request.startRequest(ExerciseHistoryResponse.class,mExerciseByChapter);
    }

    private void getExercisesByKnow(int nextPage){
        mKnowCurrentPage = nextPage;
        ExerciseHistoryByKnowRequest request = new ExerciseHistoryByKnowRequest();
        request.setSubjectId(mSubjectId);
        request.setNextPage(String.valueOf(nextPage));
        request.startRequest(ExerciseHistoryResponse.class,mExerciseByKnow);
    }

    private void getReport(String paperId){
        HomeworkReportRequest request = new HomeworkReportRequest();
        request.setPpid(paperId);
        request.bodyDealer = new DESBodyDealer();
        request.startRequest(PaperResponse.class,mReportCallback);
    }

    private void getPaper(String paperId){
        PaperRequest request = new PaperRequest();
        request.setPaperId(paperId);
        request.bodyDealer = new DESBodyDealer();
        request.startRequest(PaperResponse.class,mPaperCallback);
    }

    private String getVolume(List<EditionBeanEx> list, String editionId){
        String volume = "";
        for(EditionBeanEx bean : list){
            if(editionId.equals(bean.getId())){
                mEditionChildBeanList = bean.getChildren();
                if(bean.getChildren() != null && bean.getChildren().size() > 0){
                    volume = bean.getChildren().get(0).getId();
                    mStage.setText(bean.getChildren().get(0).getName());
                }
                break;
            }
        }
        return volume;
    }

    private void loadData(){
        if(mIsChapterMode){
            getExercisesByChapter(1);
        }else {
            getExercisesByKnow(1);
        }
    }

    private void loadMoreData(){
        if(!canLoadMore(mIsChapterMode)){
            mRefreshLayout.finishLoadMore();
            setLoadMoreEnable(false);
            return;
        }
        if(mIsChapterMode){
            mChapterCurrentPage++;
            getExercisesByChapter(mChapterCurrentPage);
        }else {
            mKnowCurrentPage++;
            getExercisesByKnow(mKnowCurrentPage);
        }
    }

    public void setLoadingIndicator(boolean active) {
        if(!active){
            mLoadingView.hiddenLoadingView();
            mRefreshLayout.finishRefreshing();
        }
    }

    public void setLoadMoreEnable(boolean enable){
        mRefreshLayout.setLoadMoreEnable(enable);
    }

    public void setLoadingMoreIndicator(boolean active) {
        if(!active){
            mRefreshLayout.finishLoadMore();
        }
    }

    private boolean canLoadMore(boolean isChapterMode){
        if(isChapterMode){
            return mChapterCurrentPage < mChapterTotalPage;
        }else {
            return  mKnowCurrentPage < mKnowTotalPage;
        }
    }

    public void updateExercises(ExerciseAdapter adapter) {
        showContentView();
        adapter.notifyDataSetChanged();
    }

    private void showPop(){
        if(mEditionChildBeanList == null || mEditionChildBeanList.size() == 0)
            return;
        if(popupWindow == null){
            View view = LayoutInflater.from(this).inflate(R.layout.popwindow_stage,null);
            popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setAnimationStyle(R.style.pop_anim);

            final PickerViewEx picker = (PickerViewEx) view.findViewById(R.id.picker_view);
            picker.setTextLocation(PickerViewEx.DEFAULT_CENTER);
            View tvOk = view.findViewById(R.id.tv_ok);
            View tvCancel = view.findViewById(R.id.tv_cancel);
            picker.setData(getEditionStrs(mEditionChildBeanList));
            picker.setSelected(0);
            mLastSelectedPos = 0;
            mCurrSelectedPos = 0;

            picker.setOnSelectListener(new PickerViewEx.onSelectListener() {
                @Override
                public void onSelect(View view, String text, int selectId) {
                    mCurrSelectedPos = selectId;
                }
            });

            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mLastSelectedPos != mCurrSelectedPos){
                        mLastSelectedPos = mCurrSelectedPos;
                        mVolume = mEditionChildBeanList.get(mCurrSelectedPos).getId();
                        mStage.setText(mEditionChildBeanList.get(mCurrSelectedPos).getName());
                        dismissPop();
                        getExercisesByChapter(1);
                    }else {
                        dismissPop();
                    }
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPop();
                }
            });

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mCurrSelectedPos = mLastSelectedPos;
                    picker.setSelected(mCurrSelectedPos);
                }
            });

        }
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    private void dismissPop(){
        if(popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    private List<String> getEditionStrs(List<EditionChildBean> list){
        List<String> result = new ArrayList<>();
        for(EditionChildBean bean : list){
            result.add(bean.getName());
        }
        return result;
    }

    public void openAnswerQuestionUI(String key) {
        mEnteredAnswerQueUI = true;
        AnswerQuestionActivity.invoke(this,key, Constants.MAINAVTIVITY_FROMTYPE_EXERCISE_HISTORY,null);
    }

    public void openAnswerReportUI(String key) {
        AnswerReportActicity.invoke(this,key);
    }

    public void showNoMoreData() {
        showMsg(getString(R.string.no_more_data));
    }

    public void showMsg(String msg) {
        ToastManager.showMsg(msg);
    }

    public void showLoadMoreDataError(String msg) {
        showMsg(msg);
    }

    public boolean isActive() {
        return !(isDestroyed() || isFinishing());
    }

    public void finishUI() {
        finish();
    }

    private void showContentView(){
        mToolBar.setVisibility(View.VISIBLE);
        mRefreshLayout.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
    }

    private void showDataEmptyView(boolean hideToolBar){
        if(hideToolBar){
            mToolBar.setVisibility(View.GONE);
        }
        mRefreshLayout.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.no_exercise_history);
        mTips.setText(R.string.no_exercise);
        mRefreshBtn.setText(R.string.click_to_refresh);
    }

    private void showDataErrorView(boolean hideToolBar){
        if(hideToolBar){
            mToolBar.setVisibility(View.GONE);
        }
        mRefreshLayout.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void setSwitchBarVisibility(String subjectId) {
        switch (subjectId){
            //非数理化生科目，不展示章节知识点切换（只有章节）
            case "1102":
            case "1104":
            case "1108":
            case "1109":
            case "1110":
                mSwitchBar.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLayoutStage.getLayoutParams();
                params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                mLayoutStage.setLayoutParams(params);
                break;
            default:
                break;
        }
    }

    ExerciseAdapter.ExerciseItemClickListener mItemClickListener = new ExerciseAdapter.ExerciseItemClickListener() {
        @Override
        public void onHomeworkClick(ExerciseBean bean) {
            if(2 == bean.getStatus()){
                getReport(bean.getPaperId());
            }else {
                getPaper(bean.getPaperId());
            }
        }

        @Override
        public void onLoadMoreClick() {
            if(!((ExerciseAdapter)mRecyclerView.getAdapter()).isLoadingMore()){
                loadMoreData();
            }
        }
    };

    HttpCallback<ExerciseHistoryResponse> mExerciseByChapter = new EXueELianBaseCallback<ExerciseHistoryResponse>() {
        @Override
        protected void onResponse(RequestBase request, ExerciseHistoryResponse response) {
            if(response.getStatus().getCode() == 0){
                if(mChapterCurrentPage == 1){
                    mEnteredAnswerQueUI = false;
                    setLoadingIndicator(false);
                    setLoadMoreEnable(true);
                    mChapterTotalPage = response.getPage().getTotalPage();
                    if(response.getData() != null && response.getData().size() > 0){
                        mExerciseListChapter.clear();
                        mExerciseListChapter.addAll(response.getData());
                        updateExercises(mChapterAdapter);
                    }else {
                        mChapterAdapter.clearData();
                        showDataEmptyView(false);
                    }
                }else if(mChapterCurrentPage > 1){
                    setLoadingMoreIndicator(false);
                    mChapterTotalPage = response.getPage().getTotalPage();
                    if(response.getData() != null && response.getData().size() > 0){
                        mExerciseListChapter.addAll(response.getData());
                        updateExercises(mChapterAdapter);
                    }else {
                        showNoMoreData();
                    }
                }

            }else {
                if(mChapterCurrentPage == 1){
                    setLoadingIndicator(false);
                    mChapterAdapter.clearData();
                    showDataEmptyView(false);
                }else if(mChapterCurrentPage > 1){
                    setLoadingMoreIndicator(false);
                    showLoadMoreDataError(response.getStatus().getDesc());
                }
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            if(mChapterCurrentPage == 1){
                setLoadingIndicator(false);
                mChapterAdapter.clearData();
                showDataErrorView(false);
            }else if(mChapterCurrentPage > 1){
                setLoadingMoreIndicator(false);
                showLoadMoreDataError(error.getLocalizedMessage());
            }
        }
    };

    HttpCallback<ExerciseHistoryResponse> mExerciseByKnow = new EXueELianBaseCallback<ExerciseHistoryResponse>() {
        @Override
        protected void onResponse(RequestBase request, ExerciseHistoryResponse response) {
            if(response.getStatus().getCode() == 0){
                if(mKnowCurrentPage == 1){
                    mEnteredAnswerQueUI = false;
                    setLoadingIndicator(false);
                    setLoadMoreEnable(true);
                    mKnowTotalPage = response.getPage().getTotalPage();
                    if(response.getData() != null && response.getData().size() > 0){
                        mExerciseListKnow.clear();
                        mExerciseListKnow.addAll(response.getData());
                        updateExercises(mKnowAdapter);
                    }else {
                        mKnowAdapter.clearData();
                        showDataEmptyView(false);
                    }
                }else if(mKnowCurrentPage > 1){
                    setLoadingMoreIndicator(false);
                    mKnowTotalPage = response.getPage().getTotalPage();
                    if(response.getData() != null && response.getData().size() > 0){
                        mExerciseListKnow.addAll(response.getData());
                        updateExercises(mKnowAdapter);
                    }else {
                        showNoMoreData();
                    }
                }

            }else {
                if(mKnowCurrentPage == 1){
                    setLoadingIndicator(false);
                    mKnowAdapter.clearData();
                    showDataEmptyView(false);
                }else if(mKnowCurrentPage > 1){
                    setLoadingMoreIndicator(false);
                    showLoadMoreDataError(response.getStatus().getDesc());
                }
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            if(mKnowCurrentPage == 1){
                setLoadingIndicator(false);
                mKnowAdapter.clearData();
                showDataErrorView(false);
            }else if(mKnowCurrentPage > 1){
                setLoadingMoreIndicator(false);
                showLoadMoreDataError(error.getLocalizedMessage());
            }
        }
    };

    HttpCallback<PaperResponse> mReportCallback = new HttpCallback<PaperResponse>() {
        @Override
        public void onSuccess(RequestBase request, PaperResponse ret) {
            if(ret.getStatus().getCode() == 0){
                if(ret.getData().size() > 0){
                    Paper paper = new Paper(ret.getData().get(0), QuestionShowType.ANALYSIS);
                    DataFetcher.getInstance().save(paper.getId(),paper);
                    openAnswerReportUI(paper.getId());
                }else {
                    showMsg(ret.getStatus().getDesc());
                }
            }else {
                showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showMsg(error.getLocalizedMessage());
        }
    };

    HttpCallback<PaperResponse> mPaperCallback = new HttpCallback<PaperResponse>() {
        @Override
        public void onSuccess(RequestBase request, PaperResponse ret) {
            if(ret.getStatus().getCode() == 0){
                if(ret.getData().size() > 0){
                    QuestionShowType type = QuestionShowType.ANSWER;
                    Paper paper = new Paper(ret.getData().get(0), type);
                    DataFetcher.getInstance().save(paper.getId(),paper);
                    openAnswerQuestionUI(paper.getId());
                }else {
                    showMsg(ret.getStatus().getDesc());
                }
            }else {
                showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showMsg(error.getLocalizedMessage());
        }
    };

    HttpCallback<EditionResponse> mEditionCallback = new EXueELianBaseCallback<EditionResponse>() {
        @Override
        protected void onResponse(RequestBase request, EditionResponse response) {
            if(response.getStatus().getCode() == 0){
                mNoEditions = false;
                mVolume = getVolume(response.getData(),mEditionId);
                getExercisesByChapter(1);
            }else {
                showDataErrorView(true);
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView(true);
        }
    };
}
