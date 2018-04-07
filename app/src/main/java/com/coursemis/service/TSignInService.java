package com.coursemis.service;

import java.util.ArrayList;

import org.json.JSONObject;

import com.coursemis.util.P;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 教师登录后开启的一个服务
 */
public class TSignInService extends Service {
	

	private final IBinder mBinder = new LocalBinder();
	
	public class LocalBinder extends Binder{
	public 	TSignInService getService()
		{
			return TSignInService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public boolean onUnbind(Intent intent)
	{
		return false;
	}
	

	public void signService()
	{
		
	}

	/**
	 *
	 * @param list
	 * @param arg1
	 */
	public void signInServiceInfo(ArrayList<String> list,JSONObject arg1)
	{
		list.add(0, "学号"+"_"+"姓名"+"_"+" 已到次数"+"_"+"总点到次数");
		for(int i=1;i<=arg1.optJSONArray("result").length();i++){
			JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i-1);
			P.p(object_temp.toString()+2222);
			list.add(i, (object_temp.optInt("SNumber")+"_"+object_temp.optString("SName")+"_"+object_temp.optString("SCPointNum")+"_"+object_temp.optString("ScPointTotalNum")));
			}
	}
	
	
}
