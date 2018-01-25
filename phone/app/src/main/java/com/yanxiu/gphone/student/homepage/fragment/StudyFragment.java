package com.yanxiu.gphone.student.homepage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.exercise.EditionSelectChangeMessage;
import com.yanxiu.gphone.student.exercise.ModifyEditionActivity;
import com.yanxiu.gphone.student.exercise.bean.SubjectBean;
import com.yanxiu.gphone.student.exercise.request.SubjectsRequest;
import com.yanxiu.gphone.student.exercise.response.SubjectsResponse;
import com.yanxiu.gphone.student.learning.adapter.LearningSubjectsAdapter;
import com.yanxiu.gphone.student.learning.activity.SelectLearningEditionActivity;
import com.yanxiu.gphone.student.learning.activity.SelectSyncAndSpecailActivity;
import com.yanxiu.gphone.student.login.activity.ChooseStageActivity;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/12 12:06.
 * Function :
 */
public class StudyFragment extends HomePageBaseFragment {
    private final static String TAG = ExerciseFragment.class.getSimpleName();
    private TextView mTips;
    private View mTipsView;
    private ImageView mTipsImg;
    private Button mRefreshBtn;
    private GridView mGridView;
    private LearningSubjectsAdapter mAdapter;
    private String mStageId;
    private PublicLoadLayout rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=new PublicLoadLayout(inflater.getContext());
        rootView.setContentView(R.layout.fragment_study);
        EventBus.getDefault().register(this);
        initView();
        initListener();
        initData();
        return rootView;
    }

    public void onEventMainThread(EditionSelectChangeMessage message){
        requestSubjects(mStageId);
    }

    public void onEventMainThread(ChooseStageActivity.StageMessage message){
        mStageId = message.stageId;
        requestSubjects(mStageId);
    }

    private void initView() {
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mTipsView = rootView.findViewById(R.id.tips_layout);
        mTipsImg = (ImageView) rootView.findViewById(R.id.iv_tips);
        mRefreshBtn = (Button) rootView.findViewById(R.id.btn_refresh);
        mTips = (TextView) rootView.findViewById(R.id.tv_tips);
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
                    SelectSyncAndSpecailActivity.invoke(getActivity(),bean.getId(),bean.getName(),bean.getData().getEditionId());
                }else {
                        String subjectId = bean.getId();
                        String subjectName = bean.getName();
                        String editionName = null;
                        if(bean.getData() != null){
                            editionName = bean.getData().getEditionName();
                        }
                    SelectLearningEditionActivity.invoke(getActivity(), subjectId, subjectName,editionName, ModifyEditionActivity.FROM_EXERCISE);
                }
            }
        });
    }

    private void initData() {
        mAdapter = new LearningSubjectsAdapter(new ArrayList<SubjectBean>(0));
        mGridView.setAdapter(mAdapter);
        mStageId = LoginInfo.getStageid();
        requestSubjects(mStageId);
    }

    private void requestSubjects(String stageId){
        rootView.showLoadingView();
        SubjectsRequest request = new SubjectsRequest();
        request.setStageId(stageId);
        request.startRequest(SubjectsResponse.class,mSubjectsCallback);
    }

    HttpCallback<SubjectsResponse> mSubjectsCallback = new EXueELianBaseCallback<SubjectsResponse>() {
        @Override
        protected void onResponse(RequestBase request, SubjectsResponse response) {
            rootView.hiddenLoadingView();
            if(response.getStatus().getCode() == 0){
                if(response.getData().size() > 0){
                    showSubjects(response.getData());
                }else {
                    rootView.showOtherErrorView();
                }
            }else {
                rootView.showNetErrorView();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            rootView.hiddenLoadingView();
            rootView.showNetErrorView();
        }
    };

//    private void showDataEmptyView(){
//        mGridView.setVisibility(View.GONE);
//        mTipsView.setVisibility(View.VISIBLE);
//        mTips.setText("");
//        mRefreshBtn.setText(R.string.click_to_retry);
//    }
//
//    private void showDataErrorView(){
//        mGridView.setVisibility(View.GONE);
//        mTipsView.setVisibility(View.VISIBLE);
//        mTipsImg.setImageResource(R.drawable.net_error);
//        mTips.setText(R.string.load_failed);
//        mRefreshBtn.setText(R.string.click_to_retry);
//    }

    private void showSubjects(List<SubjectBean> data){
        mGridView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        mAdapter.replaceData(data);
    }
}
