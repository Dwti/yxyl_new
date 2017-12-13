package com.yanxiu.gphone.student.mistakeredo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PickerViewEx;
import com.yanxiu.gphone.student.exercise.adapter.BaseExpandableRecyclerAdapter;
import com.yanxiu.gphone.student.mistakeredo.adapter.WrongQPointAdapter;
import com.yanxiu.gphone.student.mistakeredo.adapter.WrongQPointAdapter2;
import com.yanxiu.gphone.student.mistakeredo.bean.WrongQPointBean;
import com.yanxiu.gphone.student.mistakeredo.request.WrongQPointRequest;
import com.yanxiu.gphone.student.mistakeredo.response.WrongQPointResponse;
import com.yanxiu.gphone.student.user.mistake.activity.MistakeListActivity;
import com.yanxiu.gphone.student.user.mistake.response.MistakeDeleteMessage;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.anim.AlphaAnimationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MistakePointActivity extends YanxiuBaseActivity {

    private View mBack, mLayoutStage,mRootView,mTipsView,mOverlay;
    private TextView mTitle,mStage,mTips;
    private ImageView mTipsImg;
    private RecyclerView mRecyclerView;
    private String mSubjectName,mSubjectId,mStageId;
    private PopupWindow popupWindow;
    private Button mRefreshBtn;
    private WrongQPointAdapter mAdapter;
    private int mLastSelectedPos = 0;
    private int mCurrSelectedPos = 0;
    private int mDefaultStageIndex = 0 ;
    public static final String  SUBJECT_ID = "SUBJECT_ID";
    public static final String  SUBJECT_NAME = "SUBJECT_NAME";
    private List<String> mStageIds;
    private List<String> mStageNames;

    public static void invoke(Context context, String subjectId, String subjectName){
        Intent intent = new Intent(context,MistakePointActivity.class);
        intent.putExtra(SUBJECT_ID,subjectId);
        intent.putExtra(SUBJECT_NAME,subjectName);
        context.startActivity(intent);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_qpoint);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mOverlay = findViewById(R.id.overlay);
        mBack = findViewById(R.id.back);
        mStage = (TextView) findViewById(R.id.tv_stage);
        mLayoutStage = findViewById(R.id.ll_stage);
        mRootView = findViewById(R.id.root);
        mTitle = (TextView) findViewById(R.id.title);
        mTipsImg = (ImageView) findViewById(R.id.iv_tips);
        mTipsView = findViewById(R.id.tips_layout);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new WrongQPointAdapter(new ArrayList<WrongQPointBean>(0));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData(){
        mSubjectId = getIntent().getStringExtra(SUBJECT_ID);
        mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        mTitle.setText(mSubjectName);

        mStageId = LoginInfo.getStageid();
        mStageIds = Arrays.asList(Constants.StageId);
        mStageNames = new ArrayList<>(3);
        mStageNames.add(getString(R.string.primary_txt));
        mStageNames.add(getString(R.string.juinor_txt));
        mStageNames.add(getString(R.string.high_txt));
        mDefaultStageIndex = getSelectedStageIndex(mStageId);

        mStage.setText(mStageNames.get(mDefaultStageIndex));
        getWrongQPoints();
    }

    private void initListener(){

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayoutStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow == null || !popupWindow.isShowing()){
                    showPop();
                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseExpandableRecyclerAdapter.OnItemClickListener<WrongQPointBean>() {
            @Override
            public void onItemClick(View itemView, int position, WrongQPointBean node) {
                if(!node.hasChildren()){
                    //进入错题列表
                    MistakeListActivity.LaunchActivity(MistakePointActivity.this,node.getName(),mSubjectId,node.getQids());
                }
            }
        });
    }

    public void onEventMainThread(MistakeDeleteMessage message){
        if(message != null){
            getWrongQPoints();
        }
    }

    private int getSelectedStageIndex(String stageId){
        return mStageIds.indexOf(stageId);
    }
    private void getWrongQPoints(){
        WrongQPointRequest request = new WrongQPointRequest();
        request.setSubjectId(mSubjectId);
        request.setStageId(mStageId);
        request.startRequest(WrongQPointResponse.class, new EXueELianBaseCallback<WrongQPointResponse>() {
            @Override
            public void onResponse(RequestBase request, WrongQPointResponse ret) {
                if(ret.getStatus().getCode() == 0 ){
                    if(!ret.getData().isEmpty()){
                        showContentView();
                        mAdapter.replaceData(ret.getData());
                    }else {
                        showDataEmptyView();
                    }
                }else if(ret.getStatus().getCode() == 3){
                    showDataEmptyView();
                }else {
                    showDataErrorView();
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                showDataErrorView();
            }
        });
    }

    private void showContentView(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
    }

    private void showDataEmptyView(){
        mRecyclerView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.data_empty);
        mTips.setText("");
        mRefreshBtn.setText(R.string.click_to_refresh);
    }

    private void showDataErrorView(){
        mRecyclerView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showPop(){
        if(popupWindow == null){
            View view = LayoutInflater.from(this).inflate(R.layout.popwindow_stage,null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setAnimationStyle(R.style.pop_anim);

            final PickerViewEx picker = (PickerViewEx) view.findViewById(R.id.picker_view);
            picker.setTextLocation(PickerViewEx.DEFAULT_CENTER);
            View pop_bg = view.findViewById(R.id.pop_bg);
            View tvOk = view.findViewById(R.id.tv_ok);
            View tvCancel = view.findViewById(R.id.tv_cancel);
            picker.setData(mStageNames);
            picker.setSelected(mDefaultStageIndex);
            mLastSelectedPos = mDefaultStageIndex;
            mCurrSelectedPos = mDefaultStageIndex;

            pop_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPop();
                }
            });

            picker.setOnSelectListener(new PickerViewEx.onSelectListener() {
                @Override
                public void onSelect(View view, String text, int selectId) {
                    mCurrSelectedPos = selectId;
                }
            });

            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mLastSelectedPos != mCurrSelectedPos){
                        mLastSelectedPos = mCurrSelectedPos;
                        mStage.setText(mStageNames.get(mCurrSelectedPos));
                        mStageId = mStageIds.get(mCurrSelectedPos);
                        dismissPop();
                        getWrongQPoints();
                    }else {
                        dismissPop();
                    }
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPop();
                }
            });

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mCurrSelectedPos = mLastSelectedPos;
                    picker.setSelected(mCurrSelectedPos);
                }
            });

        }
        mOverlay.setVisibility(View.VISIBLE);
        AlphaAnimationUtil.startPopBgAnimIn(mOverlay);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void dismissPop(){
        AlphaAnimationUtil.startPopBgAnimExit(mOverlay);
        mOverlay.setVisibility(View.GONE);
        if(popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }
}
