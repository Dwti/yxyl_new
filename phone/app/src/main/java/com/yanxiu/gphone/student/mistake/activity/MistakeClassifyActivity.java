package com.yanxiu.gphone.student.mistake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.mistake.fragment.MistakeChapterFragment;
import com.yanxiu.gphone.student.mistake.fragment.MistakeAllFragment;
import com.yanxiu.gphone.student.mistake.fragment.MistakeKongledgeFragment;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 16:04.
 * Function :
 */
public class MistakeClassifyActivity extends YanxiuBaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private static final String MISTAKE_ALL= "all";
    private static final String MISTAKE_CHAPTER = "chapter";
    private static final String MISTAKE_KONGLEDGE = "kongledge";

    private static final String TITLE = "title";
    public static final String WRONGNUM = "wrongNum";
    public static final String SUBJECTID = "subjectId";
    public static final String EDITIONID = "editionId";
    public static final String STAGEID = "stageId";

    private RadioGroup mClassifyView;
    private RadioButton mAllView;
    private RadioButton mKongledgeView;
    private ImageView mBackView;
    private TextView mTitleView;
    private String mTitle;
    private String mSubjectId;
    private int mWrongNum;
    private String mEditionId;
    private String mStageId;
    private FragmentManager mManager = getSupportFragmentManager();

    public static void LaunchActivity(Context context, String title, String subjectId, int wrongNum, String editionId) {
        Intent intent = new Intent(context, MistakeClassifyActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(SUBJECTID, subjectId);
        intent.putExtra(WRONGNUM, wrongNum);
        intent.putExtra(EDITIONID, editionId);
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
        mWrongNum = getIntent().getIntExtra(WRONGNUM,0);
        mEditionId = getIntent().getStringExtra(EDITIONID);
        mStageId = LoginInfo.getStageid();
        initView();
        initFragment();
        listener();
        initData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);

        mClassifyView = (RadioGroup) findViewById(R.id.rg_classify);
        mAllView = (RadioButton) findViewById(R.id.rb_all);
        mKongledgeView= (RadioButton) findViewById(R.id.rb_kongledge);
    }

    private void initFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(STAGEID, mStageId);
        bundle.putString(SUBJECTID, mSubjectId);
        bundle.putString(EDITIONID, mEditionId);
        bundle.putInt(WRONGNUM, mWrongNum);

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
            }else {
                mClassifyView.setVisibility(View.GONE);
            }
        }else {
            mClassifyView.setVisibility(View.GONE);
        }
        transaction.add(R.id.fl_content, completeFragment, MISTAKE_ALL);
        transaction.commit();
        mManager.executePendingTransactions();
    }

    private void listener() {
        mClassifyView.setOnCheckedChangeListener(MistakeClassifyActivity.this);
        mBackView.setOnClickListener(MistakeClassifyActivity.this);
    }

    private void initData() {
        // TODO: 2017/7/20 隐藏章节知识点
        mClassifyView.setVisibility(View.GONE);

        mTitleView.setText(mTitle);
        mBackView.setVisibility(View.VISIBLE);
        mAllView.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction transaction = mManager.beginTransaction();
        Fragment allFragment;
        Fragment chapterFragment;
        Fragment kongledgeFragment;
        switch (checkedId) {
            case R.id.rb_all:
                allFragment = mManager.findFragmentByTag(MISTAKE_ALL);
                if (allFragment != null) {
                    transaction.show(allFragment);
                    chapterFragment=mManager.findFragmentByTag(MISTAKE_CHAPTER);
                    kongledgeFragment=mManager.findFragmentByTag(MISTAKE_KONGLEDGE);
                    if (chapterFragment!=null) {
                        transaction.hide(chapterFragment);
                    }
                    if (kongledgeFragment!=null) {
                        transaction.hide(kongledgeFragment);
                    }
                }
                break;
            case R.id.rb_chapter:
                chapterFragment = mManager.findFragmentByTag(MISTAKE_CHAPTER);
                if (chapterFragment != null) {
                    transaction.show(chapterFragment);
                    allFragment=mManager.findFragmentByTag(MISTAKE_ALL);
                    kongledgeFragment=mManager.findFragmentByTag(MISTAKE_KONGLEDGE);
                    if (allFragment!=null) {
                        transaction.hide(allFragment);
                    }
                    if (kongledgeFragment!=null) {
                        transaction.hide(kongledgeFragment);
                    }
                }
                break;
            case R.id.rb_kongledge:
                kongledgeFragment = mManager.findFragmentByTag(MISTAKE_KONGLEDGE);
                if (kongledgeFragment != null) {
                    transaction.show(kongledgeFragment);
                    chapterFragment=mManager.findFragmentByTag(MISTAKE_CHAPTER);
                    allFragment=mManager.findFragmentByTag(MISTAKE_ALL);
                    if (chapterFragment!=null) {
                        transaction.hide(chapterFragment);
                    }
                    if (allFragment!=null) {
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
