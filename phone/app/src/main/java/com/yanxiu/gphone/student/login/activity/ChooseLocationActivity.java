package com.yanxiu.gphone.student.login.activity;

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
import com.yanxiu.gphone.student.login.response.CityBean;
import com.yanxiu.gphone.student.login.response.AreaBean;
import com.yanxiu.gphone.student.login.response.ProvinceBean;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.XmlParserHandler;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

    public static final String KEY = "location";
    private static final int DEFAULT_SELECT = 0;
    private static final String REQUESTCODE = "requestCode";

    private Context mContext;
    private PickerView mChooseProvinceView;
    private PickerView mChooseCityView;
    private PickerView mChooseAreaView;
    private TextView mConfirmView;
    private ImageView mBackView;
    private View mTopView;

    private List<ProvinceBean> provinceList;
    private List<CityBean> cityList;
    private List<AreaBean> areaList;

    private int mSelectProvince = 0;
    private int mSelectCity = 0;
    private int mSelectArea = 0;
    private TextView mTitleView;
    private int mRequestCode = -1;

    public static void LaunchActivity(Context context, int requestCode) {
        Intent intent = new Intent(context, ChooseLocationActivity.class);
        intent.putExtra(REQUESTCODE, requestCode);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ChooseLocationActivity.this;
        mRequestCode = getIntent().getIntExtra(REQUESTCODE, -1);
        PublicLoadLayout rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_chooselocation);
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
        mTopView = findViewById(R.id.include_top);
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mConfirmView = (TextView) findViewById(R.id.tv_right);
        mChooseProvinceView = (PickerView) findViewById(R.id.pv_province);
        mChooseCityView = (PickerView) findViewById(R.id.pv_city);
        mChooseAreaView = (PickerView) findViewById(R.id.pv_area);
    }

    private void listener() {
        mBackView.setOnClickListener(ChooseLocationActivity.this);
        mConfirmView.setOnClickListener(ChooseLocationActivity.this);
        mChooseProvinceView.setOnSelectListener(ChooseLocationActivity.this);
        mChooseCityView.setOnSelectListener(ChooseLocationActivity.this);
        mChooseAreaView.setOnSelectListener(ChooseLocationActivity.this);
    }

    private void initData() {
        mTitleView.setText(getText(R.string.chooselocation));
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mConfirmView.setVisibility(View.VISIBLE);
        mConfirmView.setText(getText(R.string.ok));
        mConfirmView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector_choose_ensure_bg));

        mChooseProvinceView.setTextLocation(PickerView.DEFAULT_LEFT);
        mChooseCityView.setTextLocation(PickerView.DEFAULT_CENTER);
        mChooseAreaView.setTextLocation(PickerView.DEFAULT_RIGHT);

        setProvinceDatas(DEFAULT_SELECT);
        setCityDatas(DEFAULT_SELECT);
        setAreaDatas(DEFAULT_SELECT);

        mBackView.setBackgroundResource(R.drawable.selector_back);

        if (LoginInfo.isLogIn()) {
            String provinceName = LoginInfo.getProvinceName();
            mChooseProvinceView.setSelected(provinceName);
            String cityName = LoginInfo.getCityName();
            mChooseCityView.setSelected(cityName);
            String areaName = LoginInfo.getAreaName();
            mChooseAreaView.setSelected(areaName);
        }
    }

    private List<String> getProvinceNamesByDatas(List<ProvinceBean> datas) {
        List<String> list = new ArrayList<>();
        for (ProvinceBean bean : datas) {
            list.add(bean.getName());
        }
        return list;
    }

    private List<String> getCityNamesByDatas(List<CityBean> datas) {
        List<String> list = new ArrayList<>();
        for (CityBean bean : datas) {
            list.add(bean.getName());
        }
        return list;
    }

    private List<String> getAreaNamesByDatas(List<AreaBean> datas) {
        List<String> list = new ArrayList<>();
        for (AreaBean bean : datas) {
            list.add(bean.getName());
        }
        return list;
    }

    private void setProvinceDatas(int selectId) {
        this.mSelectProvince = selectId;

        provinceList = getProvinceDatas();
        mChooseProvinceView.setData(getProvinceNamesByDatas(provinceList));
        mChooseProvinceView.setSelected(mSelectProvince);
    }

    private void setCityDatas(int selectId) {
        this.mSelectCity = selectId;

        cityList = provinceList.get(mSelectProvince).getCityList();
        mChooseCityView.setData(getCityNamesByDatas(cityList));
        mChooseCityView.setSelected(mSelectCity);
    }

    private void setAreaDatas(int selectId) {
        this.mSelectArea = selectId;

        areaList = cityList.get(mSelectCity).getAreaList();
        mChooseAreaView.setData(getAreaNamesByDatas(areaList));
        mChooseAreaView.setSelected(mSelectArea);
    }

    @Override
    public void onSelect(View view, String text, int selectId) {
        switch (view.getId()) {
            case R.id.pv_province:
                this.mSelectProvince = selectId;

                setCityDatas(DEFAULT_SELECT);
                setAreaDatas(DEFAULT_SELECT);
                break;
            case R.id.pv_city:
                this.mSelectCity = selectId;

                setAreaDatas(DEFAULT_SELECT);
                break;
            case R.id.pv_area:
                this.mSelectArea = selectId;
                break;
        }
    }

    private List<ProvinceBean> getProvinceDatas() {
        List<ProvinceBean> provinceList = new ArrayList<ProvinceBean>();
        AssetManager asset = this.getAssets();
        InputStream input = null;
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
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                    input = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return provinceList;
    }

    public void onEventMainThread(SchoolMessage message) {
        ChooseLocationActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                SchoolMessage message = new SchoolMessage();
                message.requestCode = mRequestCode;
                message.provinceId = provinceList.get(mSelectProvince).getId();
                message.provinceName = provinceList.get(mSelectProvince).getName();
                message.cityId = cityList.get(mSelectCity).getId();
                message.cityName = cityList.get(mSelectCity).getName();
                message.areaId = areaList.get(mSelectArea).getZipcode();
                message.areaName = areaList.get(mSelectArea).getName();
                ChooseSchoolActivity.LuanchActivity(mContext, message);
                break;
            case R.id.iv_left:
                ChooseLocationActivity.this.finish();
                EditTextManger.getManager(mTitleView).hideSoftInput();
                break;
        }
    }

    public static class SchoolMessage implements Serializable {
        public int requestCode;

        public String provinceId;
        public String provinceName;

        public String cityId;
        public String cityName;

        public String areaId;
        public String areaName;

        public String schoolId;
        public String schoolName;
    }

}
