package com.yanxiu.gphone.student.mistake.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.mistake.adapter.MistakeAllAdapter;
import com.yanxiu.gphone.student.mistake.request.MistakeAllRequest;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 10:39.
 * Function :
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class MistakeAllFragment extends MistakeBaseFragment implements MistakeAllAdapter.onItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnScrollChangeListener {

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
    }

    @Override
    protected void listener() {
        mCompleteMistakeView.setOnScrollChangeListener(MistakeAllFragment.this);
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
        requestData("0");
    }

    @Override
    protected void requestCancle() {
        if (mCompleteRequest != null) {
            mCompleteRequest.cancelRequest();
            mCompleteRequest = null;
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
                        mMistakeAllAdapter.setData(response.getData().get(0).getPaperTest());
                    } else {
                        mMistakeAllAdapter.addData(response.getData().get(0).getPaperTest());
                    }
                    if (mWrongNum == mMistakeAllAdapter.getItemCount()) {
                        isShouldLoadMore = false;
                    }else if (mWrongNum != mMistakeAllAdapter.getItemCount()){
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

    @Override
    public void onItemClick(View view, List<PaperTestBean> paperTestBeanList, int position) {

    }

    @Override
    public void onRefresh() {
        requestData("0");
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (!isRequest && isShouldLoadMore) {
            int lastItemPosition = ((LinearLayoutManager) mCompleteMistakeView.getLayoutManager()).findLastVisibleItemPosition();
            if (lastItemPosition + 1 == mMistakeAllAdapter.getItemCount()) {
                requestData(mMistakeAllAdapter.getLastItemWqid());
            }
        }
    }
}
