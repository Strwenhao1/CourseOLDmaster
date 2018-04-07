package com.coursemis.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.coursemis.R;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class EvaluateAddActivity extends Activity {
	public Context context;
	private AsyncHttpClient client;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	private Button back;
	private TextView top_title;
	private Button button_finish;
	private RadioGroup rg_option1;
	private RadioGroup rg_option2;
	private RadioGroup rg_option3;
	private int option1=-1;
	private int option2=-1;
	private int option3=-1;
	private EditText feekback_edit;
	
	private int studentid; 
	private int courseid;
	private String feekback_idea;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_add);
		
		this.context = this;
		client = new AsyncHttpClient();
		
		preferences = getSharedPreferences("courseMis", 0);
		editor = preferences.edit();
		
		studentid = preferences.getInt("studentid", 0);//0为默认值
		
		Intent intent = getIntent();
		//studentid = intent.getExtras().getInt("studentid");
		courseid = intent.getExtras().getInt("courseid");
		
		back = (Button) findViewById(R.id.reback_btn);
//		top_title = (TextView)findViewById(R.id.tv_title);
		top_title.setText("课程评分");
		button_finish = (Button) findViewById(R.id.evaluatefinish_btn);
		rg_option1 = (RadioGroup) findViewById(R.id.option1);
		rg_option2 = (RadioGroup) findViewById(R.id.option2);
		rg_option3 = (RadioGroup) findViewById(R.id.option3);
		feekback_edit=(EditText) findViewById(R.id.feekback_edit);
		
		rg_option1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.A1:
						option1=10;
						break;
					case R.id.B1:
						option1=8;
						break;
					case R.id.C1:
						option1=6;
						break;
					case R.id.D1:
						option1=4;
						break;
					case R.id.E1:
						option1=2;
						break;
					default:
						break;
				}
			}
		});
		rg_option2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
					case R.id.A2:
						option2=10;
						break;
					case R.id.B2:
						option2=8;
						break;
					case R.id.C2:
						option2=6;
						break;
					case R.id.D2:
						option2=4;
						break;
					case R.id.E2:
						option2=2;
						break;
					default:
							break;
				}
			}
		});
		rg_option3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
					case R.id.A3:
						option3=10;
						break;
					case R.id.B3:
						option3=8;
						break;
					case R.id.C3:
						option3=6;
						break;
					case R.id.D3:
						option3=4;
						break;
					case R.id.E3:
						option3=2;
						break;
					default:
							break;
				}
				
			}
		});
		
		//返回按钮
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EvaluateAddActivity.this.finish();
			}
		});
		button_finish.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validate()){
					RequestParams params = new RequestParams();
					params.put("studentid", studentid+"");
					params.put("courseid", courseid+"");
					params.put("idea", feekback_idea);
					params.put("option1", option1+"");
					params.put("option2", option2+"");
					params.put("option3", option3+"");
					
					params.put("power", ""); ///
					params.put("action", "login");///
					
					client.post(HttpUtil.server_evaluate, params,
							new JsonHttpResponseHandler() {
	
								@Override
								public void onSuccess(int arg0, JSONObject arg1) {
									// TODO Auto-generated method stub
									
									String addEvaluate_msg = arg1.optString("result");
//									DialogUtil.showDialog(context, addEvaluate_msg, true);
									
									final AlertDialog.Builder builder=new AlertDialog.Builder(EvaluateAddActivity.this);
									builder.setTitle("温馨提示");
									if(addEvaluate_msg.equals("添加评分到该课程成功")){
										builder.setMessage(addEvaluate_msg);
										builder.setPositiveButton("退出", new DialogInterface.OnClickListener (){

											@Override
											public void onClick(DialogInterface arg0,int arg1) {
												Intent intent = new Intent(EvaluateAddActivity.this, StudentMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//												Bundle bundle = new Bundle();
//												bundle.putInt("studentid", studentid);
//												bundle.putString("type", "学生");
//												intent.putExtras(bundle);
//												Log.e("tag", "onClick: ",null );
												EvaluateAddActivity.this.startActivity(intent);
												EvaluateAddActivity.this.finish();

											}
										});
//										builder.setNegativeButton("继续评教", new DialogInterface.OnClickListener () {
//
//											@Override
//											public void onClick(DialogInterface dialog, int which) {
//												EvaluateAddActivity.this.finish();
//											}
//										});
									}else{
										builder.setMessage(addEvaluate_msg+",请稍后再来");
										builder.setPositiveButton("确定", new DialogInterface.OnClickListener (){

											@Override
											public void onClick(DialogInterface dialog,int which) {
												Intent intent = new Intent(EvaluateAddActivity.this, StudentMainActivity.class);
												Bundle bundle = new Bundle(); 
												bundle.putInt("studentid", studentid);
												bundle.putString("type", "学生");
												intent.putExtras(bundle);
												EvaluateAddActivity.this.startActivity(intent);
												EvaluateAddActivity.this.finish();
											}
										});
									}
									builder.create().show();

									super.onSuccess(arg0, arg1);
								
								}
							
					});
				}
			}
		});
		
	}

	// 对学生输入的评分情况进行校验
	private boolean validate(){
		feekback_idea=feekback_edit.getText().toString().trim();
		if (option1==-1||option2==-1||option3==-1)
		{
			DialogUtil.showDialog(this, "请评分完毕再点击完成！", false);
			return false;
		}else if(TextUtils.isEmpty(feekback_idea)){
			DialogUtil.showDialog(this, "请填写课程建议", false);
			return false;
		}
		return true;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evaluate_add, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();

	}
}
