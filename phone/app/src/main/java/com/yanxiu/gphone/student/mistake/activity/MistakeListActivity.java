package com.yanxiu.gphone.student.mistake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.mistake.adapter.MistakeAdapter;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/12 11:23.
 * Function :
 */
public class MistakeListActivity extends YanxiuBaseActivity {

    private Context mContext;

    public static void LuanchActivity(Context context){
        Intent intent=new Intent(context,MistakeListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=MistakeListActivity.this;
        PublicLoadLayout rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_mistakelist);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {
        RecyclerView recy= (RecyclerView) findViewById(R.id.recy);
        recy.setLayoutManager(new LinearLayoutManager(this));
        MistakeAdapter adapter=new MistakeAdapter(this);
        recy.setAdapter(adapter);
        adapter.setData();
    }

    private void listener() {

    }

    private void initData() {

    }
}
