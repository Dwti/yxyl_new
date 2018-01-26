package com.yanxiu.gphone.student.exercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.ChapterSwitchBar;
import com.yanxiu.gphone.student.exercise.bean.SubjectBean;
import com.yanxiu.gphone.student.exercise.request.SubjectsRequest;
import com.yanxiu.gphone.student.exercise.response.SubjectsResponse;
import com.yanxiu.gphone.student.learning.LearningEditionSelectChangeMessage;
import com.yanxiu.gphone.student.learning.request.LearningSubjectRequest;
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
    private ListView mListView;
    private ImageView mTipsImg;
    private SelectSubjectAdapter mPracticseAdapter;
    private ChapterSwitchBar mSwitchBar;
    private SelectSubjectAdapter mLearningAdapter;
    private boolean mIsPracticeMode = true;

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
        requestPracticeSubjects();
    }

    public void onEventMainThread(LearningEditionSelectChangeMessage message){
        requestLearningSubjects();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
        mTipsView = findViewById(R.id.tips_layout);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);
        mTipsImg = (ImageView) findViewById(R.id.iv_tips);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mBack = findViewById(R.id.back);

        mSwitchBar = (ChapterSwitchBar) findViewById(R.id.switchBar);
        mSwitchBar.setOnText(R.string.study);
        mSwitchBar.setOffText(R.string.exercises);
    }

    private void initListener() {
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsPracticeMode) {
                    requestPracticeSubjects();
                } else {
                    requestLearningSubjects();
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubjectBean bean;
                if (mIsPracticeMode) {
                    bean = (SubjectBean) mPracticseAdapter.getItem(position);
                } else {
                    bean = (SubjectBean) mLearningAdapter.getItem(position);
                }
                String subjectId = bean.getId();
                String subjectName = bean.getName();
                String editionName = null;
                if(bean.getData() != null){
                    editionName = bean.getData().getEditionName();
                }
                if (mIsPracticeMode) {
                    ModifyEditionActivity.invoke(SelectSubjectActivity.this, subjectId, subjectName,editionName, ModifyEditionActivity.FROM_SUBJECT_SELECT);
                } else {
                    ModifyEditionActivity.invoke(SelectSubjectActivity.this, subjectId, subjectName,editionName, ModifyEditionActivity.FROM_LEARNING);
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwitchBar.setOnCheckedChangedListener(new ChapterSwitchBar.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(boolean isOff) {
                if (isOff) {
                    mIsPracticeMode = true;
                    mListView.setAdapter(mPracticseAdapter);
                    if(mPracticseAdapter.getCount() == 0) {
                        requestPracticeSubjects();
                    }
                } else {
                    mIsPracticeMode = false;
                    mListView.setAdapter(mLearningAdapter);
                    if(mLearningAdapter.getCount() == 0) {
                        requestLearningSubjects();
                    }
                }
            }
        });
    }

    private void initData() {
        mPracticseAdapter = new SelectSubjectAdapter(new ArrayList<SubjectBean>(0));
        mListView.setAdapter(mPracticseAdapter);
        requestPracticeSubjects();
        mLearningAdapter = new SelectSubjectAdapter(new ArrayList<SubjectBean>(0));
    }

    private void showDataEmptyView() {
        mListView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.data_empty);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showDataErrorView() {
        mListView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showPracticeSubjects(List<SubjectBean> data) {
        mListView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        mPracticseAdapter.replaceData(data);
    }

    private void showLearningSubjects(List<SubjectBean> data) {
        mListView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        mLearningAdapter.replaceData(data);
    }

    private void requestPracticeSubjects() {
        SubjectsRequest request = new SubjectsRequest();
        request.setStageId(LoginInfo.getStageid());
        request.setSubjectIds(LoginInfo.getSubjectIds_string());
        request.startRequest(SubjectsResponse.class, mSubjectsCallback);
    }

    //TODO:添加学习模块获取学科接口
    private void requestLearningSubjects() {
        LearningSubjectRequest request = new LearningSubjectRequest();
        request.setStageId(LoginInfo.getStageid());
        request.setSubjectIds(LoginInfo.getSubjectIds_string());
        request.startRequest(SubjectsResponse.class, mLearningSubjectsCallback);
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
                    showPracticeSubjects(response.getData());
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


    HttpCallback<SubjectsResponse> mLearningSubjectsCallback = new EXueELianBaseCallback<SubjectsResponse>() {
        @Override
        public void onSuccess(RequestBase request, SubjectsResponse ret) {
            super.onSuccess(request, ret);
        }

        @Override
        protected void onResponse(RequestBase request, SubjectsResponse response) {
            if (response.getStatus().getCode() == 0) {
                if (response.getData().size() > 0) {
//                    List<SubjectBean> list = response.getData();
//                    list.remove(0);
//                    if(list.size() > 0) {
//                        showLearningSubjects(list);
//                    }
                    showLearningSubjects(response.getData());
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
