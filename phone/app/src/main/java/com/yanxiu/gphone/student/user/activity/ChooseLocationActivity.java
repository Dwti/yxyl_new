package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.user.PickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/19 11:11.
 * Function :
 */
public class ChooseLocationActivity extends YanxiuBaseActivity {

    private Context mContext;

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
        PickerView mProvinceView= (PickerView) findViewById(R.id.province);
        mProvinceView.setTextLocation(PickerView.DEFAULT_LEFT);
        PickerView mCityView= (PickerView) findViewById(R.id.city);
        mCityView.setTextLocation(PickerView.DEFAULT_CENTER);
        PickerView mAreaView= (PickerView) findViewById(R.id.area);
        mAreaView.setTextLocation(PickerView.DEFAULT_RIGHT);
        mAreaView.setData(getList());
        mCityView.setData(getList());
        mProvinceView.setData(getList());
    }

    private List<String> getList(){
        List<String> list=new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(i+"alfakuuagujgeugiwheifwhjpiwhigfhywugfuwoygfo8uwyg8uf");
        }
        return list;
    }

    private void listener() {

    }

    private void initData() {

    }
}
