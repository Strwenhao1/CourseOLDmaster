package com.coursemis.util;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.coursemis.manage.WebService;

public class TextUploadUtil {
	static String result = "";
	static String path = "";
	private Handler uihandler = null;
	static String TAG = "TestUploadUtil";

	public TextUploadUtil(Handler handler) {
		this.uihandler = handler;
	}

	public String uploadFile(File file, String URL) {
		int responseCode = 0;
		
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);// 设置通信协议版本
		
		HttpPost httppost = new HttpPost(URL);
		httppost.addHeader("auth", "0000;121a10c29912a912b12e61207122f1082e;A");	
		
		
		MultipartEntity mpEntity = new MultipartEntity(); // 文件传输	
	
		mpEntity.addPart("userfile", new FileBody(file,file.getAbsolutePath(), "image/jpeg", "UTF-8")); 		
		httppost.setEntity(mpEntity);
		
		Log.i("textUpliadUtil line38", "executing request " + httppost.getRequestLine());
		try {
			final HttpResponse response = httpclient.execute(httppost);
			final HttpEntity resEntity = response.getEntity();	
			
			responseCode = response.getStatusLine().getStatusCode();
			Log.i(TAG, "responseCode is :" + responseCode);
			
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity, "utf-8");
				
				JSONObject p = null;
				try {
					p = new JSONObject(result);//把结果变成json对象
					path = (String) p.get("path");
				} catch (Exception e) {
					e.printStackTrace();
				}
			
				
				
			}
			if (resEntity != null) {
				resEntity.consumeContent();// 销毁内容
			}
			
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Log.e(TAG, "IoException:");
			e1.printStackTrace();
			
		}

		httpclient.getConnectionManager().shutdown();
		TellConState("text Uploading!");
		return result;
	}
	private void TellConState(String str) {
		Message mesg = this.uihandler.obtainMessage();
		mesg.what = WebService.CON_STATE;
		mesg.obj = str;

		this.uihandler.sendMessage(mesg);

	}
}
