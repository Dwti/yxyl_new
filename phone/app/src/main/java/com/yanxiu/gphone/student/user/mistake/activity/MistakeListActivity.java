package com.yanxiu.gphone.student.user.mistake.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.user.mistake.fragment.MistakeChapterFragment;
import com.yanxiu.gphone.student.user.mistake.fragment.MistakeAllFragment;
import com.yanxiu.gphone.student.user.mistake.fragment.MistakeKongledgeFragment;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 16:04.
 * Function :
 */
public class MistakeListActivity extends YanxiuBaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private static final String MISTAKE_ALL = "all";
    private static final String MISTAKE_CHAPTER = "chapter";
    private static final String MISTAKE_KONGLEDGE = "kongledge";

    public static final String TITLE = "title";
    public static final String WRONGNUM = "wrongNum";
    public static final String SUBJECTID = "subjectId";
    public static final String EDITIONID = "editionId";
    public static final String STAGEID = "stageId";
    public static final String QIDS = "qids";

    private RadioGroup mClassifyView;
    private RadioButton mAllView;
    private RadioButton mKongledgeView;
    private ImageView mBackView;
    private TextView mTitleView;
    private View mTopView;
    private String mTitle;
    private String mSubjectId;
    private int mWrongNum;
    private String mEditionId;
    private String mStageId;
    private ArrayList<String> mQids;
    private FragmentManager mManager = getSupportFragmentManager();

    public static void LaunchActivity(Context context, String title, String subjectId,ArrayList<String> qids) {
        Intent intent = new Intent(context, MistakeListActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(SUBJECTID, subjectId);
        intent.putExtra(WRONGNUM, qids.size());
        intent.putStringArrayListExtra(QIDS,qids);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PublicLoadLayout rootView = new PublicLoadLayout(this);
        rootView.setContentView(R.layout.activity_mistakeclassify);
        setContentView(rootView);
        mTitle = getIntent().getStringExtra(TITLE);
        mSubjectId = getIntent().getStringExtra(SUBJECTID);
        mWrongNum = getIntent().getIntExtra(WRONGNUM, 0);
        mEditionId = getIntent().getStringExtra(EDITIONID);
        mQids = getIntent().getStringArrayListExtra(QIDS);
        mStageId = LoginInfo.getStageid();
        initView();
        initFragment();
        listener();
        initData();
    }

    private void initView() {
        mTopView=findViewById(R.id.include_top);
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);

        mClassifyView = (RadioGroup) findViewById(R.id.rg_classify);
        mAllView = (RadioButton) findViewById(R.id.rb_all);
        mKongledgeView = (RadioButton) findViewById(R.id.rb_kongledge);
    }

    private void initFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(STAGEID, mStageId);
        bundle.putString(SUBJECTID, mSubjectId);
        bundle.putInt(WRONGNUM, mWrongNum);
        bundle.putStringArrayList(QIDS,mQids);
        bundle.putString(TITLE,mTitle);

        FragmentTransaction transaction = mManager.beginTransaction();
        MistakeChapterFragment chapterFragment = new MistakeChapterFragment();
        MistakeKongledgeFragment kongledgeFragment = new MistakeKongledgeFragment();
        MistakeAllFragment completeFragment = new MistakeAllFragment();
        chapterFragment.setArguments(bundle);
        kongledgeFragment.setArguments(bundle);
        completeFragment.setArguments(bundle);

        if (mStageId.equals(Constants.StageId[0]) || mStageId.equals(Constants.StageId[1])) {
            if (mTitle.equals(getText(R.string.mistake_redo_math))) {
                transaction.add(R.id.fl_content, kongledgeFragment, MISTAKE_KONGLEDGE);
                transaction.add(R.id.fl_content, chapterFragment, MISTAKE_CHAPTER);
            } else if (mTitle.equals(getText(R.string.mistake_redo_english))) {
                transaction.add(R.id.fl_content, chapterFragment, MISTAKE_CHAPTER);
                mKongledgeView.setVisibility(View.INVISIBLE);
            } else {
                mClassifyView.setVisibility(View.GONE);
            }
        } else {
            mClassifyView.setVisibility(View.GONE);
        }
        transaction.add(R.id.fl_content, completeFragment, MISTAKE_ALL);
        transaction.commit();
        mManager.executePendingTransactions();
    }

    private void listener() {
        mClassifyView.setOnCheckedChangeListener(MistakeListActivity.this);
        mBackView.setOnClickListener(MistakeListActivity.this);
    }

    private void initData() {
        // TODO: 2017/7/20 隐藏章节知识点
        mClassifyView.setVisibility(View.GONE);

        mTitleView.setText(mTitle);
        mBackView.setVisibility(View.VISIBLE);
        mAllView.setChecked(true);
        mTopView.setBackgroundColor(Color.WHITE);

        mTitleView.setTextColor(ContextCompat.getColor(this,R.color.color_666666));
        mBackView.setBackgroundResource(R.drawable.selector_back);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction transaction = mManager.beginTransaction();
        Fragment allFragment = mManager.findFragmentByTag(MISTAKE_ALL);
        Fragment chapterFragment = mManager.findFragmentByTag(MISTAKE_CHAPTER);
        Fragment kongledgeFragment = mManager.findFragmentByTag(MISTAKE_KONGLEDGE);
        switch (checkedId) {
            case R.id.rb_all:
                if (allFragment != null) {
                    transaction.show(allFragment);
                    if (chapterFragment != null) {
                        transaction.hide(chapterFragment);
                    }
                    if (kongledgeFragment != null) {
                        transaction.hide(kongledgeFragment);
                    }
                }
                break;
            case R.id.rb_chapter:
                if (chapterFragment != null) {
                    transaction.show(chapterFragment);
                    if (allFragment != null) {
                        transaction.hide(allFragment);
                    }
                    if (kongledgeFragment != null) {
                        transaction.hide(kongledgeFragment);
                    }
                }
                break;
            case R.id.rb_kongledge:
                if (kongledgeFragment != null) {
                    transaction.show(kongledgeFragment);
                    if (chapterFragment != null) {
                        transaction.hide(chapterFragment);
                    }
                    if (allFragment != null) {
                        transaction.hide(allFragment);
                    }
                }
                break;
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                this.finish();
                break;
        }
    }
}
