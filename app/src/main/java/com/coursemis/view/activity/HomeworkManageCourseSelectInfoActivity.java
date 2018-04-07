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
import com.coursemis.service.WebService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeworkManageCourseSelectInfoActivity extends Activity {
	
	Intent intent = null;
	String title =null;
	TextView tv = null;
	String sm=null;
	Button back=null;
	String text3=null;
	private ListView hms=null;
	byte[] data=null;
	private Button check = null;
	private AsyncHttpClient client=new AsyncHttpClient();
	private Handler handler =new Handler(){ 
		 
		@Override 
		 
		//当有消息发送出来的时候就执行Handler的这个方法 
		 
		public void handleMessage(Message msg){ 
		 
		super.handleMessage(msg); 
		 
		//处理UI 
		Bitmap bit=BitmapFactory.decodeByteArray(data, 0, data.length);
		saveMyBitmap(sm+"_副本",bit);
		Toast.makeText(HomeworkManageCourseSelectInfoActivity.this,"作业已经下载", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(HomeworkManageCourseSelectInfoActivity.this,ImageViewActivity.class);
		intent.putExtra("bitmap", sm+"_副本.JPG");
		startActivity(intent);
		} 
		 
		}; 
	public void ButtonOnclick_homeworkmanagecourseselect_info__upload(View view)
	{
		
//		String smid = text3.substring(0,text3.indexOf(" "));
		SharedPreferences  sharedata=getSharedPreferences("courseMis", 0);
		int tid = Integer.parseInt(sharedata.getString("userID",null));
		
		String cid = title.substring(0,title.indexOf(" "));
//		intent.putExtra("cid", cid);
//		intent.putExtra("smid", smid);
		Intent intent2  = new Intent(HomeworkManageCourseSelectInfoActivity.this,ImageActivity.class);
		intent2.putExtra("tid", tid+"");
		intent2.putExtra("cid", cid+"");
		startActivity(intent2);
		
	}
	
	
	public void ButtonOnclick_homeworkmanagecourseselect_info__down(View view)
	{
	
		if(text3==null)
		{
			Toast.makeText(HomeworkManageCourseSelectInfoActivity.this,"您没有选择哪份作业", Toast.LENGTH_SHORT).show();
		}else
		{
			String smname = text3.substring(text3.indexOf(" "),text3.length());
			smname = smname.substring(1,smname.length());
			sm = smname.substring(0,smname.indexOf(".")).trim();
			Log.v("看下照片的名字",smname);
			
			
			Toast.makeText(HomeworkManageCourseSelectInfoActivity.this,"作业下载中，请耐心等待...", Toast.LENGTH_SHORT).show();
			final String address=HttpUtil.server+"/teacherHomeWork"+"/"+smname;
			
			try {
				new Thread(){ 
					 
					@Override 
					 
					public void run(){ 
					 
						data=WebService.getImage(address); //得到图片的输入流
					 
					//执行完毕后给handler发送一个空消息 
					 
					handler.sendEmptyMessage(0); 
					 
					} 
					 
					}.start();
		
//				
//				//二进制数据生成位图
				
//				
				 
			} catch (Exception e) {
			    Log.e("NetActivity", e.toString());
				
				Toast.makeText(HomeworkManageCourseSelectInfoActivity.this, "下载出错", 1).show();
			}
			
		}
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		intent =getIntent();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homeworkmanagecourseselect_info);
		
		final List<String>  sml = intent.getStringArrayListExtra("sourcemanagelist");
		List<String>  sml_temp=new ArrayList<String>();
		hms=(ListView)findViewById(R.id.homeworkmanagecourseinfoListview);
		tv = (TextView)findViewById(R.id.homeworkmanagecourseselect_info__textview);
		
		back=(Button)findViewById(R.id.homeworkmanagecourseselect_info__back);
		check=(Button)findViewById(R.id.homeworkmanagecourseselect_info__check);
		
		title = intent.getStringExtra("title");
		tv.setText(title.substring(title.indexOf(" ")+1,title.length()));
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}});
		
		
		for(int i=0;i<sml.size();i++)
		{
			String temp=sml.get(i);
			String temp1 = temp.substring(0,temp.indexOf(" "));
			String temp2 = temp.substring(temp.indexOf(" ")+1,temp.length()); 
			String temp3 = temp2.substring(0,temp2.indexOf(" "));
			sml_temp.add("编号:		"+temp1+"文件名：		"+temp3);
		}
		
		
		
		ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_checked, sml_temp);
		hms.setAdapter(aaRadioButtonAdapter);
		hms.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		hms.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String temp_string = sml.get(arg2);
				text3 = (String)temp_string.substring(0,temp_string.lastIndexOf(" "));
				
			}
			
		});
		
		
		
		
		
		
		
		check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(text3==null)
				{
					Toast.makeText(HomeworkManageCourseSelectInfoActivity.this,"您没有选择哪份作业", Toast.LENGTH_SHORT).show();
				}else
				{
					P.p("insert");
					RequestParams params = new RequestParams();
					params.put("sminfo", text3);
					client.post(HttpUtil.server_teacher_check_homework, params,
							new JsonHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, JSONObject arg1) {
									JSONArray object = arg1.optJSONArray("result");
									P.p(object.toString()+1111);
									
									if(object.length()==0){
										Toast.makeText(HomeworkManageCourseSelectInfoActivity.this,"您还没有学生提交这门作业", Toast.LENGTH_SHORT).show();
									}else{
										ArrayList<String> list=new ArrayList<String>();
										for(int i=0;i<arg1.optJSONArray("result").length();i++){
											JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
											P.p(object_temp.toString()+2222);
											list.add(i, (object_temp.optInt("shid")+" "+object_temp.optString("shname")));
											}
										
										Intent intent = new Intent(HomeworkManageCourseSelectInfoActivity.this,HomeworkManageJobInfoActivity.class);
										intent.putStringArrayListExtra("smlist", list);
										intent.putExtra("title", text3);
										
										startActivity(intent);
									}
									
									super.onSuccess(arg0, arg1);
								}
							});
				}
				
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homework_manage_course_select_info,
				menu);
		return true;
	}

	   /**
	    *保存到sd卡上的方法
	    */
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
//http://192.168.173.1:8080/upload_serve/image/zb.JPG	
}
