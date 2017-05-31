package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.ExerciseBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.user.adapter.ChooseSchoolAdapter;
import com.yanxiu.gphone.student.user.request.ChooseSchoolRequest;
import com.yanxiu.gphone.student.user.response.ChooseSchoolResponse;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.ToastManager;

import de.greenrobot.event.EventBus;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/22 17:54.
 * Function :
 */
public class ChooseSchoolActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener, ChooseSchoolAdapter.OnItemClickListener {

    private Context mContext;
    private EditText mSchoolNameView;
    private ImageView mSearchView;
    private TextView mProvinceView;
    private TextView mCityView;
    private TextView mAreaView;
    private RecyclerView mSchoolListView;
    private ImageView mBackView;
    private View mTopView;

    private CompleteInfoActivity.SchoolMessage message;
    private PublicLoadLayout rootView;
    private ChooseSchoolRequest mChooseSchoolRequest;
    private ChooseSchoolAdapter adapter;
    private TextView mTitleView;

    public static void LuanchActivity(Context context, CompleteInfoActivity.SchoolMessage message){
        Intent intent=new Intent(context,ChooseSchoolActivity.class);
        intent.putExtra(ChooseLocationActivity.KEY,message);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=ChooseSchoolActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_chooseschool);
        setContentView(rootView);
        message= (CompleteInfoActivity.SchoolMessage) getIntent().getSerializableExtra(ChooseLocationActivity.KEY);
        initView();
        listener();
        initData();
        searchSchool("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelRequest();
    }

    private void initView() {
        mTopView=findViewById(R.id.include_top);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mSchoolNameView= (EditText) findViewById(R.id.et_school_name);
        mSearchView= (ImageView) findViewById(R.id.iv_search);
        mProvinceView= (TextView) findViewById(R.id.tv_province);
        mCityView= (TextView) findViewById(R.id.tv_city);
        mAreaView= (TextView) findViewById(R.id.tv_area);
        mSchoolListView= (RecyclerView) findViewById(R.id.recy_school_list);
        mSchoolListView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter=new ChooseSchoolAdapter(mContext);
    }

    private void listener() {
        mBackView.setOnClickListener(ChooseSchoolActivity.this);
        mSearchView.setOnClickListener(ChooseSchoolActivity.this);
        adapter.setOnItemClickListener(ChooseSchoolActivity.this);
        EditTextManger.getManager(mSchoolNameView).setTextChangedListener(ChooseSchoolActivity.this);
    }

    private void initData() {
        mTitleView.setText(getText(R.string.chooseschool));
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mSearchView.setEnabled(false);
        mProvinceView.setText(message.provinceName);
        mCityView.setText(message.cityName);
        mAreaView.setText(message.areaName);
        mSchoolListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_search:
                String schoolName=mSchoolNameView.getText().toString().trim();
                searchSchool(schoolName);
                break;
            case R.id.iv_left:
                ChooseSchoolActivity.this.finish();
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
        searchSchool(value);
    }

    private void cancelRequest(){
        if (mChooseSchoolRequest!=null){
            mChooseSchoolRequest.cancelRequest();
            mChooseSchoolRequest=null;
        }
    }

    private void searchSchool(String schoolName){
        cancelRequest();
        rootView.showLoadingView();
        mChooseSchoolRequest=new ChooseSchoolRequest();
        mChooseSchoolRequest.school=schoolName;
        mChooseSchoolRequest.regionId=message.areaId;
        mChooseSchoolRequest.startRequest(ChooseSchoolResponse.class, new ExerciseBaseCallback<ChooseSchoolResponse>() {

            @Override
            protected void onResponse(RequestBase request, ChooseSchoolResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0&&response.data!=null){
                    adapter.setDatas(response.data);
                }else {
                    adapter.setDatas(null);
                    ToastManager.showMsg(getText(R.string.search_school_no));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(View view, ChooseSchoolResponse.School school, int position) {
        message.schoolId=school.id;
        message.schoolName=school.name;
        EventBus.getDefault().post(message);
        ChooseSchoolActivity.this.finish();
    }
}
