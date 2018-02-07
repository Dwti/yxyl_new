package com.yanxiu.gphone.student.learning.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.ChapterSwitchBar;
import com.yanxiu.gphone.student.customviews.PickerViewEx;
import com.yanxiu.gphone.student.exercise.adapter.BaseExpandableRecyclerAdapter;
import com.yanxiu.gphone.student.exercise.bean.ChapterBean;
import com.yanxiu.gphone.student.exercise.bean.EditionChildBean;
import com.yanxiu.gphone.student.exercise.bean.KnowledgePointBean;
import com.yanxiu.gphone.student.exercise.request.GenQuesRequest;
import com.yanxiu.gphone.student.exercise.response.ChapterListResponse;
import com.yanxiu.gphone.student.exercise.response.GetVolumeResponse;
import com.yanxiu.gphone.student.exercise.response.KnowledgePointResponse;
import com.yanxiu.gphone.student.exercise.response.SaveVolumeResponse;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.learning.adapter.SpecialAdapter;
import com.yanxiu.gphone.student.learning.adapter.SyncAdapter;
import com.yanxiu.gphone.student.learning.bean.ChannelChildBean;
import com.yanxiu.gphone.student.learning.request.GetChannelRequest;
import com.yanxiu.gphone.student.learning.request.GetLearningVolumesRequest;
import com.yanxiu.gphone.student.learning.request.GetTreeRequest;
import com.yanxiu.gphone.student.learning.request.SaveLearningVolumeRequest;
import com.yanxiu.gphone.student.learning.request.GetResourceListDataRequest;
import com.yanxiu.gphone.student.learning.response.GetChannelResponse;
import com.yanxiu.gphone.student.learning.response.GetResourceListDataResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.util.anim.AlphaAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufengqing on 2018/1/15.
 */

public class SelectSyncAndSpecailActivity extends YanxiuBaseActivity {
    private View mBack, mLayoutStage, mRootView, mToolBar, mTipsView, mOverlay;
    private TextView mTitle, mStage, mTips;
    private ImageView mTipsImg;
    private RecyclerView mRecyclerView;
    private SyncAdapter mSyncAdapter;
    private SpecialAdapter mSpecialAdapter;
    private String mSubjectName, mSubjectId, mEditionId, mVolumeId;
    private ChapterSwitchBar mSwitchBar;
    private PopupWindow popupWindow;
    private Button mRefreshBtn;
    private List<EditionChildBean> mEditionChildBeanList;
    private int mLastSelectedPos = 0;
    private int mCurrSelectedPos = 0;
    private int mDefaultVolumeIndex = 0;
    private boolean mIsSyncMode = true;  //当前选中的是否是同步
    private boolean mNoEditions = true; //第一次进来是否请求Editions数据失败

    private static final String SUBJECT_ID = "SUBJECT_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String EDITION_ID = "EDITION_ID";
    private List<ChannelChildBean> mChannelList;
    private boolean mHasgetChannel = false;
    private HttpCallback<GetChannelResponse> mGetChannelCallback = new HttpCallback<GetChannelResponse>() {
        @Override
        public void onSuccess(RequestBase request, GetChannelResponse ret) {
            if (ret.getStatus().getCode() == 0) {
                mHasgetChannel = true;
                mChannelList = ret.getData();
                if(mChannelList.size() == 1) {
                    mSwitchBar.setVisibility(View.GONE);
                    if(TextUtils.equals(mChannelList.get(0).getName(),"专题")) {
                        mIsSyncMode = false;
                        mLayoutStage.setVisibility(View.GONE);
                        getSpecialTree(mSubjectId);
                    } else {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLayoutStage.getLayoutParams();
                        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
                        mLayoutStage.setLayoutParams(params);
                        getSyncVolumes();
                    }
                } else {
                    mSwitchBar.setVisibility(View.VISIBLE);
                    if(mIsSyncMode) {
                        getSyncVolumes();
                        mLayoutStage.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                showDataErrorView(true);
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView(true);
        }
    };

    public static void invoke(Activity activity, String subjectId, String subjectName, String editionId) {
        Intent intent = new Intent(activity, SelectSyncAndSpecailActivity.class);
        intent.putExtra(SUBJECT_ID, subjectId);
        intent.putExtra(SUBJECT_NAME, subjectName);
        intent.putExtra(EDITION_ID, editionId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
                if (isOff) {
                    mIsSyncMode = true;
                    showContentView();
                    mLayoutStage.setVisibility(View.VISIBLE);
                    mRecyclerView.setAdapter(mSyncAdapter);
                    if (mSyncAdapter.getItemCount() == 0) {
                        getSyncVolumes();
                    }
                } else {
                    mIsSyncMode = false;
                    showContentView();
                    mLayoutStage.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(mSpecialAdapter);
                    if (mSpecialAdapter.getItemCount() == 0) {
                        getSpecialTree(mSubjectId);
                    }
                }
            }
        });

        mLayoutStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });

        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mHasgetChannel) {
                    getChannel();
                    return;
                }
                if (mIsSyncMode) {
                    mSyncAdapter.clearData();
                } else {
                    mSpecialAdapter.clearData();
                }
                showContentView();
                if (mNoEditions) {
                    getSyncVolumes();
                } else {
                    loadData();
                }
            }
        });

        mSyncAdapter.setOnItemClickListener(new BaseExpandableRecyclerAdapter.OnItemClickListener<ChapterBean>() {
            @Override
            public void onItemClick(View itemView, int position, ChapterBean node) {
                String chapterId = null;
//                if (node.getLevel() == 0) {
                    chapterId = node.getId();
//                } else if (node.getLevel() == 1) {
//                    chapterId = node.getParent().getId();
//                } else if (node.getLevel() == 2) {
//                    chapterId = node.getParent().getParent().getId();
//                }
                openVideoListActivity("1", chapterId, node.getName());
            }
        });

        mSpecialAdapter.setOnItemClickListener(new BaseExpandableRecyclerAdapter.OnItemClickListener<KnowledgePointBean>() {
            @Override
            public void onItemClick(View itemView, int position, KnowledgePointBean node) {
                String chapterId = null;
//                if (node.getLevel() == 0) {
                    chapterId = node.getId();
//                } else if (node.getLevel() == 1) {
//                    chapterId = node.getParent().getId();
//                } else if (node.getLevel() == 2) {
//                    chapterId = node.getParent().getParent().getId();
//                }
                openVideoListActivity("2", chapterId, node.getName());
            }
        });
    }

    private void initView() {
        mOverlay = findViewById(R.id.overlay);
        mSwitchBar = (ChapterSwitchBar) findViewById(R.id.switchBar);
        mSwitchBar.setOnText(R.string.special_title);
        mSwitchBar.setOffText(R.string.sync_title);
        mBack = findViewById(R.id.back);
        mToolBar = findViewById(R.id.rl_tool_bar);
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

        mSyncAdapter = new SyncAdapter(new ArrayList<ChapterBean>(0));
        mSpecialAdapter = new SpecialAdapter(new ArrayList<KnowledgePointBean>());
        mRecyclerView.setAdapter(mSyncAdapter);
    }

    private void initData() {
        mSubjectId = getIntent().getStringExtra(SUBJECT_ID);
        mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        mEditionId = getIntent().getStringExtra(EDITION_ID);
        mTitle.setText(mSubjectName);
        getChannel();
    }

    private void getChannel() {
        GetChannelRequest request = new GetChannelRequest();
        request.startRequest(GetChannelResponse.class, mGetChannelCallback);
    }

    private void loadData() {
        if (mIsSyncMode) {
            getSyncTree(mSubjectId, mEditionId, mVolumeId);
        } else {
            getSpecialTree(mSubjectId);
        }
    }

    private void showPop() {
        if (mEditionChildBeanList == null || mEditionChildBeanList.size() == 0)
            return;
        if (popupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.popwindow_stage, null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setAnimationStyle(R.style.pop_anim);

            final PickerViewEx picker = (PickerViewEx) view.findViewById(R.id.picker_view);
            picker.setTextLocation(PickerViewEx.DEFAULT_CENTER);
            View pop_bg = view.findViewById(R.id.pop_bg);
            View tvOk = view.findViewById(R.id.tv_ok);
            View tvCancel = view.findViewById(R.id.tv_cancel);
            picker.setData(getEditionStrs(mEditionChildBeanList));
            picker.setSelected(mDefaultVolumeIndex);
            mLastSelectedPos = mDefaultVolumeIndex;
            mCurrSelectedPos = mDefaultVolumeIndex;

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
                    if (mLastSelectedPos != mCurrSelectedPos) {
                        mLastSelectedPos = mCurrSelectedPos;
                        mVolumeId = mEditionChildBeanList.get(mCurrSelectedPos).getId();
                        mStage.setText(mEditionChildBeanList.get(mCurrSelectedPos).getName());
                        dismissPop();
                        saveVolume(mVolumeId);
                        getSyncTree(mSubjectId, mEditionId, mVolumeId);
                    } else {
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

    private List<String> getEditionStrs(List<EditionChildBean> list) {
        List<String> result = new ArrayList<>();
        for (EditionChildBean bean : list) {
            result.add(bean.getName());
        }
        return result;
    }

    protected void openVideoListActivity(String channel, String chapterId, String name) {
        VideoListActivity.invoke(this, channel, chapterId, name);
    }

    private void dismissPop() {
        AlphaAnimationUtil.startPopBgAnimExit(mOverlay);
        mOverlay.setVisibility(View.GONE);
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    private void showContentView() {
        if(mIsSyncMode) {
            mToolBar.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
    }

    private void showDataErrorView(boolean hideToolBar) {
        if (hideToolBar) {
            mToolBar.setVisibility(View.GONE);
        }
        mRecyclerView.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void getSyncTree(String subjectId, String editionId, String volume) {
        GetTreeRequest request = new GetTreeRequest();
        request.setSubjectId(subjectId);
        request.setChannel("1");
        request.setVolumeId(volume);
        request.startRequest(ChapterListResponse.class, mSyncCallback);
    }

    private void getSpecialTree(String subjectId) {
        GetTreeRequest request = new GetTreeRequest();
        request.setSubjectId(subjectId);
        request.setChannel("2");
        request.startRequest(KnowledgePointResponse.class, mSpecialCallback);
    }

    private void getSyncVolumes() {
        GetLearningVolumesRequest request = new GetLearningVolumesRequest();
        request.setSubjectId(mSubjectId);
        request.setEditionId(mEditionId);
        request.startRequest(GetVolumeResponse.class, mGetVolumeCallback);
    }

    private void saveVolume(String volumeId) {
        SaveLearningVolumeRequest request = new SaveLearningVolumeRequest();
        request.setSubjectId(mSubjectId);
        request.setVolumeId(volumeId);
        request.startRequest(SaveVolumeResponse.class, mSaveVolumeCallback);
    }

    private void initVolumeId(List<EditionChildBean> list) {
        EditionChildBean bean;
        for (int i = 0; i < list.size(); i++) {
            bean = list.get(i);
            if ("1".equals(bean.getSelected())) {
                mDefaultVolumeIndex = i;
                mVolumeId = bean.getId();
                mStage.setText(bean.getName());
                return;
            }
        }
        mDefaultVolumeIndex = 0;
        mVolumeId = list.get(0).getId();
        mStage.setText(list.get(0).getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        mOverlay.clearAnimation();
    }

    HttpCallback<ChapterListResponse> mSyncCallback = new EXueELianBaseCallback<ChapterListResponse>() {
        @Override
        protected void onResponse(RequestBase request, ChapterListResponse response) {
            if (response.getStatus().getCode() == 0) {
                showContentView();
                mSyncAdapter.replaceData(response.getData());
            } else {
                showDataErrorView(false);
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView(false);
        }
    };

    HttpCallback<KnowledgePointResponse> mSpecialCallback = new EXueELianBaseCallback<KnowledgePointResponse>() {
        @Override
        protected void onResponse(RequestBase request, KnowledgePointResponse response) {
            if (response.getStatus().getCode() == 0) {
                showContentView();
                mRecyclerView.setAdapter(mSpecialAdapter);
                mSpecialAdapter.replaceData(response.getData());
            } else {
                showDataErrorView(false);
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView(false);
        }
    };

    HttpCallback<GetVolumeResponse> mGetVolumeCallback = new HttpCallback<GetVolumeResponse>() {
        @Override
        public void onSuccess(RequestBase request, GetVolumeResponse ret) {
            if (ret.getStatus().getCode() == 0) {
                mNoEditions = false;
                mEditionChildBeanList = ret.getData();
                initVolumeId(mEditionChildBeanList);
                showContentView();
                getSyncTree(mSubjectId, mEditionId, mVolumeId);
            } else {
                showDataErrorView(true);
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView(true);
        }
    };

    HttpCallback<SaveVolumeResponse> mSaveVolumeCallback = new HttpCallback<SaveVolumeResponse>() {
        @Override
        public void onSuccess(RequestBase request, SaveVolumeResponse ret) {

        }

        @Override
        public void onFail(RequestBase request, Error error) {
        }
    };

}
