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
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.ChapterSwitchBar;
import com.yanxiu.gphone.student.exercise.adapter.BaseExpandableRecyclerAdapter;
import com.yanxiu.gphone.student.exercise.adapter.ChapterAdapter;
import com.yanxiu.gphone.student.exercise.adapter.KnowledgePointAdapter;
import com.yanxiu.gphone.student.exercise.adapter.PopListAdapter;
import com.yanxiu.gphone.student.exercise.bean.ChapterBean;
import com.yanxiu.gphone.student.exercise.bean.EditionBeanEx;
import com.yanxiu.gphone.student.exercise.bean.EditionChildBean;
import com.yanxiu.gphone.student.exercise.bean.KnowledgePointBean;
import com.yanxiu.gphone.student.exercise.request.EditionRequest;
import com.yanxiu.gphone.student.exercise.request.ChapterListRequest;
import com.yanxiu.gphone.student.exercise.request.GenQuesByChapterRequest;
import com.yanxiu.gphone.student.exercise.request.GenQuesByKnpointRequest;
import com.yanxiu.gphone.student.exercise.request.GenQuesRequest;
import com.yanxiu.gphone.student.exercise.request.KnowledgePointRequest;
import com.yanxiu.gphone.student.exercise.response.ChapterListResponse;
import com.yanxiu.gphone.student.exercise.response.EditionResponse;
import com.yanxiu.gphone.student.exercise.response.KnowledgePointResponse;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
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
    private String mSubjectName,mSubjectId,mEditionId, mVolumeId;
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
                    mRecyclerView.setAdapter(mChapterAdapter);
                    if(mChapterAdapter.getItemCount() == 0){
                        getEditionList(mSubjectId);
                    }
                }else {
                    mLayoutStage.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(mKnowledgePointAdapter);
                    if(mKnowledgePointAdapter.getItemCount() == 0){
                        getKnowledgePointList(mSubjectId);
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

        mChapterAdapter.setOnItemClickListener(new BaseExpandableRecyclerAdapter.OnItemClickListener<ChapterBean>() {
            @Override
            public void onItemClick(View itemView, int position, ChapterBean node) {
                GenQuesByChapterRequest request = new GenQuesByChapterRequest();
                request.setSubjectId(mSubjectId);
                request.setEditionId(mEditionId);
                request.setVolumeId(mVolumeId);
                request.bodyDealer = new DESBodyDealer();
                if(node.getLevel() == 0){
                    request.setChapterId(node.getId());
                }else if(node.getLevel() == 1){
                    request.setChapterId(node.getParent().getId());
                    request.setSectionId(node.getId());
                }else if(node.getLevel() == 2){
                    request.setChapterId(node.getParent().getParent().getId());
                    request.setSectionId(node.getParent().getId());
                    request.setCellId(node.getId());
                }
                request.startRequest(PaperResponse.class,mGenQuesByChapterCallback);
            }
        });

        mKnowledgePointAdapter.setOnItemClickListener(new BaseExpandableRecyclerAdapter.OnItemClickListener<ChapterBean>() {
            @Override
            public void onItemClick(View itemView, int position, ChapterBean node) {
                GenQuesByKnpointRequest request = new GenQuesByKnpointRequest();
                request.setSubjectId(mSubjectId);
                request.bodyDealer = new DESBodyDealer();
                if(node.getLevel() == 0){
                    request.setKnpId1(node.getId());
                }else if(node.getLevel() == 1){
                    request.setKnpId1(node.getParent().getId());
                    request.setKnpId2(node.getId());
                }else if(node.getLevel() == 2){
                    request.setKnpId1(node.getParent().getParent().getId());
                    request.setKnpId2(node.getParent().getId());
                    request.setKnpId3(node.getId());
                }else if(node.getLevel() == 3){
                    request.setKnpId1(node.getParent().getParent().getParent().getId());
                    request.setKnpId2(node.getParent().getParent().getId());
                    request.setKnpId3(node.getParent().getId());
                    request.setKnpId4(node.getId());
                }
                request.startRequest(PaperResponse.class,mGenQuesByKnpointCallback);
            }
        });
    }

    private void initView() {
        mSwitchBar = (ChapterSwitchBar) findViewById(R.id.switchBar);
        mBack = findViewById(R.id.back);
        mStage = (TextView) findViewById(R.id.tv_stage);
        mLayoutStage = findViewById(R.id.ll_stage);
        mRootView = findViewById(R.id.root);
        mTitle = (TextView) findViewById(R.id.title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mChapterAdapter = new ChapterAdapter(new ArrayList<ChapterBean>(0));
        mKnowledgePointAdapter = new KnowledgePointAdapter(new ArrayList<KnowledgePointBean>());

        mRecyclerView.setAdapter(mChapterAdapter);
    }

    private void initData(){
        mSubjectId = getIntent().getStringExtra(SUBJECT_ID);
        mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        mEditionId = getIntent().getStringExtra(EDITION_ID);
        mTitle.setText(mSubjectName);

        setSwitchBarVisibility(mSubjectId);

        getEditionList(mSubjectId);
    }

    private void showPop(final List<EditionChildBean> data){
        if(data == null || data.size() == 0)
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
                        mVolumeId = data.get(position).getId();
                        data.get(position).setSelected(true);
                        data.get(mLastSelectedPos).setSelected(false);
                        mLastSelectedPos = position;
                        adapter.notifyDataSetChanged();

                        dismissPop();
                        getChapterList(mSubjectId,mEditionId, mVolumeId);
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

    protected void openAnswerQuestionUI(String key,GenQuesRequest request){
        AnswerQuestionActivity.invoke(this,key, Constants.MAINAVTIVITY_FROMTYPE_EXERCISE,request);
    }

    private void dismissPop(){
        if(popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    private void setSwitchBarVisibility(String subjectId) {
        switch (subjectId){
            //非数理化生科目，不展示章节知识点切换（只有章节）
            case "1102":
            case "1104":
            case "1108":
            case "1109":
            case "1110":
                mSwitchBar.setVisibility(View.GONE);
                break;
            default:
                break;
        }
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
                mVolumeId = getVolume(response.getData(),mEditionId);
                mStage.setText(response.getData().get(0).getChildren().get(0).getName());
                getChapterList(mSubjectId,mEditionId, mVolumeId);
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
                mChapterAdapter.replaceData(response.getData());
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
                mKnowledgePointAdapter.replaceData(response.getData());
            }else {
                ToastManager.showMsg(response.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };

    HttpCallback<PaperResponse> mGenQuesByChapterCallback = new EXueELianBaseCallback<PaperResponse>() {
        @Override
        public void onResponse(RequestBase request, PaperResponse ret) {
            if(ret.getStatus().getCode() == 0){
                Paper paper = new Paper(ret.getData().get(0), QuestionShowType.ANSWER);
                DataFetcher.getInstance().save(paper.getId(),paper);
                openAnswerQuestionUI(paper.getId(), (GenQuesRequest) request);
            }else {
                ToastManager.showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };

    HttpCallback<PaperResponse> mGenQuesByKnpointCallback = new EXueELianBaseCallback<PaperResponse>() {
        @Override
        public void onResponse(RequestBase request, PaperResponse ret) {
            if(ret.getStatus().getCode() == 0){
                Paper paper = new Paper(ret.getData().get(0), QuestionShowType.ANSWER);
                DataFetcher.getInstance().save(paper.getId(),paper);
                openAnswerQuestionUI(paper.getId(), (GenQuesRequest) request);
            }else {
                ToastManager.showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };
}
