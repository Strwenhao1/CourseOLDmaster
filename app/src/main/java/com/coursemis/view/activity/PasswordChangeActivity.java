package com.coursemis.view.activity;

import org.json.JSONObject;


import com.coursemis.R;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PasswordChangeActivity extends Activity {
	public Context context;///
	private EditText password_old;
	private EditText password_new;
	private EditText password_conform;
	
	private Button back;
	private TextView top_title;
	private Button button_finish;
	
	private int teacherid;
	private int studentid;
	private String type;
	
	private AsyncHttpClient client;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_change);
		
		this.context = this;
		client = new AsyncHttpClient();
		
		preferences = getSharedPreferences("courseMis", 0);
		editor = preferences.edit();
		
		password_old = (EditText) findViewById(R.id.password_old);
		password_new = (EditText) findViewById(R.id.password_new);
		password_conform = (EditText) findViewById(R.id.password_conform);
		
		back=(Button)findViewById(R.id.reback_btn);
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PasswordChangeActivity.this.finish();
			}
			
		});
		
		top_title = (TextView)findViewById(R.id.title);
		top_title.setText("         密码修改             ");
		button_finish = (Button) findViewById(R.id.continue_btn);
		
		
		
		/*Intent intent = getIntent();
		type = intent.getExtras().getString("type");
*/		
		type = preferences.getString("type", "");
		
		if(type.equals("教师")){
			//teacherid = intent.getExtras().getInt("teacherid");
			teacherid = preferences.getInt("teacherid", 0);//0为默认值
		}
		else{
			//studentid = intent.getExtras().getInt("studentid");
			studentid = preferences.getInt("studentid", 0);//0为默认值
		}
		
		button_finish.setOnClickListener
		(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validate()){
					RequestParams params = new RequestParams();
					params.put("password_old", password_old.getText().toString().trim());
					params.put("password_new1", password_new.getText().toString().trim());
					params.put("password_new2", password_conform.getText().toString().trim());
					
					params.put("type", type);//用户类型
					if(type.equals("教师")){
						params.put("teacherid", teacherid+"");//用户类型为教师
					}
					else{
						params.put("studentid", studentid+"");//用户类型为教师
					}
					
					
					params.put("power", ""); ///
					params.put("action", "login");///
					
					client.post(HttpUtil.server_pwd_change, params,
							new JsonHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, JSONObject arg1) {
									// TODO Auto-generated method stub
									System.out.println(arg1.toString());
									String pwdChange_msg = arg1.optString("result");
									DialogUtil.showDialog(context, pwdChange_msg, true);
									
									if(type.equals("教师")){
										Intent intent = new Intent(PasswordChangeActivity.this, WelcomeActivity.class);
										Bundle bundle = new Bundle(); 
										bundle.putInt("teacherid", teacherid);
										intent.putExtras(bundle);
										PasswordChangeActivity.this.startActivity(intent);
									}
									else{
										Intent intent = new Intent(PasswordChangeActivity.this, StudentMainActivity.class);
										Bundle bundle = new Bundle(); 
										bundle.putInt("studentid", studentid);
										intent.putExtras(bundle);
										PasswordChangeActivity.this.startActivity(intent);
									}
									
									PasswordChangeActivity.this.finish();
								
									super.onSuccess(arg0, arg1);
								}

							});
					
				}
				
				
				
			}
			
		});
		
	}
	
	// 对用户输入的用户名、密码进行校验
	private boolean validate()
	{
		String old = password_old.getText().toString().trim();
		if (old.equals(""))
		{
			DialogUtil.showDialog(this, "用户原始密码是必填项！", false);
			return false;
		}
		String pwd1 = password_new.getText().toString().trim();
		if (pwd1.equals(""))
		{
			DialogUtil.showDialog(this, "新密码是必填项！", false);
			return false;
		}
		String pwd2 = password_conform.getText().toString().trim();
		if (pwd2.equals(""))
		{
			DialogUtil.showDialog(this, "确认密码是必填项！", false);
			return false;
		}
		if (!pwd2.equals(pwd1))
		{
			DialogUtil.showDialog(this, "确认密码失败，必须与新密码相同！", false);
			return false;
		}
		if (pwd2.equals(old))
		{
			DialogUtil.showDialog(this, "新密码不得与原密码相同！", false);
			return false;
		}
		return true;
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.password_change, menu);
		return true;
	}

}
