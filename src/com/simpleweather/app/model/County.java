package com.simpleweather.app.model;

public class County {

	private String provinceName;
	private String cityName;
	private String countyName;
	private String id;
	private String ename;
	
	public County(String s1,String s2,String s3 ,String s4 ,String s5)
	{
		this.provinceName=s5;
		this.cityName=s4;
		this.countyName=s3;
		this.id=s1;
		this.ename=s2;
	}
	
	public County()
	{
		this.provinceName=null;
		this.cityName=null;
		this.countyName=null;
		this.id=null;
		this.ename=null;
	}
	
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	
}
