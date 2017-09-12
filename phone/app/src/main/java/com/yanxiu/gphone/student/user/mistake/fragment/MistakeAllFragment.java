package com.yanxiu.gphone.student.user.mistake.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.srt.refresh.EXueELianRefreshLayout;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.user.mistake.activity.MistakeClassifyActivity;
import com.yanxiu.gphone.student.user.mistake.adapter.MistakeAllAdapter;
import com.yanxiu.gphone.student.user.mistake.request.MistakeAllRequest;
import com.yanxiu.gphone.student.user.mistake.request.MistakeDeleteQuestionRequest;
import com.yanxiu.gphone.student.user.mistake.response.MistakeDeleteMessage;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.WrongQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperBean;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ToastManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 10:39.
 * Function :
 */
public class MistakeAllFragment extends MistakeBaseFragment implements MistakeAllAdapter.onItemClickListener, EXueELianRefreshLayout.RefreshListener ,View.OnClickListener{

    private static final String DEFAULT_QID = "0";

    private EXueELianRefreshLayout mRefreshView;
    private MistakeAllAdapter mMistakeAllAdapter;
    private MistakeAllRequest mCompleteRequest;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mistakeall;
    }

    @Override
    protected void initView() {
        RecyclerView completeMistakeView = (RecyclerView) rootView.findViewById(R.id.recy_complete_mistake);
        completeMistakeView.setLayoutManager(new LinearLayoutManager(mContext));
        mMistakeAllAdapter = new MistakeAllAdapter(mContext);
        completeMistakeView.setAdapter(mMistakeAllAdapter);
        mRefreshView = (EXueELianRefreshLayout) rootView.findViewById(R.id.srl_refresh);
    }

    @Override
    protected void listener() {
        mMistakeAllAdapter.addItemClickListener(MistakeAllFragment.this);
        mRefreshView.setRefreshListener(MistakeAllFragment.this);
        rootView.setRetryButtonOnclickListener(MistakeAllFragment.this);
    }

    @Override
    protected void initData() {
        mRefreshView.setLoadMoreEnable(true);
        rootView.showLoadingView();
        onRefresh(null);
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
                    rootView.showLoadingView();
                    onRefresh(null);
                }
            }
        }
    }

    private void requestData(final String currentId) {
        requestCancle();
        mCompleteRequest = new MistakeAllRequest();
        mCompleteRequest.bodyDealer = new DESBodyDealer();
        mCompleteRequest.currentId = currentId;
        mCompleteRequest.stageId = mStageId;
        mCompleteRequest.subjectId = mSubjectId;
        mCompleteRequest.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                rootView.hiddenLoadingView();
                mRefreshView.finishLoadMore();
                mRefreshView.finishRefreshing();
                if (response.getStatus().getCode() == 0) {
                    if (response.getData()!=null&&response.getData().size()>0) {
                        if (currentId.equals("0")) {
                            mMistakeAllAdapter.setData(response.getData().get(0));
                        } else {
                            mMistakeAllAdapter.addData(response.getData().get(0));
                        }
                        if (mWrongNum == mMistakeAllAdapter.getItemCount()) {
                            mMistakeAllAdapter.loadMoreNo();
                            mRefreshView.setLoadMoreEnable(false);
                        } else if (mWrongNum != mMistakeAllAdapter.getItemCount()) {
                            mRefreshView.setLoadMoreEnable(true);
                        }

                    }else {
                        if (currentId.equals("0")) {
                            rootView.showOtherErrorView();
                        }
                    }
                } else {
                    if (currentId.equals("0")){
                        rootView.showNetErrorView();
                        mMistakeAllAdapter.clear();
                    }
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRefreshView.finishLoadMore();
                mRefreshView.finishRefreshing();
                rootView.hiddenLoadingView();
                if (currentId.equals("0")){
                    rootView.showNetErrorView();
                    mMistakeAllAdapter.clear();
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
    public void onRefresh(EXueELianRefreshLayout refreshLayout) {
        requestData(DEFAULT_QID);
    }

    @Override
    public void onLoadMore(EXueELianRefreshLayout refreshLayout) {
        requestData(mMistakeAllAdapter.getLastItemWqid());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retry_button:
                rootView.hiddenNetErrorView();
                rootView.showLoadingView();
                onRefresh(null);
                break;
        }
    }
}
