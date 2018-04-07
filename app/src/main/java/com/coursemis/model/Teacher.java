package com.coursemis.model;

public class Teacher implements java.io.Serializable {
	private Integer TId;
	private String TNum;
	private String TName;
	private String TDepartment;
	private String TTel;
	private String TEmail;
	private String TPassword;
	
	public Integer getTId() {
		return TId;
	}
	public void setTId(Integer tId) {
		TId = tId;
	}
	public String getTNum() {
		return TNum;
	}
	public void setTNum(String tNum) {
		TNum = tNum;
	}
	public String getTName() {
		return TName;
	}
	public void setTName(String tName) {
		TName = tName;
	}
	public String getTDepartment() {
		return TDepartment;
	}
	public void setTDepartment(String tDepartment) {
		TDepartment = tDepartment;
	}
	public String getTTel() {
		return TTel;
	}
	public void setTTel(String tTel) {
		TTel = tTel;
	}
	public String getTEmail() {
		return TEmail;
	}
	public void setTEmail(String tEmail) {
		TEmail = tEmail;
	}
	public String getTPassword() {
		return TPassword;
	}
	public void setTPassword(String tPassword) {
		TPassword = tPassword;
	}
}
