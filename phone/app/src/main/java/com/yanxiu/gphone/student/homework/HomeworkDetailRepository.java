package com.yanxiu.gphone.student.homework;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.ExerciseBaseCallback;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailBean;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailRequest;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailResponse;
import com.yanxiu.gphone.student.homework.data.PaperRequest;
import com.yanxiu.gphone.student.homework.data.PaperResponse;
import com.yanxiu.gphone.student.util.DESBodyDealer;

import java.util.List;

/**
 * Created by sp on 17-5-22.
 */

public class HomeworkDetailRepository implements HomeworkDetailDataSource {
    private static HomeworkDetailRepository INSTANCE = null;

    private int mPageIndex = 1;

    private int mTotalPage = 0;

    private List<HomeworkDetailBean> mHomeworkDetails;

    public static HomeworkDetailRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new HomeworkDetailRepository();
        }
        return INSTANCE;
    }

    @Override
    public void getHomeworkDetails(String homeworkId, final LoadHomeworkDetailCallback loadHomeworkDetailCallback) {
        HomeworkDetailRequest request = new HomeworkDetailRequest();
        request.setGroupId(homeworkId);
        request.setPage("1");
        request.startRequest(HomeworkDetailResponse.class, new ExerciseBaseCallback<HomeworkDetailResponse>() {

            @Override
            public void onSuccess(RequestBase request, HomeworkDetailResponse ret) {
                super.onSuccess(request, ret);
                if(ret.getStatus().getCode() == 0 ){
                    mPageIndex = 1;
                    mTotalPage = ret.getPage().getTotalPage();
                    mHomeworkDetails = ret.getData();
                    if(ret.getData().size() == 0){
                        loadHomeworkDetailCallback.onDataEmpty();
                    }else {
                        loadHomeworkDetailCallback.onHomeworkDetailLoaded(mHomeworkDetails);
                    }
                }else {
                    loadHomeworkDetailCallback.onDataError(ret.getStatus().getCode(), ret.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                    loadHomeworkDetailCallback.onDataError(Constants.NET_ERROR, error.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getMoreHomeworkDetails(String homeworkId, final LoadHomeworkDetailCallback loadHomeworkDetailCallback) {
        mPageIndex++;
        HomeworkDetailRequest request = new HomeworkDetailRequest();
        request.setGroupId(homeworkId);
        request.setPage(mPageIndex+"");
        request.startRequest(HomeworkDetailResponse.class, new ExerciseBaseCallback<HomeworkDetailResponse>() {

            @Override
            public void onSuccess(RequestBase request, HomeworkDetailResponse ret) {
                super.onSuccess(request, ret);
                if(ret.getStatus().getCode() == 0 ){
                    mTotalPage = ret.getPage().getTotalPage();
                    if(ret.getData().size() == 0){
                        loadHomeworkDetailCallback.onDataEmpty();
                    }else {
                        mHomeworkDetails.addAll(ret.getData());
                        loadHomeworkDetailCallback.onHomeworkDetailLoaded(mHomeworkDetails);
                    }
                }else {
                    loadHomeworkDetailCallback.onDataError(ret.getStatus().getCode(), ret.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                loadHomeworkDetailCallback.onDataError(Constants.NET_ERROR,error.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getPaper(String paperId, final LoadPaperCallback loadPaperCallback) {
        PaperRequest request = new PaperRequest();
        request.setPaperId(paperId);
        request.bodyDealer = new DESBodyDealer();
        request.startRequest(PaperResponse.class, new HttpCallback<PaperResponse>() {
            @Override
            public void onSuccess(RequestBase request, PaperResponse ret) {
                if(ret.getStatus().getCode() == 0){
                    if(ret.getData().size() > 0){
                        //TODO 先存起来
                        loadPaperCallback.onPaperLoaded(ret.getData());
                    }else {
                        loadPaperCallback.onDataEmpty();
                    }
                }else {
                    loadPaperCallback.onDataError(ret.getStatus().getCode(),ret.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                loadPaperCallback.onDataError(Constants.NET_ERROR,error.getLocalizedMessage());
            }
        });
    }

    public boolean canLoadMore(){
        return mPageIndex < mTotalPage;
    }
}
