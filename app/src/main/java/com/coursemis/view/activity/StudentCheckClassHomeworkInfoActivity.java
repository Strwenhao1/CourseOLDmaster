package com.coursemis.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentCheckClassHomeworkInfoActivity extends Activity {
	private TextView title =null;
	private List<String>studentCourseHomeworkInfo = null;
	private String courseinfo = null;
	private Intent intent = null;
	private String homework=null;
	private String name = null;
	private ListView scchi=null;
	byte[] data=null;
	String sm=null;
	private AsyncHttpClient client = new AsyncHttpClient();

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_check_class_homework_info);
		intent = getIntent();
		courseinfo=intent.getStringExtra("courseinfo");
		title=(TextView)findViewById(R.id.studentcheckclasshomeworkinfo__textview);
		title.setText(courseinfo.substring(courseinfo.indexOf(" ")+1,courseinfo.length()));
		studentCourseHomeworkInfo = intent.getStringArrayListExtra("studentCourseHomeworkInfo");
		final ArrayList<String> studentCourseHomeworkInfo_temp=new ArrayList<String>();
		scchi=(ListView)findViewById(R.id.studentcheckclasshomeworkinfo_listview);
		for(int i=0;i<studentCourseHomeworkInfo.size();i++)
		{
			studentCourseHomeworkInfo_temp.add("第"+studentCourseHomeworkInfo.get(i)+"次作业");
		}
		
		ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_checked, studentCourseHomeworkInfo_temp);
		scchi.setAdapter(aaRadioButtonAdapter);
		scchi.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		scchi.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put("courseinfo", studentCourseHomeworkInfo_temp.get(arg2)+"");
				 client.post(HttpUtil.server_student_StudentClassCourseCheckhomework, params,
							new JsonHttpResponseHandler(){
					  @Override
						public void onSuccess(int arg0, JSONObject arg1) {
						  JSONArray object = arg1.optJSONArray("result");
							
							if(object.length()==0){
								Toast.makeText(StudentCheckClassHomeworkInfoActivity.this,"这门课程尚未有任何学生提交作业", Toast.LENGTH_SHORT).show();
							}else{
								ArrayList<String> list=new ArrayList<String>();
								
								for(int i=0;i<arg1.optJSONArray("result").length();i++){
									JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
									P.p(object_temp.toString()+2222);
									list.add(i, object_temp.optInt("shid")+" "+object_temp.optString("sname")+"  分数"+object_temp.optInt("score"));
									}
								
								Intent intent = new Intent(StudentCheckClassHomeworkInfoActivity.this,StudentCheckClassHomeworkInfo_2Activity.class);
								intent.putExtra("courseinfo", studentCourseHomeworkInfo_temp.get(arg2)+"");
								intent.putStringArrayListExtra("studentCourseHomeworkInfo", list);
								
								startActivity(intent);
							}
						  
						  
						  super.onSuccess(arg0, arg1); 
					  }
				  });
			}});
		
	}	
	
	public void buttonOnclick_studentcheckclasshomeworkinfo__back(View view){
		
		
		finish();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_check_class_homework_info,
				menu);
		return true;
	}

	
	public void saveMyBitmap(String bitName,Bitmap mBitmap){
		   File f = new File("/sdcard/" + bitName + ".JPG");
		   try {
		    f.createNewFile();
		   } catch (IOException e) {
		    // TODO Auto-generated catch block
		    Log.v("在保存图片时出错：",e.toString());
		   }
		   FileOutputStream fOut = null;
		   try {
		    fOut = new FileOutputStream(f);
		   } catch (FileNotFoundException e) {
		    e.printStackTrace();
		   }
		   mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		   try {
		    fOut.flush();
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		   try {
		    fOut.close();
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		  } 
	
}
