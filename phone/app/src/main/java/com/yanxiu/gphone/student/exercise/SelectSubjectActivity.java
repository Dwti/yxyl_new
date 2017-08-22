package com.yanxiu.gphone.student.exercise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.exercise.bean.SubjectBean;
import com.yanxiu.gphone.student.exercise.request.SubjectsRequest;
import com.yanxiu.gphone.student.exercise.response.SubjectsResponse;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sp on 17-7-26.
 */

public class SelectSubjectActivity extends YanxiuBaseActivity {

    private TextView mTips;
    private View mTipsView,mBack;
    private Button mRefreshBtn;
    private GridView mGridView;
    private SubjectsAdapter mAdapter;

    public static void invoke(Context context) {
        Intent intent = new Intent(context, SelectSubjectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }

    public void onEventMainThread(EditionSelectChangeMessage message){
        requestSubjects();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gridView);
        mTipsView = findViewById(R.id.tips_layout);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mBack = findViewById(R.id.back);
    }

    private void initListener() {
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSubjects();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubjectBean bean = (SubjectBean) mAdapter.getItem(position);
                String subjectId = bean.getId();
                String subjectName = bean.getName();
                String editionName = null;
                if(bean.getData() != null){
                    editionName = bean.getData().getEditionName();
                }
                SelectEditionActivity.invoke(SelectSubjectActivity.this, subjectId, subjectName,editionName,SelectEditionActivity.FROM_SUBJECT_SELECT);
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mAdapter = new SubjectsAdapter(new ArrayList<SubjectBean>(0));
        mGridView.setAdapter(mAdapter);
        requestSubjects();
    }

    private void showDataEmptyView() {
        mGridView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.data_empty);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showDataErrorView() {
        mGridView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showSubjects(List<SubjectBean> data) {
        mGridView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        mAdapter.replaceData(data);
    }

    private void requestSubjects() {
        SubjectsRequest request = new SubjectsRequest();
        request.setStageId(LoginInfo.getStageid());
        request.startRequest(SubjectsResponse.class, mSubjectsCallback);
    }

    HttpCallback<SubjectsResponse> mSubjectsCallback = new EXueELianBaseCallback<SubjectsResponse>() {
        @Override
        public void onSuccess(RequestBase request, SubjectsResponse ret) {
            super.onSuccess(request, ret);
        }

        @Override
        protected void onResponse(RequestBase request, SubjectsResponse response) {
            if (response.getStatus().getCode() == 0) {
                if (response.getData().size() > 0) {
                    showSubjects(response.getData());
                } else {
                    showDataEmptyView();
                }
            } else {
                showDataErrorView();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
