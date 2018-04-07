package com.coursemis.util;


public class FormFile
{

	 /* 上传文件的数据 */   
	private byte[] data;   

    /**
	 * @return the data
	 */
	public byte[] getData()
	{
		return data;
	}


	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data)
	{
		this.data = data;
	}

	/* 文件名称 */  
    private String filname;  
    /* 表单字段名称*/  
    private String formname;  
    /* 内容类型 */  
    private String contentType = "application/octet-stream"; //需要查阅相关的资料  
      
    public FormFile(String filname, byte[] data,String formname, String contentType) {  
    	 this.data = data;     
        this.filname = filname;  
        this.formname = formname;  
        if(contentType!=null) this.contentType = contentType;  
    }

	
	/**
	 * @return the filname
	 */
	public String getFilname()
	{
		return filname;
	}

	/**
	 * @param filname the filname to set
	 */
	public void setFilname(String filname)
	{
		this.filname = filname;
	}

	/**
	 * @return the formname
	 */
	public String getFormname()
	{
		return formname;
	}

	/**
	 * @param formname the formname to set
	 */
	public void setFormname(String formname)
	{
		this.formname = formname;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType()
	{
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}  

}
