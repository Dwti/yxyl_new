package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/19 11:11.
 * Function :
 */
public class ChooseLocationActivity extends YanxiuBaseActivity implements PickerView.onSelectListener {

    private Context mContext;
    private PickerView mProvinceView;
    private PickerView mCityView;
    private PickerView mAreaView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,ChooseLocationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooselocation);
        mContext=ChooseLocationActivity.this;
        initView();
        listener();
        initData();
    }

    private void initView() {
        mProvinceView= (PickerView) findViewById(R.id.pv_province);
        mCityView= (PickerView) findViewById(R.id.pv_city);
        mAreaView= (PickerView) findViewById(R.id.pv_area);
    }

    private List<String> getList(){
        List<String> list=new ArrayList<>();
        for (int i=0;i<10;i++){
            if (i==1||i==3||i==5) {
                list.add(i + "alfakuuagujgeugiwheifwhjpiwhigfhywugfuwoygfo8uwyg8uf");
            }else {
                list.add(""+i);
            }
        }
        return list;
    }

    private void listener() {
        mProvinceView.setOnSelectListener(ChooseLocationActivity.this);
        mCityView.setOnSelectListener(ChooseLocationActivity.this);
        mAreaView.setOnSelectListener(ChooseLocationActivity.this);
    }

    private void initData() {
        mProvinceView.setTextLocation(PickerView.DEFAULT_LEFT);
        mCityView.setTextLocation(PickerView.DEFAULT_CENTER);
        mAreaView.setTextLocation(PickerView.DEFAULT_RIGHT);
        mAreaView.setData(getList());
        mCityView.setData(getList());
        mProvinceView.setData(getList());
    }

    @Override
    public void onSelect(View view, String text) {
        switch (view.getId()){
            case R.id.pv_province:
                break;
            case R.id.pv_city:
                break;
            case R.id.pv_area:
                break;
        }
    }
}
