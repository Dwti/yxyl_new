package com.yanxiu.gphone.student.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PickerViewEx;
import com.yanxiu.gphone.student.exercise.bean.EditionBeanEx;
import com.yanxiu.gphone.student.exercise.request.EditionRequest;
import com.yanxiu.gphone.student.exercise.request.SaveEditionRequest;
import com.yanxiu.gphone.student.exercise.response.EditionResponse;
import com.yanxiu.gphone.student.exercise.response.SaveEditionResponse;
import com.yanxiu.gphone.student.learning.LearningEditionSelectChangeMessage;
import com.yanxiu.gphone.student.learning.request.LearningSaveEditionRequest;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sp on 17-7-26.
 */

public class ModifyEditionActivity extends YanxiuBaseActivity {

    private TextView mTips, mSubject,mDes;
    private View mTipsView, mContent;
    private Button mRefreshBtn;
    private ImageView mIcon, mBack;
    private ImageView mTipsImg;
    private PickerViewEx mPickerView;
    private Button mBtnOk;
    private String mSubjectId = "",mSubjectName;
    private int mSelectedIndex;
    private String mDefaultEditionName;
    private boolean mIsSavingEdition = false;
    private List<EditionBeanEx> editions;
    private static final String SUBJECT_ID = "subjectId";
    private static final String SUBJECT_NAME = "subjectName";
    private static final String EDITION_NAME = "editionName";
    private static final String COME_FROM = "comeFrom";
    public static final int FROM_EXERCISE = 0x01;
    public static final int FROM_SUBJECT_SELECT = 0x02;
    public static final int FROM_LEARNING = 0x03;
    private int mComeFrom;

    public static void invoke(Activity activity, String subjectId, String subjectName, String editionName, int comeFrom) {
        Intent intent = new Intent(activity, ModifyEditionActivity.class);
        intent.putExtra(SUBJECT_ID, subjectId);
        intent.putExtra(SUBJECT_NAME, subjectName);
        intent.putExtra(COME_FROM, comeFrom);
        intent.putExtra(EDITION_NAME,editionName);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ScreenUtils.getScreenHeight(this) < 900) {
            setContentView(R.layout.activity_select_edition_small);
        } else {
            setContentView(R.layout.activity_select_edition);
        }
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mContent = findViewById(R.id.ll_content);
        mTipsView = findViewById(R.id.tips_layout);
        mRefreshBtn = (Button) findViewById(R.id.btn_refresh);
        mTipsImg = (ImageView) findViewById(R.id.iv_tips);
        mTips = (TextView) findViewById(R.id.tv_tips);
        mSubject = (TextView) findViewById(R.id.subject);
        mIcon = (ImageView) findViewById(R.id.icon);
        mBack = (ImageView) findViewById(R.id.back);
        mPickerView = (PickerViewEx) findViewById(R.id.picker_view);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mDes = (TextView) findViewById(R.id.tv_des);
        mPickerView.setTextLocation(PickerViewEx.DEFAULT_CENTER);
    }

    private void initData() {
        mSubjectId = getIntent().getStringExtra(SUBJECT_ID);
        mDefaultEditionName = getIntent().getStringExtra(EDITION_NAME);
        mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        mComeFrom = getIntent().getIntExtra(COME_FROM, 0);
        mSubject.setText(mSubjectName);
        if (mIcon != null)
            setIcon(mIcon, mSubjectId);
        mDes.setVisibility(View.INVISIBLE);
        if(mComeFrom == FROM_SUBJECT_SELECT) {
            requestEditions(mSubjectId);
        } else if(mComeFrom == FROM_LEARNING)  {
            requestLearningEditions(mSubjectId);
        }
    }

    private void initListener() {
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEditions(mSubjectId);
            }
        });

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedIndex == mPickerView.getSelectedIndex()){
                    finish();
                    return;
                }
                String editionId = editions.get(mPickerView.getSelectedIndex()).getId();
                if(mComeFrom == FROM_SUBJECT_SELECT) {
                    saveEdition(mSubjectId, editionId);
                } else if(mComeFrom == FROM_LEARNING)  {
                    saveLearningEdition(mSubjectId, editionId);
                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDataEmptyView() {
        mContent.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTips.setText(R.string.data_empty);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showDataErrorView() {
        mContent.setVisibility(View.GONE);
        mTipsView.setVisibility(View.VISIBLE);
        mTipsImg.setImageResource(R.drawable.net_error);
        mTips.setText(R.string.load_failed);
        mRefreshBtn.setText(R.string.click_to_retry);
    }

    private void showContent(List<EditionBeanEx> editionList) {
        mContent.setVisibility(View.VISIBLE);
        mTipsView.setVisibility(View.GONE);
        editions = editionList;
        mPickerView.setData(getEditionStrs(editions));
        if (TextUtils.isEmpty(mDefaultEditionName)){
            mPickerView.setSelected(0);
        }else {
            mPickerView.setSelected(mDefaultEditionName);
        }
        mSelectedIndex = getDefaultSelectedIndex(editionList, mDefaultEditionName);
    }

    private void requestEditions(String subjectId) {
        EditionRequest request = new EditionRequest();
        request.setSubjectId(subjectId);
        request.startRequest(EditionResponse.class, mGetEditionsCallback);
    }

    private void requestLearningEditions(String subjectId) {
        EditionRequest request = new EditionRequest();
        request.setSubjectId(subjectId);
        request.startRequest(EditionResponse.class, mGetEditionsCallback);
    }

    private void saveEdition(String subjectId, String editionId) {
        if (mIsSavingEdition)
            return;
        mIsSavingEdition = true;
        SaveEditionRequest request = new SaveEditionRequest();
        request.setSubjectId(subjectId);
        request.setBeditionId(editionId);
        request.startRequest(SaveEditionResponse.class, mSaveEditionCallback);
    }

    private void saveLearningEdition(String subjectId, String editionId) {
        if (mIsSavingEdition)
            return;
        mIsSavingEdition = true;
        LearningSaveEditionRequest request = new LearningSaveEditionRequest();
        request.setSubjectId(subjectId);
        request.setBeditionId(editionId);
        request.startRequest(SaveEditionResponse.class, mSaveEditionCallback);
    }

    private List<String> getEditionStrs(List<EditionBeanEx> editions) {
        List<String> result = new ArrayList<>();
        for (EditionBeanEx bean : editions) {
            result.add(bean.getName());
        }
        return result;
    }

    private int getDefaultSelectedIndex(List<EditionBeanEx> editionList, String editionName) {
        for (int i = 0; i < editionList.size(); i++) {
            if (editionList.get(i).getName().equals(editionName)) {
                return i;
            }
        }
        return -1;
    }

    private void sendEditionChangeMsg() {
        EditionSelectChangeMessage message = new EditionSelectChangeMessage();
        EventBus.getDefault().post(message);
    }

    private void sendLearningEditionChangeMsg() {
        LearningEditionSelectChangeMessage message = new LearningEditionSelectChangeMessage();
        EventBus.getDefault().post(message);
    }

    HttpCallback<EditionResponse> mGetEditionsCallback = new EXueELianBaseCallback<EditionResponse>() {
        @Override
        protected void onResponse(RequestBase request, EditionResponse response) {
            if (response.getStatus().getCode() == 0) {
                showContent(response.getData());
            } else if (response.getData().size() == 0) {
                showDataEmptyView();
            } else {
                showDataErrorView();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            showDataErrorView();
        }
    };

    HttpCallback<SaveEditionResponse> mSaveEditionCallback = new EXueELianBaseCallback<SaveEditionResponse>() {
        @Override
        public void onSuccess(RequestBase request, SaveEditionResponse ret) {
            super.onSuccess(request, ret);
            mIsSavingEdition = false;
        }

        @Override
        protected void onResponse(RequestBase request, SaveEditionResponse response) {
            if (response.getStatus().getCode() == 0) {
                mSelectedIndex = mPickerView.getSelectedIndex();
                ToastManager.showMsg(response.getStatus().getDesc());
                if(mComeFrom == FROM_SUBJECT_SELECT) {
                    sendEditionChangeMsg();
                } else if(mComeFrom == FROM_LEARNING)  {
                    sendLearningEditionChangeMsg();
                }
                finish();
            } else {
                ToastManager.showMsg(response.getStatus().getDesc());
            }
            mIsSavingEdition = false;
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
            mIsSavingEdition = false;
        }
    };

    private void setIcon(ImageView imageView, String subjectId) {
        switch (subjectId) {
            case "1102":
                imageView.setImageResource(R.drawable.select_yuwen);
                break;
            case "1103":
                imageView.setImageResource(R.drawable.select_shuxue);
                break;
            case "1104":
                imageView.setImageResource(R.drawable.select_yingyu);
                break;
            case "1105":
                imageView.setImageResource(R.drawable.select_wuli);
                break;
            case "1106":
                imageView.setImageResource(R.drawable.select_huaxue);
                break;
            case "1107":
                imageView.setImageResource(R.drawable.select_shengwu);
                break;
            case "1108":
                imageView.setImageResource(R.drawable.select_dili);
                break;
            case "1109":
                imageView.setImageResource(R.drawable.select_zhengzhi);
                break;
            case "1110":
                imageView.setImageResource(R.drawable.select_lishi);
                break;
            default:
                break;
        }
    }
}
