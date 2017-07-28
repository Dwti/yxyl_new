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
import com.yanxiu.gphone.student.exercise.adapter.ChapterAdapter;
import com.yanxiu.gphone.student.exercise.adapter.ExpandChapterAdapter;
import com.yanxiu.gphone.student.exercise.bean.ChapterBean;
import com.yanxiu.gphone.student.exercise.bean.EditionBeanEx;
import com.yanxiu.gphone.student.exercise.request.EditionRequest;
import com.yanxiu.gphone.student.exercise.request.GetChapterListRequest;
import com.yanxiu.gphone.student.exercise.response.ChapterListResponse;
import com.yanxiu.gphone.student.exercise.response.EditionResponse;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class SelecChapterAndKnowledgeActivity extends Activity{

    private View mBack;
    private TextView mTitle;
    private RecyclerView mRecyclerView;
    private ExpandChapterAdapter mAdapter;
    private String mSubjectName,mSubjectId,mEditionId;

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
    }

    private void initView() {
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
        GetChapterListRequest request = new GetChapterListRequest();
        request.setSubjectId(subjectId);
        request.setEditionId(editionId);
        request.setVolume(volume);
        request.startRequest(ChapterListResponse.class,mChapterCallback);
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
                mAdapter = new ExpandChapterAdapter(response.getData());
                mRecyclerView.setAdapter(mAdapter);
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
