package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.coursemis.service.WebService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeworkCourseCSubmitInfoActivity extends Activity {
	private ListView hccsi  = null;
	private TextView title =null;
	private List<String>studentCourseHomeworkInfo = null;
	private String courseinfo = null;
	private Intent intent = null;
	private String homework=null;
	byte[] data=null;
	String sm=null;
	private Handler handler =new Handler(){ 
		 
		@Override 
		 
		//当有消息发送出来的时候就执行Handler的这个方法 
		 
		public void handleMessage(Message msg){ 
		 
		super.handleMessage(msg); 
		 
		//处理UI
		if(data!=null)
		{
		Bitmap bit=BitmapFactory.decodeByteArray(data, 0, data.length);
		saveMyBitmap(sm+"_副本",bit);
		Toast.makeText(HomeworkCourseCSubmitInfoActivity.this,"作业已经下载", Toast.LENGTH_SHORT).show();
		}else
		{
			Toast.makeText(HomeworkCourseCSubmitInfoActivity.this,"服务器数据出现错误，该作业不存在", Toast.LENGTH_SHORT).show();
		}
		//	Intent intent = new Intent(HomeworkCourseCSubmitInfoActivity.this,ImageViewActivity.class);
//		intent.putExtra("bitmap", sm+"_副本.JPG");
//		startActivity(intent);
		} 
		 
		}; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_homework_course_csubmit_info);
		hccsi =(ListView)findViewById(R.id.homeworkcoursecsubmitinfo_Listview);
		intent = getIntent();
		title= (TextView)findViewById(R.id.homeworkcoursesubmitInfo_info__textview);
		courseinfo=intent.getStringExtra("courseinfo");
		studentCourseHomeworkInfo = intent.getStringArrayListExtra("studentCourseHomeworkInfo");
		ArrayList<String> studentCourseHomeworkInfo_temp = new  ArrayList<String>();
		title.setText(courseinfo.substring(courseinfo.indexOf(" ")+1,courseinfo.length()));
		
		for(int i=0;i<studentCourseHomeworkInfo.size();i++)
		{
			String temp = studentCourseHomeworkInfo.get(i);
			String smname =temp.substring(temp.indexOf(" ")+1,temp.indexOf("_"));
			String flag = temp.substring(temp.indexOf("_")+1,temp.length());
			studentCourseHomeworkInfo_temp.add(smname+" "+flag);
		}
		
		
		ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_checked, studentCourseHomeworkInfo_temp);
		hccsi.setAdapter(aaRadioButtonAdapter);
		hccsi.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		hccsi.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				homework =studentCourseHomeworkInfo.get(arg2)+"";
			}
			
		});
		

		
		
		
		
		
	}

	public void ButtonOnclick_homeworkcoursesubmitInfo_info__check(View view)
	{
		if(sm==null)
		{
			Toast.makeText(HomeworkCourseCSubmitInfoActivity.this,"作业没有下载", Toast.LENGTH_SHORT).show();
		}else
		{
			Intent intent = new Intent(HomeworkCourseCSubmitInfoActivity.this,ImageViewActivity.class);
			intent.putExtra("bitmap", sm+"_副本.JPG");
			startActivity(intent);
		}
	}
	
	public void ButtonOnclick_homeworkmanagecourseselect_info__down(View view)
	{
		if(homework==null)
		{
			Toast.makeText(HomeworkCourseCSubmitInfoActivity.this,"您没有选择哪份作业", Toast.LENGTH_SHORT).show();
		}else
		{
			String smname = homework.substring(homework.indexOf(" ")+1,homework.indexOf("_"));
			
			sm = smname.substring(0,smname.indexOf(".")).trim();
			Log.v("看下照片的名字",smname);
			
			
			
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
				
				Toast.makeText(HomeworkCourseCSubmitInfoActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	
	public void ButtonOnclick_homeworkmanagecourseselect_info__upload(View view)
	{
		if(homework==null)
		{
			Toast.makeText(HomeworkCourseCSubmitInfoActivity.this, "您还没有选择哪份作业", Toast.LENGTH_SHORT).show();
		}else
		{
			String temp = homework.substring(homework.indexOf("_"),homework.length());
			if(temp.equals("_已交"))
			{
			Toast.makeText(HomeworkCourseCSubmitInfoActivity.this, "这份作业您已经提交，不能重复提交", Toast.LENGTH_SHORT).show();	
			}else
			{
//				String smid = text3.substring(0,text3.indexOf(" "));
				SharedPreferences  sharedata=getSharedPreferences("courseMis", 0);
				int sid = Integer.parseInt(sharedata.getString("userID",null));
				
				String smid = homework.substring(0,homework.indexOf(" "));
//				intent.putExtra("cid", cid);
//				intent.putExtra("smid", smid);
				Intent intent2  = new Intent(HomeworkCourseCSubmitInfoActivity.this,StudentHMUploadActivity.class);
				intent2.putExtra("smid", smid);
				intent2.putExtra("sid", sid+"");
				startActivity(intent2);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homework_course_csubmit_info, menu);
		return true;
	}
	
	public void buttonOnclick_homeworkcoursesubmitInfo_info__back(View view){
		finish();
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
