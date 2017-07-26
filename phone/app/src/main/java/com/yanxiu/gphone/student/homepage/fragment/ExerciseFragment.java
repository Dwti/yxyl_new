package com.yanxiu.gphone.student.homepage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.exercise.SubjectBean;
import com.yanxiu.gphone.student.exercise.SubjectsAdapter;
import com.yanxiu.gphone.student.exercise.SubjectsRequest;
import com.yanxiu.gphone.student.exercise.SubjectsResponse;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页 “练习”Fragment
 */
public class ExerciseFragment extends HomePageBaseFragment {
    private final static String TAG = ExerciseFragment.class.getSimpleName();
    private TextView mTips;
    private View mTipsView;
    private Button mRefreshBtn;
    private GridView mGridView;
    private SubjectsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mTipsView = view.findViewById(R.id.tips_layout);
        mRefreshBtn = (Button) view.findViewById(R.id.btn_refresh);
        mTips = (TextView) view.findViewById(R.id.tv_tips);
        initData();
        initListener();
        return view;
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
                //TODO
            }
        });
    }

    private void initData() {
        mAdapter = new SubjectsAdapter(new ArrayList<SubjectBean>(0));
        mGridView.setAdapter(mAdapter);
        requestSubjects();
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
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showSubjects(List<SubjectBean> data){
        mGridView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        mAdapter.replaceData(data);
    }

    private void requestSubjects(){
        SubjectsRequest request = new SubjectsRequest();
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
