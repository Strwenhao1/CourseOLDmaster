
package com.coursemis.model;

import java.io.Serializable;


/**
* @author nebo
* @E-mail:nebofeng@gmail.com
* @version creatTime：2017年5月20日 下午9:17:51
* 类说明:方便传递
*/
public class AskStudent  implements Serializable{
	private Integer scId;
	
	private String cName;
	private Integer sNumber;
	private String sName;
	public AskStudent(Integer scId, String cName, Integer sNumber, String sName) {
		super();
		this.scId = scId;
		this.cName = cName;
		this.sNumber = sNumber;
		this.sName = sName;
	}
	
	public Integer getScId() {
		return scId;
	}
	public String getcName() {
		return cName;
	}
	public Integer getsNumber() {
		return sNumber;
	}
	public String getsName() {
		return sName;
	}
	public void setScId(Integer scId) {
		this.scId = scId;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public void setsNumber(Integer sNumber) {
		this.sNumber = sNumber;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	
	
	
	
	
}
