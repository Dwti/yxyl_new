package com.yanxiu.gphone.student.homepage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.exercise.EditionSelectChangeMessage;
import com.yanxiu.gphone.student.exercise.SelectChapterAndKnowledgeActivity;
import com.yanxiu.gphone.student.exercise.SelectEditionActivity;
import com.yanxiu.gphone.student.exercise.bean.SubjectBean;
import com.yanxiu.gphone.student.exercise.SubjectsAdapter;
import com.yanxiu.gphone.student.exercise.request.SubjectsRequest;
import com.yanxiu.gphone.student.exercise.response.SubjectsResponse;
import com.yanxiu.gphone.student.login.activity.ChooseStageActivity;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 首页 “练习”Fragment
 */
public class ExerciseFragment extends HomePageBaseFragment {
    private final static String TAG = ExerciseFragment.class.getSimpleName();
    private TextView mTips;
    private View mTipsView;
    private ImageView mTipsImg;
    private Button mRefreshBtn;
    private GridView mGridView;
    private SubjectsAdapter mAdapter;
    private String mStageId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        initData();
        initListener();
        return view;
    }

    public void onEventMainThread(EditionSelectChangeMessage message){
        requestSubjects(mStageId);
    }

    public void onEventMainThread(ChooseStageActivity.StageMessage message){
        mStageId = message.stageId;
        requestSubjects(mStageId);
    }

    private void initView(View view) {
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mTipsView = view.findViewById(R.id.tips_layout);
        mTipsImg = (ImageView) view.findViewById(R.id.iv_tips);
        mRefreshBtn = (Button) view.findViewById(R.id.btn_refresh);
        mTips = (TextView) view.findViewById(R.id.tv_tips);
    }

    private void initListener() {
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSubjects(LoginInfo.getStageid());
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubjectBean bean = (SubjectBean) mAdapter.getItem(position);
                if(bean.getData() != null){
                    SelectChapterAndKnowledgeActivity.invoke(getActivity(),bean.getId(),bean.getName(),bean.getData().getEditionId());
                }else {
                    String subjectId = bean.getId();
                    String subjectName = bean.getName();
                    String editionName = null;
                    if(bean.getData() != null){
                        editionName = bean.getData().getEditionName();
                    }
                    SelectEditionActivity.invoke(getActivity(), subjectId, subjectName,editionName,SelectEditionActivity.FROM_EXERCISE);
                }
            }
        });
    }

    private void initData() {
        mAdapter = new SubjectsAdapter(new ArrayList<SubjectBean>(0));
        mGridView.setAdapter(mAdapter);
        mStageId = LoginInfo.getStageid();
        requestSubjects(mStageId);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showDataEmptyView(){
        mGridView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText("");
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showDataErrorView(){
        mGridView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showSubjects(List<SubjectBean> data){
        mGridView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        mAdapter.replaceData(data);
    }

    private void requestSubjects(String stageId){
        SubjectsRequest request = new SubjectsRequest();
        request.setStageId(stageId);
        request.startRequest(SubjectsResponse.class,mSubjectsCallback);
    }

    HttpCallback<SubjectsResponse> mSubjectsCallback = new EXueELianBaseCallback<SubjectsResponse>() {
        @Override
        protected void onResponse(RequestBase request, SubjectsResponse response) {
            if(response.getStatus().getCode() == 0){
                if(response.getData().size() > 0){
                    showSubjects(response.getData());
                }else {
                    showDataEmptyView();
                }
            }else {
                showDataErrorView();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView();
        }
    };
}
