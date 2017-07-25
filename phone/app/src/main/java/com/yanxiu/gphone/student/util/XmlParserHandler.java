package com.yanxiu.gphone.student.util;

import com.yanxiu.gphone.student.login.response.CityBean;
import com.yanxiu.gphone.student.login.response.AreaBean;
import com.yanxiu.gphone.student.login.response.ProvinceBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XmlParserHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private ArrayList<ProvinceBean> provinceList = new ArrayList<>();

	public XmlParserHandler() {

	}

	public ArrayList<ProvinceBean> getDataList() {
		return provinceList;
	}

	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	private ProvinceBean province;
	private CityBean city;
	private AreaBean area;

	@Override
	public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("province")) {
			province = new ProvinceBean();
			province.setName(attributes.getValue(0));
			province.setId(attributes.getValue(1));
			province.setCityList(new ArrayList<CityBean>());
		} else if (qName.equals("city")) {
			city = new CityBean();
			city.setProvinceName(province.getName());
			city.setProvinceId(province.getId());
			city.setName(attributes.getValue(0));
			city.setId(attributes.getValue(1));
			city.setAreaList(new ArrayList<AreaBean>());
		} else if (qName.equals("area")) {
			area = new AreaBean();
			area.setCityName(city.getName());
			area.setProvinceName(city.getProvinceName());
			area.setProvinceId(city.getProvinceId());
			area.setCityId(city.getId());
			area.setName(attributes.getValue(0));
			area.setZipcode(attributes.getValue(1));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("area")) {
			city.getAreaList().add(area);
		} else if (qName.equals("city")) {
			province.getCityList().add(city);
		} else if (qName.equals("province")) {
			provinceList.add(province);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}
