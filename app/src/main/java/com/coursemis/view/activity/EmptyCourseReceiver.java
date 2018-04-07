package com.coursemis.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class EmptyCourseReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		/*Toast.makeText(arg0 , "接收到的Intent的Action为："
				+ arg1.getAction() 
				+ "\n消息内容是：" + arg1.getStringExtra("msg")
				, Toast.LENGTH_LONG).show();*/
	}

}
