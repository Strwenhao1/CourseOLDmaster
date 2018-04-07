package com.coursemis.model;

public class Student implements java.io.Serializable {
	private Integer SId;
	private String SNum;
	private String SName;
	private String SSex;
	private String SDepartment;
	private String SClass;
	private String STel;
	private String SEmail;
	private String SPassword;
	public Integer getSId() {
		return SId;
	}
	public void setSId(Integer sId) {
		SId = sId;
	}
	public String getSNum() {
		return SNum;
	}
	public void setSNum(String sNum) {
		SNum = sNum;
	}
	public String getSName() {
		return SName;
	}
	public void setSName(String sName) {
		SName = sName;
	}
	public String getSSex() {
		return SSex;
	}
	public void setSSex(String sSex) {
		SSex = sSex;
	}
	public String getSDepartment() {
		return SDepartment;
	}
	public void setSDepartment(String sDepartment) {
		SDepartment = sDepartment;
	}
	public String getSClass() {
		return SClass;
	}
	public void setSClass(String sClass) {
		SClass = sClass;
	}
	public String getSTel() {
		return STel;
	}
	public void setSTel(String sTel) {
		STel = sTel;
	}
	public String getSEmail() {
		return SEmail;
	}
	public void setSEmail(String sEmail) {
		SEmail = sEmail;
	}
	public String getSPassword() {
		return SPassword;
	}
	public void setSPassword(String sPassword) {
		SPassword = sPassword;
	}
	
	
}
