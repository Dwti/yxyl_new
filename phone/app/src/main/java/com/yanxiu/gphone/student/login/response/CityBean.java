package com.yanxiu.gphone.student.login.response;

import java.util.ArrayList;

public class CityBean {
	private String name;
	private String id;
	private String provinceName;
	private String provinceId;
	private ArrayList<AreaBean> areaList;

	public CityBean() {
		super();
	}

	public CityBean(String name, ArrayList<AreaBean> areaList) {
		super();
		this.name = name;
		this.areaList = areaList;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<AreaBean> getAreaList() {
		return areaList;
	}

	public void setAreaList(ArrayList<AreaBean> districtList) {
		this.areaList = districtList;
	}

	@Override
	public String toString() {
		return "CityBean [name=" + name + ", districtList=" + areaList
				+ "]";
	}
	
}
