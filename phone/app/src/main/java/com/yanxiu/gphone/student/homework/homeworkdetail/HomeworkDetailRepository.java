package com.yanxiu.gphone.student.homework.homeworkdetail;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.homework.request.HomeworkReportRequest;
import com.yanxiu.gphone.student.homework.response.HomeworkDetailBean;
import com.yanxiu.gphone.student.homework.request.HomeworkDetailRequest;
import com.yanxiu.gphone.student.homework.response.HomeworkDetailResponse;
import com.yanxiu.gphone.student.homework.request.PaperRequest;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;

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
        request.startRequest(HomeworkDetailResponse.class, new EXueELianBaseCallback<HomeworkDetailResponse>() {

            @Override
            public void onResponse(RequestBase request, HomeworkDetailResponse ret) {
                if(ret.getStatus().getCode() == 0 ){
                    mPageIndex = 1;
                    if(ret.getPage() != null){
                        mTotalPage = ret.getPage().getTotalPage();
                    }else {
                        loadHomeworkDetailCallback.onDataEmpty();
                        return;
                    }
                    mHomeworkDetails = ret.getData();
                    if(ret.getData() == null || ret.getData().size() == 0){
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
        request.startRequest(HomeworkDetailResponse.class, new EXueELianBaseCallback<HomeworkDetailResponse>() {

            @Override
            public void onResponse(RequestBase request, HomeworkDetailResponse ret) {
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
    public void getPaper(String paperId, final int status,final LoadPaperCallback loadPaperCallback) {
        PaperRequest request = new PaperRequest();
        request.setPaperId(paperId);
        request.bodyDealer = new DESBodyDealer();
        request.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            public void onResponse(RequestBase request, PaperResponse ret) {
                if(ret.getStatus().getCode() == 0){
                    if(ret.getData().size() > 0){
                        QuestionShowType type = status == HomeworkDetailPresenter.STATUS_TODO ? QuestionShowType.ANSWER:QuestionShowType.ANALYSIS;
                        Paper paper = new Paper(ret.getData().get(0), type);
                        DataFetcher.getInstance().save(paper.getId(),paper);
                        loadPaperCallback.onPaperLoaded(paper);
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

    @Override
    public void getReport(String paperId, final LoadReportCallback loadReportCallback) {
        HomeworkReportRequest request = new HomeworkReportRequest();
        request.setPpid(paperId);
        request.bodyDealer = new DESBodyDealer();
        request.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            public void onResponse(RequestBase request, PaperResponse ret) {
                if(ret.getStatus().getCode() == 0){
                    if(ret.getData().size() > 0){
                        Paper paper = new Paper(ret.getData().get(0), QuestionShowType.ANALYSIS);
                        DataFetcher.getInstance().save(paper.getId(),paper);
                        loadReportCallback.onAnalysisLoaded(paper);
                    }else {
                        loadReportCallback.onDataEmpty();
                    }
                }else {
                    loadReportCallback.onDataError(ret.getStatus().getCode(),ret.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                loadReportCallback.onDataError(Constants.NET_ERROR,error.getLocalizedMessage());
            }
        });
    }

    public boolean canLoadMore(){
        return mPageIndex < mTotalPage;
    }
}
