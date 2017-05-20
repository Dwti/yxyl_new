package com.yanxiu.gphone.student.homework;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailBean;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailRequest;
import com.yanxiu.gphone.student.homework.data.HomeworkDetailResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-5-18.
 */

public class HomeworkDetailActivity extends Activity {
    public static final String EXTRA_SUBJECT_ID = "HOMEWORK_ID";
    private int mPageIndex = 1;
    private String mHomeworkId;
    private List<HomeworkDetailBean> mHomeworkList = new ArrayList<>();
    private HomeworkDetailAdapter mHomeworkDetailAdapter;
    private ListView mHomeworkListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        View back = findViewById(R.id.iv_back);
        mHomeworkListView = (ListView) findViewById(R.id.list_view);
        mHomeworkDetailAdapter = new HomeworkDetailAdapter(mHomeworkList);
        mHomeworkListView.setAdapter(mHomeworkDetailAdapter);
        mHomeworkId = getIntent().getStringExtra(EXTRA_SUBJECT_ID);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadHomework(mHomeworkId,mPageIndex);
    }

    private void loadHomework(String homeworkId, int pageIndex) {
        HomeworkDetailRequest request = new HomeworkDetailRequest();
        request.setGroupId(homeworkId);
        request.setPage(pageIndex+"");
        request.startRequest(HomeworkDetailResponse.class,mLoadHomeworkCallback);

    }

    HttpCallback<HomeworkDetailResponse> mLoadHomeworkCallback = new HttpCallback<HomeworkDetailResponse>() {
        @Override
        public void onSuccess(RequestBase request, HomeworkDetailResponse ret) {
            if(ret.getStatus().getCode() ==0){
                if(mPageIndex ==1){
                    mHomeworkList = ret.getData();
                }else if(mPageIndex > 1){
                    mHomeworkList.addAll(ret.getData());
                }
                mHomeworkDetailAdapter.replaceData(mHomeworkList);
            }
            //TODO 错误的时候没有处理(别的界面也没有处理)
        }

        @Override
        public void onFail(RequestBase request, Error error) {

        }
    };

    private static class HomeworkDetailAdapter extends BaseAdapter{
        List<HomeworkDetailBean> homeworkDetails;
        Context mContext;
        public HomeworkDetailAdapter(List<HomeworkDetailBean> homeworkDetails) {
            this.homeworkDetails = homeworkDetails;
        }

        @Override
        public int getCount() {
            return homeworkDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return homeworkDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void replaceData(List<HomeworkDetailBean> data){
            if(data != null){
                homeworkDetails = data;
                notifyDataSetChanged();
            }
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            HomeworkDetailBean bean = homeworkDetails.get(position);
            if(mContext == null){
                mContext = parent.getContext();
            }
            if(convertView == null){
                holder = new Holder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_homework_detail,parent,false);
                holder.mIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.mName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.mState1 = (TextView) convertView.findViewById(R.id.tv_state1);
                holder.mState2 = (TextView) convertView.findViewById(R.id.tv_state2);
                holder.mComment = (TextView) convertView.findViewById(R.id.tv_comment);
                holder.mCommentLayout = convertView.findViewById(R.id.ll_comment);
                convertView.setTag(holder);
            }else {
                holder = (Holder) convertView.getTag();
            }
            holder.mName.setText(bean.getName());
            //TODO 根据状态设置icon
            if(bean.getPaperStatus().getStatus() == 0){   //待完成
                holder.mState1.setText(mContext.getString(R.string.homework_done_num) + bean.getAnswernum()  + "/" + bean.getQuesnum());
                if(bean.getIsEnd() == 0){  //未截止
                    holder.mState2.setText(mContext.getString(R.string.homework_remain_time)+ bean.getRemaindertimeStr());
                }else {   //已截止
                    holder.mState2.setText(R.string.can_redo);
                }
                holder.mCommentLayout.setVisibility(View.GONE);
            }else if(bean.getPaperStatus().getStatus() ==1){ // 未完成
                holder.mState1.setText(R.string.over_deadline);
                holder.mState2.setText("");
                holder.mCommentLayout.setVisibility(View.GONE);
            }else if(bean.getPaperStatus().getStatus() == 2){//已完成
                if(TextUtils.isEmpty(bean.getPaperStatus().getTeachercomments()) || TextUtils.isEmpty(bean.getPaperStatus().getTeacherName())){
                    holder.mCommentLayout.setVisibility(View.GONE);
                    holder.mState1.setText(R.string.homework_done_uncheck);
                }else {
                    holder.mCommentLayout.setVisibility(View.VISIBLE);
                    holder.mState1.setText(R.string.homework_checked);
                    //TODO 前面几个字儿需要加粗
                    String text = bean.getPaperStatus().getTeacherName() + mContext.getString(R.string.comment) + bean.getPaperStatus().getTeachercomments();
                    holder.mComment.setText(text);
                }
            }
            return convertView;
        }

        class Holder {
            ImageView mIcon;
            TextView mName,mState1,mState2,mComment;
            View mCommentLayout;
        }
    }
}
