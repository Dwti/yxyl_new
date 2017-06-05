package com.yanxiu.gphone.student.homework;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.ExerciseBaseCallback;
import com.yanxiu.gphone.student.base.HomePageBaseFragment;
import com.yanxiu.gphone.student.homework.classmanage.ClassInfoActivity;
import com.yanxiu.gphone.student.homework.classmanage.ClassStatus;
import com.yanxiu.gphone.student.homework.classmanage.SearchClassFragment;
import com.yanxiu.gphone.student.homework.response.ClassBean;
import com.yanxiu.gphone.student.homework.request.SearchClassRequest;
import com.yanxiu.gphone.student.homework.response.SearchClassResponse;
import com.yanxiu.gphone.student.homework.response.SubjectBean;
import com.yanxiu.gphone.student.homework.request.SubjectRequest;
import com.yanxiu.gphone.student.homework.response.SubjectResponse;
import com.yanxiu.gphone.student.homework.homeworkdetail.HomeworkDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页 作业列表Fragment
 */
public class HomeworkFragment extends HomePageBaseFragment implements SearchClassFragment.OnJoinClassCompleteListener {

    private final static String TAG = HomeworkFragment.class.getSimpleName();

    public static final int REQUEST_CLASS_INFO = 0x01;

    private int mStatus = -1;  //班级状态，0：已加入班级 71：未加入班级  72：班级正在审核

    private String mClassId;

    private HomeworkAdapter mHomeworkAdapter;

    private SearchClassFragment mSearchClassFragment;

    private View mTipsView;

    private TextView mTitle, mTips;

    private Button mRefreshBtn;

    private ListView mListView;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_homework, container, false);
        ImageView btnCheckClassInfo = (ImageView) root.findViewById(R.id.iv_join_class);
        mTitle = (TextView) root.findViewById(R.id.tv_title);
        mListView = (ListView) root.findViewById(R.id.list_view);
        mTipsView = root.findViewById(R.id.tips_layout);
        mTips = (TextView) root.findViewById(R.id.tv_tips);
        mRefreshBtn = (Button) root.findViewById(R.id.btn_refresh);

        mHomeworkAdapter = new HomeworkAdapter(new ArrayList<SubjectBean>(0));
        mListView.setAdapter(mHomeworkAdapter);

        btnCheckClassInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClassInfo();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HomeworkDetailActivity.class);
                intent.putExtra(HomeworkDetailActivity.EXTRA_SUBJECT_ID,((SubjectBean)mHomeworkAdapter.getItem(position)).getId());
                intent.putExtra(HomeworkDetailActivity.EXTRA_SUBJECT_NAME,((SubjectBean)mHomeworkAdapter.getItem(position)).getName());
                startActivity(intent);
            }
        });
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSubject();
            }
        });
        requestData();
        return root;
    }

    /**
     * 请求数据
     */
    @Override
    public void requestData() {
        loadSubject();
    }

    private void checkClassInfo() {
        if(mStatus == ClassStatus.HAS_CLASS.getCode() || mStatus == ClassStatus.APPLYING_CLASS.getCode()){
            searchClass(mClassId);
        }
    }
    private void searchClass(String id){
        SearchClassRequest request = new SearchClassRequest();
        request.setClassId(id);
        request.startRequest(SearchClassResponse.class,mSearchClassCallback);
    }

    HttpCallback<SearchClassResponse> mSearchClassCallback = new ExerciseBaseCallback<SearchClassResponse>() {
        @Override
        public void onResponse(RequestBase request, SearchClassResponse ret) {
            if(ret.getStatus().getCode() == 0 ){
                ClassBean bean = ret.getData().get(0);
                Intent intent = new Intent(getActivity(),ClassInfoActivity.class);
                intent.putExtra(ClassInfoActivity.EXTRA_CLASS_INFO,bean);
                intent.putExtra(ClassInfoActivity.EXTRA_STATUS,mStatus);
                startActivityForResult(intent,REQUEST_CLASS_INFO);
            }else {
                Toast.makeText(getActivity(),ret.getStatus().getDesc(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            Toast.makeText(getActivity(),error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    private void loadSubject() {
        SubjectRequest request = new SubjectRequest();
        request.startRequest(SubjectResponse.class, mLoadSubjectCallback);
    }


    HttpCallback<SubjectResponse> mLoadSubjectCallback = new ExerciseBaseCallback<SubjectResponse>(){

        @Override
        public void onResponse(RequestBase request, SubjectResponse ret) {
            if(ret.getStatus().getCode() == ClassStatus.HAS_CLASS.getCode()){  //已经加入班级
                mClassId = ret.getProperty().getClassId();
                if(ret.getData() == null || ret.getData().size() == 0){
                    showDataEmptyView();
                }else {
                    showSubjects(ret.getData());
                }
                mTitle.setText(ret.getProperty().getClassName());
            }else if(ret.getStatus().getCode() == ClassStatus.APPLYING_CLASS.getCode()){ //班级正在审核
                mClassId = ret.getProperty().getClassId();
                showClassApplyView();
            }else if(ret.getStatus().getCode() == ClassStatus.NO_CLASS.getCode()){   //未加入班级
                openJoinClassUI();
            }else {           //其它错误
                showDataErrorView();
            }
            mStatus = ret.getStatus().getCode();
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView();
        }
    } ;

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        ((SearchClassFragment)childFragment).setOnJoinClassCompleteListener(this);
    }

    private void showSubjects(List<SubjectBean> data){
        showContentView();
        mHomeworkAdapter.replaceData(data);
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

    private void showClassApplyView(){
        mListView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.apply_for_class_checking);
        mRefreshBtn.setText(R.string.click_to_refresh);
    }

    private void showDataErrorView(){
        mListView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void openJoinClassUI(){
        mSearchClassFragment  = SearchClassFragment.getInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_content, mSearchClassFragment).commitAllowingStateLoss();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CLASS_INFO:
                if(resultCode == getActivity().RESULT_OK){
                    openJoinClassUI();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onJoinClassComplete() {
        getChildFragmentManager().beginTransaction().remove(mSearchClassFragment).commitAllowingStateLoss();
        loadSubject();
    }

    private static class HomeworkAdapter extends BaseAdapter{

        private List<SubjectBean> subjects;

        public HomeworkAdapter(List<SubjectBean> subjects) {
            this.subjects = subjects;
        }

        public void replaceData(List<SubjectBean> data){
            if(data != null){
                subjects = data;
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject,parent,false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            TextView state = (TextView) convertView.findViewById(R.id.tv_state);
            name.setText(subjects.get(position).getName());
            if(subjects.get(position).getWaitFinishNum() > 0){
                state.setText(String.format(parent.getContext().getResources().getString(R.string.homework_todo), subjects.get(position).getWaitFinishNum()));
                state.setTextColor(parent.getResources().getColor(R.color.color_99cc00));
            }else {
                state.setText(R.string.check_homework_done);
                state.setTextColor(parent.getResources().getColor(R.color.color_999999));
            }
            return convertView;
        }
    }


}
