package com.yanxiu.gphone.student.mistake.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.mistake.activity.MistakeClassifyActivity;
import com.yanxiu.gphone.student.mistake.adapter.MistakeAllAdapter;
import com.yanxiu.gphone.student.mistake.request.MistakeAllRequest;
import com.yanxiu.gphone.student.mistake.request.MistakeDeleteQuestionRequest;
import com.yanxiu.gphone.student.mistake.response.MistakeDeleteMessage;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.WrongQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperBean;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 10:39.
 * Function :
 */
public class MistakeAllFragment extends MistakeBaseFragment implements MistakeAllAdapter.onItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String DEFAULT_QID = "0";

    private SwipeRefreshLayout mRefreshView;
    private RecyclerView mCompleteMistakeView;
    private MistakeAllAdapter mMistakeAllAdapter;
    private MistakeAllRequest mCompleteRequest;
    private boolean isRequest = false;
    private boolean isShouldLoadMore = true;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mistakeall;
    }

    @Override
    protected void initView() {
        mCompleteMistakeView = (RecyclerView) rootView.findViewById(R.id.recy_complete_mistake);
        mCompleteMistakeView.setLayoutManager(new LinearLayoutManager(mContext));
        mMistakeAllAdapter = new MistakeAllAdapter(mContext);
        mCompleteMistakeView.setAdapter(mMistakeAllAdapter);
        mRefreshView = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);
        mRefreshView.setSize(SwipeRefreshLayout.DEFAULT);
        mRefreshView.setProgressViewOffset(true, mRefreshView.getProgressViewStartOffset(), ScreenUtils.dpToPxInt(mContext,30));
    }

    @Override
    protected void listener() {
        mCompleteMistakeView.addOnScrollListener(mScrollListener);
        mMistakeAllAdapter.addItemClickListener(MistakeAllFragment.this);
        mRefreshView.setOnRefreshListener(MistakeAllFragment.this);
    }

    @Override
    protected void initData() {
        mRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshView.setRefreshing(true);
            }
        });
        requestData(DEFAULT_QID);
    }

    @Override
    protected void requestCancle() {
        if (mCompleteRequest != null) {
            mCompleteRequest.cancelRequest();
            mCompleteRequest = null;
        }
    }

    @Override
    protected void onDeleteItem(MistakeDeleteMessage message) {
        if (message != null) {
            this.mWrongNum=message.wrongNum;
            if (mWrongNum==0){
                ((MistakeClassifyActivity)mContext).finish();
            }else {
                mMistakeAllAdapter.deleteItem(message.position, message.questionId);
                if (mMistakeAllAdapter.getItemCount() == 0) {
                    mRefreshView.setRefreshing(true);
                    requestData(DEFAULT_QID);
                }
            }
        }
    }

    private void requestData(final String currentId) {
        isRequest = true;
        requestCancle();
        mCompleteRequest = new MistakeAllRequest();
        mCompleteRequest.bodyDealer = new DESBodyDealer();
        mCompleteRequest.currentId = currentId;
        mCompleteRequest.stageId = mStageId;
        mCompleteRequest.subjectId = mSubjectId;
        mCompleteRequest.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                isRequest = false;
                if (mRefreshView.isRefreshing()) {
                    mRefreshView.setRefreshing(false);
                }
                if (response.getStatus().getCode() == 0) {
                    if (currentId.equals("0")) {
                        mMistakeAllAdapter.setData(response.getData().get(0));
                    } else {
                        mMistakeAllAdapter.addData(response.getData().get(0));
                    }
                    if (mWrongNum == mMistakeAllAdapter.getItemCount()) {
                        isShouldLoadMore = false;
                    } else if (mWrongNum != mMistakeAllAdapter.getItemCount()) {
                        isShouldLoadMore = true;
                    }
                } else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                isRequest = false;
                if (mRefreshView.isRefreshing()) {
                    mRefreshView.setRefreshing(false);
                }
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    public void onEventMainThread(MistakeAllAdapter.OnItemDelete itemDelete){
        if (itemDelete!=null){
            setDeleteItem(itemDelete);
        }
    }

    private void setDeleteItem(final MistakeAllAdapter.OnItemDelete itemDelete){
        rootView.showLoadingView();
        MistakeDeleteQuestionRequest deleteQuestionRequest=new MistakeDeleteQuestionRequest();
        deleteQuestionRequest.questionId=itemDelete.questionId;
        deleteQuestionRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0){
                    mWrongNum-=1;
                    MistakeDeleteMessage deleteMessage=new MistakeDeleteMessage();
                    deleteMessage.wrongNum=mWrongNum;
                    deleteMessage.position=itemDelete.position;
                    deleteMessage.questionId=itemDelete.questionId;
                    deleteMessage.subjectId=mSubjectId;
                    EventBus.getDefault().post(deleteMessage);
                }else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(View view, PaperBean paperBean, int position) {
        Paper paper = new Paper(paperBean, QuestionShowType.MISTAKE_ANALYSIS);
        DataFetcher.getInstance().save(paper.getId(), paper);
        WrongQuestionActivity.LuanchActivity(mContext, paper.getId(), mSubjectId, mStageId, mWrongNum, position);
    }

    @Override
    public void onRefresh() {
        requestData(DEFAULT_QID);
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!isRequest && isShouldLoadMore) {
                int lastItemPosition = ((LinearLayoutManager) mCompleteMistakeView.getLayoutManager()).findLastVisibleItemPosition();
                if (lastItemPosition + 1 == mMistakeAllAdapter.getItemCount()) {
                    requestData(mMistakeAllAdapter.getLastItemWqid());
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

}
