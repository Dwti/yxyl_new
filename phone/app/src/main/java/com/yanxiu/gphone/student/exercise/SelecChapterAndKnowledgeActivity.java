package com.yanxiu.gphone.student.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.customviews.ChapterSwitchBar;
import com.yanxiu.gphone.student.exercise.adapter.ChapterAdapter;
import com.yanxiu.gphone.student.exercise.adapter.KnowledgePointAdapter;
import com.yanxiu.gphone.student.exercise.bean.EditionBeanEx;
import com.yanxiu.gphone.student.exercise.request.EditionRequest;
import com.yanxiu.gphone.student.exercise.request.ChapterListRequest;
import com.yanxiu.gphone.student.exercise.request.KnowledgePointRequest;
import com.yanxiu.gphone.student.exercise.response.ChapterListResponse;
import com.yanxiu.gphone.student.exercise.response.EditionResponse;
import com.yanxiu.gphone.student.exercise.response.KnowledgePointResponse;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class SelecChapterAndKnowledgeActivity extends Activity{

    private View mBack;
    private TextView mTitle;
    private RecyclerView mRecyclerView;
    private ChapterAdapter mChapterAdapter;
    private KnowledgePointAdapter mKnowledgePointAdapter;
    private String mSubjectName,mSubjectId,mEditionId;
    private ChapterSwitchBar mSwitchBar;

    private static final String SUBJECT_ID = "SUBJECT_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String EDITION_ID = "EDITION_ID";


    public static void invoke(Activity activity,String subjectId,String subjectName,String editionId){
        Intent intent = new Intent(activity,SelecChapterAndKnowledgeActivity.class);
        intent.putExtra(SUBJECT_ID,subjectId);
        intent.putExtra(SUBJECT_NAME,subjectName);
        intent.putExtra(EDITION_ID,editionId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_chapter);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSwitchBar.setOnCheckedChangedListener(new ChapterSwitchBar.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(boolean isOff) {
                if(isOff){
                    if(mChapterAdapter == null){
                        getEditionList(mSubjectId);
                    }else {
                        mRecyclerView.setAdapter(mChapterAdapter);
                    }
                }else {
                    if(mKnowledgePointAdapter == null){
                        getKnowledgePointList(mSubjectId);
                    }else {
                        mRecyclerView.setAdapter(mKnowledgePointAdapter);
                    }
                }
            }
        });
    }

    private void initView() {
        mSwitchBar = (ChapterSwitchBar) findViewById(R.id.switchBar);
        mSwitchBar.setVisibility(View.INVISIBLE);
        mBack = findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData(){
        mSubjectId = getIntent().getStringExtra(SUBJECT_ID);
        mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        mEditionId = getIntent().getStringExtra(EDITION_ID);
        mTitle.setText(mSubjectName);
        getEditionList(mSubjectId);
    }

    private void getChapterList(String subjectId,String editionId,String volume){
        ChapterListRequest request = new ChapterListRequest();
        request.setSubjectId(subjectId);
        request.setEditionId(editionId);
        request.setVolume(volume);
        request.startRequest(ChapterListResponse.class,mChapterCallback);
    }

    private void getKnowledgePointList(String subjectId){
        KnowledgePointRequest request = new KnowledgePointRequest();
        request.setSubjectId(subjectId);
        request.startRequest(KnowledgePointResponse.class,mKnowledgePointCallback);
    }

    private void getEditionList(String subjectId){
        EditionRequest request = new EditionRequest();
        request.setSubjectId(subjectId);
        request.startRequest(EditionResponse.class,mEditionCallback);
    }

    private String getVolume(List<EditionBeanEx> list,String editionId){
        String volume = "";
        for(EditionBeanEx bean : list){
            if(editionId.equals(bean.getId())){
                if(bean.getChildren() != null && bean.getChildren().size() > 0){
                    volume = bean.getChildren().get(0).getId();
                }
                break;
            }
        }
        return volume;
    }

    HttpCallback<EditionResponse> mEditionCallback = new EXueELianBaseCallback<EditionResponse>() {
        @Override
        protected void onResponse(RequestBase request, EditionResponse response) {
            if(response.getStatus().getCode() == 0){
                String volume = getVolume(response.getData(),mEditionId);
                getChapterList(mSubjectId,mEditionId,volume);
            }else {
                ToastManager.showMsg(response.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };

    HttpCallback<ChapterListResponse> mChapterCallback = new EXueELianBaseCallback<ChapterListResponse>() {
        @Override
        protected void onResponse(RequestBase request, ChapterListResponse response) {
            if(response.getStatus().getCode() == 0){
                mSwitchBar.setVisibility(View.VISIBLE);
                mChapterAdapter = new ChapterAdapter(response.getData());
                mRecyclerView.setAdapter(mChapterAdapter);
            }else {
                ToastManager.showMsg(response.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };

    HttpCallback<KnowledgePointResponse> mKnowledgePointCallback = new EXueELianBaseCallback<KnowledgePointResponse>() {
        @Override
        protected void onResponse(RequestBase request, KnowledgePointResponse response) {
            if(response.getStatus().getCode() == 0){
                mKnowledgePointAdapter = new KnowledgePointAdapter(response.getData());
                mRecyclerView.setAdapter(mKnowledgePointAdapter);
            }else {
                ToastManager.showMsg(response.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };
}
