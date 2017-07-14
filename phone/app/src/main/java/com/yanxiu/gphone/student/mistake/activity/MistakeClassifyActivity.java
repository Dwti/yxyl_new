package com.yanxiu.gphone.student.mistake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.mistake.fragment.MistakeChapterFragment;
import com.yanxiu.gphone.student.mistake.fragment.MistakeCompleteFragment;
import com.yanxiu.gphone.student.mistake.fragment.MistakeKongledgeFragment;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 16:04.
 * Function :
 */
public class MistakeClassifyActivity extends YanxiuBaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private static final String MISTAKE_COMPLETE="complete";
    private static final String MISTAKE_CHAPTER="chapter";
    private static final String MISTAKE_KONGLEDGE="kongledge";

    private RadioGroup mClassifyView;
    private RadioButton mCompleteView;
    private ImageView mBackView;
    private TextView mTitleView;
    private FragmentManager mManager = getSupportFragmentManager();

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, MistakeClassifyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PublicLoadLayout rootView = new PublicLoadLayout(this);
        rootView.setContentView(R.layout.activity_mistakeclassify);
        setContentView(rootView);
        initView();
        initFragment();
        listener();
        initData();
    }

    private void initView() {
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);

        mClassifyView = (RadioGroup) findViewById(R.id.rg_classify);
        mCompleteView= (RadioButton) findViewById(R.id.rb_complete);
    }

    private void initFragment() {
        FragmentTransaction transaction=mManager.beginTransaction();
        transaction.add(R.id.fl_content,new MistakeChapterFragment(),MISTAKE_CHAPTER);
        transaction.add(R.id.fl_content,new MistakeKongledgeFragment(),MISTAKE_KONGLEDGE);
        transaction.add(R.id.fl_content,new MistakeCompleteFragment(),MISTAKE_COMPLETE);
        transaction.commit();
        mManager.executePendingTransactions();
    }

    private void listener() {
        mClassifyView.setOnCheckedChangeListener(MistakeClassifyActivity.this);
        mBackView.setOnClickListener(MistakeClassifyActivity.this);
    }

    private void initData() {
        mBackView.setVisibility(View.VISIBLE);
        mCompleteView.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction transaction=mManager.beginTransaction();
        switch (checkedId) {
            case R.id.rb_complete:
                transaction.show(mManager.findFragmentByTag(MISTAKE_COMPLETE));
                transaction.hide(mManager.findFragmentByTag(MISTAKE_CHAPTER));
                transaction.hide(mManager.findFragmentByTag(MISTAKE_KONGLEDGE));
                break;
            case R.id.rb_chapter:
                transaction.show(mManager.findFragmentByTag(MISTAKE_CHAPTER));
                transaction.hide(mManager.findFragmentByTag(MISTAKE_COMPLETE));
                transaction.hide(mManager.findFragmentByTag(MISTAKE_KONGLEDGE));
                break;
            case R.id.rb_kongledge:
                transaction.show(mManager.findFragmentByTag(MISTAKE_KONGLEDGE));
                transaction.hide(mManager.findFragmentByTag(MISTAKE_CHAPTER));
                transaction.hide(mManager.findFragmentByTag(MISTAKE_COMPLETE));
                break;
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                this.finish();
                break;
        }
    }
}
