package com.coursemis.model;

public class Coursetime {
	private Integer ctId;
	private Course course;
	private Integer ctWeekNum;
	private Integer ctWeekChoose;
	private Integer ctWeekDay;
	private Integer ctStartClass;
	private Integer ctEndClass;
	private String ctAddress;
	
	public Integer getCtId() {
		return ctId;
	}
	public void setCtId(Integer ctId) {
		this.ctId = ctId;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Integer getCtWeekNum() {
		return ctWeekNum;
	}
	public void setCtWeekNum(Integer ctWeekNum) {
		this.ctWeekNum = ctWeekNum;
	}
	public Integer getCtWeekChoose() {
		return ctWeekChoose;
	}
	public void setCtWeekChoose(Integer ctWeekChoose) {
		this.ctWeekChoose = ctWeekChoose;
	}
	public Integer getCtWeekDay() {
		return ctWeekDay;
	}
	public void setCtWeekDay(Integer ctWeekDay) {
		this.ctWeekDay = ctWeekDay;
	}
	public Integer getCtStartClass() {
		return ctStartClass;
	}
	public void setCtStartClass(Integer ctStartClass) {
		this.ctStartClass = ctStartClass;
	}
	public Integer getCtEndClass() {
		return ctEndClass;
	}
	public void setCtEndClass(Integer ctEndClass) {
		this.ctEndClass = ctEndClass;
	}
	public String getCtAddress() {
		return ctAddress;
	}
	public void setCtAddress(String ctAddress) {
		this.ctAddress = ctAddress;
	}
}
