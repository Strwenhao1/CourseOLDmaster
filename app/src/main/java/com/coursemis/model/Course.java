package com.coursemis.model;

public class Course implements java.io.Serializable {
	private Integer CId;
	private Teacher teacher;
	private String CNum;
	private String CName;
	private Boolean CFlag;
	private Integer CPointTotalNum;
	public Integer getCId() {
		return CId;
	}
	public void setCId(Integer cId) {
		CId = cId;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public String getCNum() {
		return CNum;
	}
	public void setCNum(String cNum) {
		CNum = cNum;
	}
	public String getCName() {
		return CName;
	}
	public void setCName(String cName) {
		CName = cName;
	}
	public Boolean getCFlag() {
		return CFlag;
	}
	public void setCFlag(Boolean cFlag) {
		CFlag = cFlag;
	}
	public Integer getCPointTotalNum() {
		return CPointTotalNum;
	}
	public void setCPointTotalNum(Integer cPointTotalNum) {
		CPointTotalNum = cPointTotalNum;
	}
}
