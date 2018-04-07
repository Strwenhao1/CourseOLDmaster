package com.coursemis.thread;

import java.util.Map;


public class UploadThread implements Runnable{

	
	private String requestUrl;
	private Map<String, String> params;
	private FormFile formfile;
	public UploadThread(String requestUrl ,Map<String, String> params,FormFile formfile)
	{
		this.requestUrl=requestUrl;
		this.params=params;
		this.formfile=formfile;
		
	}
	public void run() {
		// TODO Auto-generated method stub
		try {
			FileUtil.post(requestUrl, params, formfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	

	
}


