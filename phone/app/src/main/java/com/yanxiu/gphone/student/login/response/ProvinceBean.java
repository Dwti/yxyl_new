package com.yanxiu.gphone.student.login.response;


import java.util.ArrayList;

public class ProvinceBean {
	private String name;
	private String id;
	private ArrayList<CityBean> cityList;
	
	public ProvinceBean() {
		super();
	}

	public ProvinceBean(String name, ArrayList<CityBean> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CityBean> getCityList() {
		return cityList;
	}

	public void setCityList(ArrayList<CityBean> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceBean [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
