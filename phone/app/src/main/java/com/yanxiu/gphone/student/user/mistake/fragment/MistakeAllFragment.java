package com.yanxiu.gphone.student.user.mistake.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.srt.refresh.EXueELianRefreshLayout;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.BuildConfig;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.mistakeredo.MistakeRedoActivity;
import com.yanxiu.gphone.student.mistakeredo.request.WrongQByQidsRequest;
import com.yanxiu.gphone.student.user.mistake.activity.MistakeListActivity;
import com.yanxiu.gphone.student.user.mistake.adapter.MistakeAllAdapter;
import com.yanxiu.gphone.student.user.mistake.request.MistakeAllRequest;
import com.yanxiu.gphone.student.user.mistake.request.MistakeDeleteQuestionRequest;
import com.yanxiu.gphone.student.user.mistake.response.MistakeDeleteMessage;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.MistakeAnalysisActivity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperBean;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

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
    private int mCurrentPos = 0;
    private int mPageSize = 10;

    private boolean mEnterAnalysis = false; //进入解析之后，又可能删除题目，所以回来的时候需要刷新界面

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
                ((MistakeListActivity)mContext).finish();
            }else {
                mMistakeAllAdapter.deleteItem(message.position, message.questionId);
                if (mMistakeAllAdapter.getItemCount() == 0) {
                    rootView.showLoadingView();
                    onRefresh(null);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(mEnterAnalysis){
//            getWrongQByQids(true);
//            mEnterAnalysis = false;
//        }
    }

    private void getWrongQByQids(final boolean isRefresh){
        WrongQByQidsRequest request = new WrongQByQidsRequest();
        request.setSubjectId(mSubjectId);
        request.setQids(getQids(isRefresh));
        request.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                rootView.hiddenLoadingView();
                mRefreshView.finishLoadMore();
                mRefreshView.finishRefreshing();
                if (response.getStatus().getCode() == 0) {
                    if (response.getData()!=null&&response.getData().size()>0) {
                        String qids = ((WrongQByQidsRequest)request).getQids();
                        int count = qids.split(",").length;
                        mCurrentPos += count;
                        if (isRefresh) {
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
                        if (isRefresh) {
                            rootView.showOtherErrorView();
                        }
                    }
                } else {
                    if (isRefresh){
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
                if (isRefresh){
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
                    mQids.remove(deleteMessage.questionId);
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

    private String getQids(boolean isRefresh){
        String qids = "";
        List<String> result;
        if(isRefresh){
            mCurrentPos = 0;
        }
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
        Log.i("qids",qids);
        return qids;
    }

    @Override
    public void onItemClick(View view, PaperBean paperBean, int position) {
        Paper paper = new Paper(paperBean, QuestionShowType.MISTAKE_ANALYSIS);
        DataFetcher.getInstance().save(paper.getId(), paper);
        MistakeAnalysisActivity.LuanchActivity(mContext, paper.getId(), mTitle, mSubjectId, mStageId, mWrongNum, position,mQids);
        mEnterAnalysis = true;
    }

    private void openMistakeRedoUI(PaperBean paperBean){
        Paper paper = new Paper(paperBean, QuestionShowType.MISTAKE_REDO);
        DataFetcher.getInstance().save(paper.getId(), paper);
        MistakeRedoActivity.LuanchActivity(mContext, paper.getId(),mTitle, mSubjectId, mStageId, mWrongNum,mQids);
        mEnterAnalysis = true;
    }

    @Override
    public void onRefresh(EXueELianRefreshLayout refreshLayout) {
        getWrongQByQids(true);
    }

    @Override
    public void onLoadMore(EXueELianRefreshLayout refreshLayout) {
        getWrongQByQids(false);
    }

    public void redoMistake(){
        if(mMistakeAllAdapter.getPaperBean() != null){
            openMistakeRedoUI(mMistakeAllAdapter.getPaperBean());
        }
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
