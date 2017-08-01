package com.yanxiu.gphone.student.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.customviews.ChapterSwitchBar;
import com.yanxiu.gphone.student.exercise.adapter.ChapterAdapter;
import com.yanxiu.gphone.student.exercise.adapter.KnowledgePointAdapter;
import com.yanxiu.gphone.student.exercise.adapter.PopListAdapter;
import com.yanxiu.gphone.student.exercise.bean.EditionBeanEx;
import com.yanxiu.gphone.student.exercise.bean.EditionChildBean;
import com.yanxiu.gphone.student.exercise.request.EditionRequest;
import com.yanxiu.gphone.student.exercise.request.ChapterListRequest;
import com.yanxiu.gphone.student.exercise.request.KnowledgePointRequest;
import com.yanxiu.gphone.student.exercise.response.ChapterListResponse;
import com.yanxiu.gphone.student.exercise.response.EditionResponse;
import com.yanxiu.gphone.student.exercise.response.KnowledgePointResponse;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class SelectChapterAndKnowledgeActivity extends Activity{

    private View mBack, mLayoutStage,mRootView;
    private TextView mTitle,mStage;
    private RecyclerView mRecyclerView;
    private ChapterAdapter mChapterAdapter;
    private KnowledgePointAdapter mKnowledgePointAdapter;
    private String mSubjectName,mSubjectId,mEditionId,mVolume;
    private ChapterSwitchBar mSwitchBar;
    private PopupWindow popupWindow;
    private List<EditionChildBean> mEditionChildBeanList;
    private int mLastSelectedPos = 0;

    private static final String SUBJECT_ID = "SUBJECT_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String EDITION_ID = "EDITION_ID";


    public static void invoke(Activity activity,String subjectId,String subjectName,String editionId){
        Intent intent = new Intent(activity,SelectChapterAndKnowledgeActivity.class);
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
                    mLayoutStage.setVisibility(View.VISIBLE);
                    if(mChapterAdapter == null){
                        getEditionList(mSubjectId);
                    }else {
                        mRecyclerView.setAdapter(mChapterAdapter);
                    }
                }else {
                    mLayoutStage.setVisibility(View.GONE);
                    if(mKnowledgePointAdapter == null){
                        getKnowledgePointList(mSubjectId);
                    }else {
                        mRecyclerView.setAdapter(mKnowledgePointAdapter);
                    }
                }
            }
        });

        mLayoutStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow ==null || (popupWindow != null && !popupWindow.isShowing())){
                    showPop(mEditionChildBeanList);
                }else {
                    dismissPop();
                }
            }
        });
    }

    private void initView() {
        mSwitchBar = (ChapterSwitchBar) findViewById(R.id.switchBar);
        mSwitchBar.setVisibility(View.INVISIBLE);
        mBack = findViewById(R.id.back);
        mStage = (TextView) findViewById(R.id.tv_stage);
        mLayoutStage = findViewById(R.id.ll_stage);
        mRootView = findViewById(R.id.root);
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

    private void showPop(final List<EditionChildBean> data){
        if(data == null && data.size() == 0)
            return;
        if(popupWindow == null){
            View view = LayoutInflater.from(this).inflate(R.layout.popwindow_stage,null);
            popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

            View stage = view.findViewById(R.id.ll_stage_pop);
            final TextView tvPop = (TextView) view.findViewById(R.id.tv_pop);
            data.get(0).setSelected(true);
            tvPop.setText(data.get(0).getName());

            ListView listView = (ListView) view.findViewById(R.id.list_view);
            final PopListAdapter adapter = new PopListAdapter(data);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position != mLastSelectedPos){
                        tvPop.setText(data.get(position).getName());
                        mStage.setText(data.get(position).getName());
                        mVolume = data.get(position).getId();
                        data.get(position).setSelected(true);
                        data.get(mLastSelectedPos).setSelected(false);
                        mLastSelectedPos = position;
                        adapter.notifyDataSetChanged();

                        dismissPop();
                        getChapterList(mSubjectId,mEditionId,mVolume);
                    }

                }
            });

            stage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPop();
                }
            });

            if(data.size() > 6){
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
                layoutParams.height = ScreenUtils.dpToPxInt(this,51 * 6);
                listView.setLayoutParams(layoutParams);
            }

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mLayoutStage.setVisibility(View.VISIBLE);
                }
            });
            popupWindow.setFocusable(true);
        }
        mLayoutStage.setVisibility(View.INVISIBLE);
        popupWindow.showAsDropDown(mRootView);

    }

    private void dismissPop(){
        if(popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(popupWindow !=null && popupWindow.isShowing()){
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    HttpCallback<EditionResponse> mEditionCallback = new EXueELianBaseCallback<EditionResponse>() {
        @Override
        protected void onResponse(RequestBase request, EditionResponse response) {
            if(response.getStatus().getCode() == 0){
                mEditionChildBeanList = response.getData().get(0).getChildren();
                mVolume = getVolume(response.getData(),mEditionId);
                mStage.setText(response.getData().get(0).getChildren().get(0).getName());
                getChapterList(mSubjectId,mEditionId,mVolume);
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
                if(!"1104".equals(mSubjectId)){
                    //英语不展示知识点切换
                    mSwitchBar.setVisibility(View.VISIBLE);
                }
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
