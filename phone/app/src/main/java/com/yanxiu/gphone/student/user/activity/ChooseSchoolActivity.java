package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.util.EditTextManger;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/22 17:54.
 * Function :
 */
public class ChooseSchoolActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private Context mContext;
    private EditText mSchoolNameView;
    private ImageView mSearchView;
    private TextView mProvinceView;
    private TextView mCityView;
    private TextView mAreaView;
    private RecyclerView mSchoolListView;

    public static void LuanchActivity(Context context){
        Intent intent=new Intent(context,ChooseSchoolActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseschool);
        mContext=ChooseSchoolActivity.this;
        initView();
        listener();
        initData();
    }

    private void initView() {
        mSchoolNameView= (EditText) findViewById(R.id.et_school_name);
        mSearchView= (ImageView) findViewById(R.id.iv_search);
        mProvinceView= (TextView) findViewById(R.id.tv_province);
        mCityView= (TextView) findViewById(R.id.tv_city);
        mAreaView= (TextView) findViewById(R.id.tv_area);
        mSchoolListView= (RecyclerView) findViewById(R.id.recy_school_list);
    }

    private void listener() {
        mSearchView.setOnClickListener(ChooseSchoolActivity.this);
        EditTextManger.getManager(mSchoolNameView).setTextChangedListener(ChooseSchoolActivity.this);
    }

    private void initData() {
        mSearchView.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_search:
                String schoolName=mSchoolNameView.getText().toString().trim();
                searchSchool(schoolName);
                break;
        }
    }

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        if (isEmpty){
            mSearchView.setEnabled(false);
        } else {
            mSearchView.setEnabled(true);
        }
    }

    private void searchSchool(String schoolName){

    }
}
