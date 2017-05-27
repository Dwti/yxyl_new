package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PickerView;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.user.response.CityBean;
import com.yanxiu.gphone.student.user.response.AreaBean;
import com.yanxiu.gphone.student.user.response.ProvinceBean;
import com.yanxiu.gphone.student.util.XmlParserHandler;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import de.greenrobot.event.EventBus;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/19 11:11.
 * Function :
 */
public class ChooseLocationActivity extends YanxiuBaseActivity implements PickerView.onSelectListener, View.OnClickListener {

    public static final String KEY="location";
    private static final int DEFAULT_SELECT=0;

    private Context mContext;
    private PickerView mChooseProvinceView;
    private PickerView mChooseCityView;
    private PickerView mChooseAreaView;
    private TextView mTitleRightView;
    private ImageView mTitleLeftView;
    private View mTopView;

    private List<ProvinceBean> provinceList;
    private List<CityBean> cityList;
    private List<AreaBean> areaList;

    private int mSelectProvince=0;
    private int mSelectCity=0;
    private int mSelectArea=0;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,ChooseLocationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=ChooseLocationActivity.this;
        PublicLoadLayout rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_chooselocation);
//        rootView.finish();
        setContentView(rootView);
        EventBus.getDefault().register(mContext);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    private void initView() {
        mTopView=findViewById(R.id.include_top);
        mTitleLeftView= (ImageView) findViewById(R.id.iv_left);
        mTitleRightView= (TextView) findViewById(R.id.tv_right);
        mChooseProvinceView= (PickerView) findViewById(R.id.pv_province);
        mChooseCityView= (PickerView) findViewById(R.id.pv_city);
        mChooseAreaView= (PickerView) findViewById(R.id.pv_area);
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
        mTitleLeftView.setOnClickListener(ChooseLocationActivity.this);
        mTitleRightView.setOnClickListener(ChooseLocationActivity.this);
        mChooseProvinceView.setOnSelectListener(ChooseLocationActivity.this);
        mChooseCityView.setOnSelectListener(ChooseLocationActivity.this);
        mChooseAreaView.setOnSelectListener(ChooseLocationActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mTitleLeftView.setVisibility(View.VISIBLE);
        mTitleRightView.setVisibility(View.VISIBLE);
        mTitleRightView.setText(getText(R.string.ok));
        mTitleRightView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.selector_choose_ensure_bg));

        mChooseProvinceView.setTextLocation(PickerView.DEFAULT_LEFT);
        mChooseCityView.setTextLocation(PickerView.DEFAULT_CENTER);
        mChooseAreaView.setTextLocation(PickerView.DEFAULT_RIGHT);

        setProvinceDatas(DEFAULT_SELECT);
        setCityDatas(DEFAULT_SELECT);
        setAreaDatas(DEFAULT_SELECT);
    }

    private List<String> getProvinceNamesByDatas(List<ProvinceBean> datas){
        List<String> list=new ArrayList<>();
        for (ProvinceBean bean:datas){
            list.add(bean.getName());
        }
        return list;
    }

    private List<String> getCityNamesByDatas(List<CityBean> datas){
        List<String> list=new ArrayList<>();
        for (CityBean bean:datas){
            list.add(bean.getName());
        }
        return list;
    }

    private List<String> getAreaNamesByDatas(List<AreaBean> datas){
        List<String> list=new ArrayList<>();
        for (AreaBean bean:datas){
            list.add(bean.getName());
        }
        return list;
    }

    private void setProvinceDatas(int selectId){
        this.mSelectProvince=selectId;

        provinceList=getProvinceDatas();
        mChooseProvinceView.setData(getProvinceNamesByDatas(provinceList));
        mChooseProvinceView.setSelected(mSelectProvince);
    }

    private void setCityDatas(int selectId){
        this.mSelectCity=selectId;

        cityList=provinceList.get(mSelectProvince).getCityList();
        mChooseCityView.setData(getCityNamesByDatas(cityList));
        mChooseCityView.setSelected(mSelectCity);
    }

    private void setAreaDatas(int selectId){
        this.mSelectArea=selectId;

        areaList=cityList.get(mSelectCity).getAreaList();
        mChooseAreaView.setData(getAreaNamesByDatas(areaList));
        mChooseAreaView.setSelected(mSelectArea);
    }

    @Override
    public void onSelect(View view, String text,int selectId) {
        switch (view.getId()){
            case R.id.pv_province:
                this.mSelectProvince=selectId;

                setCityDatas(DEFAULT_SELECT);
                setAreaDatas(DEFAULT_SELECT);
                break;
            case R.id.pv_city:
                this.mSelectCity=selectId;

                setAreaDatas(DEFAULT_SELECT);
                break;
            case R.id.pv_area:
                this.mSelectArea=selectId;
                break;
        }
    }

    private List<ProvinceBean> getProvinceDatas() {
        List<ProvinceBean> provinceList = new ArrayList<ProvinceBean>();
        AssetManager asset = this.getAssets();
        InputStream input=null;
        try {
            input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            // 获取解析出来的数据
            provinceList.addAll(handler.getDataList());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e){
            e.printStackTrace();
        } catch (ParserConfigurationException e){
            e.printStackTrace();
        }finally {
            try {
                if (input!=null) {
                    input.close();
                    input=null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return provinceList;
    }

    public void onEventMainThread(CompleteInfoActivity.SchoolMessage message) {
        ChooseLocationActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right:
                CompleteInfoActivity.SchoolMessage message=new CompleteInfoActivity.SchoolMessage();
                message.provinceId=provinceList.get(mSelectProvince).getId();
                message.provinceName=provinceList.get(mSelectProvince).getName();
                message.cityId=cityList.get(mSelectCity).getId();
                message.cityName=cityList.get(mSelectCity).getName();
                message.areaId=areaList.get(mSelectArea).getZipcode();
                message.areaName=areaList.get(mSelectArea).getName();
                ChooseSchoolActivity.LuanchActivity(mContext,message);
                break;
            case R.id.iv_left:
                ChooseLocationActivity.this.finish();
                break;
        }
    }
}