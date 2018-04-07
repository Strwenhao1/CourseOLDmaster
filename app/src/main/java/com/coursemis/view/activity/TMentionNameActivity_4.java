package com.coursemis.view.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import com.coursemis.view.myView.TitleView;
import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class TMentionNameActivity_4 extends Activity{
	private TitleView mTitle=null;
	
	Intent intent =null;
	ArrayList<String> studentName=null;
	String TMN_C_INFO=null;
	TextView textView=null;
	ListView studentList=null;
	
	AsyncHttpClient client = new AsyncHttpClient();
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tmentionnameactivity_4);
		intent=getIntent();
		studentName=intent.getStringArrayListExtra("course_Name_list");
		TMN_C_INFO=intent.getStringExtra("tmn_c_info");
		mTitle=(TitleView)findViewById(R.id.title_dianming2);
		mTitle.setTitle(TMN_C_INFO);
		LinearLayout ll=(LinearLayout)findViewById(R.id.layout_1);
		final int amount =studentName.size();
		
		final String[] studentNameArray=new String[amount];
		final String[] studentNameArrayTemp=new String[amount];
		for(int j=0;j<amount;j++)
		{
			String temp = studentName.get(j);
			String temp1 = temp.substring(temp.indexOf(" ")+1,temp.length());
			String temp2 = null;
			for(int i =0;i<temp1.length();i++)
			{
				if(temp1.charAt(i)>='0'&&temp1.charAt(i)<='9')
				{
					temp2 = temp1.charAt(i)+"";
				}
			}
			String temp3 = temp1.replace(temp2, "");
			studentNameArray[j]=studentName.get(j);
			studentNameArrayTemp[j]="学号：	"+temp2+"	姓名：	"+temp3;
			
		}
		studentList = (ListView) findViewById(R.id.studentList);        
		 ArrayAdapter<String> a = new ArrayAdapter<String>(this,
		         	android.R.layout.simple_list_item_multiple_choice, studentNameArrayTemp);
        studentList.setAdapter(a);
        studentList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
        mTitle.setLeftButton(R.string.back, new TitleView.OnLeftButtonClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
        mTitle.setRightButton(R.string.confirm, new TitleView.OnRightButtonClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(amount==0)
				{
					Toast.makeText(TMentionNameActivity_4.this,"您这门课没有学生选修!", Toast.LENGTH_SHORT).show();
				}else
				{
					RequestParams params = new RequestParams();
					 for(int i = 0; i < studentNameArray.length; i++) {
				        	if (studentList.isItemChecked(i))
				        		params.put(i+"",studentNameArray[i]);
				        }
					 client.post(HttpUtil.server_teacher_courseStudentCount, params,
								new JsonHttpResponseHandler() {
						 			
									@Override
									public void onSuccess(int arg0, JSONObject arg1) {
									
										super.onSuccess(arg0, arg1);
									}
								});
					 finish();
				}
				
			}});
		
		
		}
	
	

}
