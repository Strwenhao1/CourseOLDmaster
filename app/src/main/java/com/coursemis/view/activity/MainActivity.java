package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;

import com.coursemis.R;


public class MainActivity extends Activity {
	private static final int num = 123;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//获取登录文本组件，并注册监听      
        TextView logintext=(TextView) this.findViewById(R.id.logintext);
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				 Intent intent=new Intent(MainActivity.this,LoginActivity.class);
			        //MainActivity.this.startActivity(intent);
			        
			        //Intent intent = new Intent(FirstActivity.this, SecondActivity.class);   
			        startActivity(intent);
			                       
			        //添加界面切换效果，注意只有Android的2.0(SdkVersion版本号为5)以后的版本才支持   
			        int version = Integer.valueOf(android.os.Build.VERSION.SDK);      
			        if(version  >= 5) {      
			             //overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  //此为自定义的动画效果，下面两个为系统的动画效果   
			           //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);     
			             overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);  
			        }
			        MainActivity.this.finish();
			}
		}, 500);
       
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
