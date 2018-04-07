
package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursemis.R;
import com.coursemis.view.myView.TitleView;

/**
* @author nebo
* @E-mail:nebofeng@gmail.com
* @version creatTime：2017年4月23日 下午4:02:09
* 类说明:课堂活动界面 （1，课堂点名， 2. 随机提问 ，3 ：（课堂测试，实现与否待定
*/
public class ClassActivities extends Activity  implements OnClickListener{
	
	
	
	public ImageView img_back;
	public TextView tv_class_signin,tv_random_question;
	private TitleView mTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView() ;
		initData() ;
	}

	private void initData() {
		setListener();
		mTitleView.setTitle("课堂活动");
		mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
			@Override
			public void onClick(View button) {
				ClassActivities.this.finish();
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_class_activities);
		img_back=(ImageView) this.findViewById(R.id.title_left_imageview);
		tv_class_signin=(TextView) this.findViewById(R.id.class_signin);
		tv_random_question=(TextView) this.findViewById(R.id.random_question);
		mTitleView = (TitleView) findViewById(R.id.class_title);

	}


	/*
	 *给view注册监听事件 
	 */
	public  void setListener() {
		//img_back.setOnClickListener(this);
		tv_class_signin.setOnClickListener(this);
		tv_random_question.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch(v.getId()){
			case R.id.class_signin:
				Intent i = new Intent(ClassActivities.this, TMentionNameActivity.class);//
				Bundle bundle = new Bundle(); 
				i.putExtras(bundle);
				ClassActivities.this.startActivity(i);
				break;
			case R.id.random_question:
				Intent ran = new Intent(ClassActivities.this, TRandomAskActivity.class);//
				Bundle bundle1 = new Bundle(); 
				ran.putExtras(bundle1);
				ClassActivities.this.startActivity(ran);
				break;
		}
	}
	
}
