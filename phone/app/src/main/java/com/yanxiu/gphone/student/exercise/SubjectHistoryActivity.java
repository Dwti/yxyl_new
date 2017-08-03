package com.yanxiu.gphone.student.exercise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.exercise.bean.SubjectBean;
import com.yanxiu.gphone.student.exercise.request.SubjectHistoryRequest;
import com.yanxiu.gphone.student.exercise.response.SubjectHistoryResponse;
import com.yanxiu.gphone.student.homework.homeworkdetail.HomeworkDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-8-2.
 */

public class SubjectHistoryActivity extends Activity {

    private SubjectAdapter mAdapter;
    private View mTipsView,mBack;
    private TextView mTips;
    private Button mRefreshBtn;
    private ListView mListView;


    public static void invoke(Context context){
        Intent intent = new Intent(context,SubjectHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_history);
        initView();
        initListener();
        loadSubject();
    }

    private void initView() {
        mBack = findViewById(R.id.iv_back);
        mListView = (ListView) findViewById(R.id.list_view);
        mTipsView = findViewById(R.id.tips_layout);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);
        mAdapter = new SubjectAdapter(new ArrayList<SubjectBean>(0));
        mListView.setAdapter(mAdapter);
    }


    private void initListener() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubjectBean bean = (SubjectBean) mAdapter.getItem(position);
                ExerciseHistoryActivity.invoke(SubjectHistoryActivity.this,bean.getId(),bean.getName(),bean.getData().getEditionId());
            }
        });
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSubjects();
                showContentView();
                loadSubject();
            }
        });
    }

    private void loadSubject() {
        SubjectHistoryRequest request = new SubjectHistoryRequest();
        request.startRequest(SubjectHistoryResponse.class, mLoadSubjectCallback);
    }


    HttpCallback<SubjectHistoryResponse> mLoadSubjectCallback = new EXueELianBaseCallback<SubjectHistoryResponse>(){

        @Override
        public void onResponse(RequestBase request, SubjectHistoryResponse ret) {
            if(ret.getStatus().getCode() == 0){
                if(ret.getData() != null && ret.getData().size() > 0){
                    showSubjects(ret.getData());
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
    } ;

    private void showSubjects(List<SubjectBean> data){
        showContentView();
        mAdapter.replaceData(data);
    }

    private void clearSubjects(){
        mAdapter.clearData();
    }

    private void showContentView(){
        mListView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
    }

    private void showDataEmptyView(){
        mListView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.class_no_homework);
        mRefreshBtn.setText(R.string.click_to_refresh);
    }


    private void showDataErrorView(){
        mListView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private static class SubjectAdapter extends BaseAdapter {

        private List<SubjectBean> subjects;

        public SubjectAdapter(List<SubjectBean> subjects) {
            this.subjects = subjects;
        }

        public void replaceData(List<SubjectBean> data){
            if(data != null){
                subjects = data;
                notifyDataSetChanged();
            }
        }

        public void clearData(){
            if(subjects !=null && subjects.size() > 0){
                subjects.clear();
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return subjects.size();
        }

        @Override
        public Object getItem(int position) {
            return subjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_history,parent,false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            name.setText(subjects.get(position).getName());
            setIcon(subjects.get(position).getId(),icon);
            return convertView;
        }

        private void setIcon(String id, ImageView imageView) {
            switch (id){
                case "1102":
                    imageView.setImageResource(R.drawable.yuwen1);
                    break;
                case "1103":
                    imageView.setImageResource(R.drawable.shuxue1);
                    break;
                case "1104":
                    imageView.setImageResource(R.drawable.yingyu1);
                    break;
                case "1105":
                    imageView.setImageResource(R.drawable.wuli1);
                    break;
                case "1106":
                    imageView.setImageResource(R.drawable.huaxue1);
                    break;
                case "1107":
                    imageView.setImageResource(R.drawable.shengwu1);
                    break;
                case "1108":
                    imageView.setImageResource(R.drawable.dili1);
                    break;
                case "1109":
                    imageView.setImageResource(R.drawable.zhengzhi1);
                    break;
                case "1110":
                    imageView.setImageResource(R.drawable.lishi1);
                    break;
                default:
                    break;
            }
        }
    }

}
