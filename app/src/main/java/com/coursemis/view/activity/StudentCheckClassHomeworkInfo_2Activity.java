package com.coursemis.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.coursemis.service.WebService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StudentCheckClassHomeworkInfo_2Activity extends Activity {
	private Button back=null;
	private Intent intent=null;
	private TextView title = null;
	private AsyncHttpClient client = new AsyncHttpClient();
	private List<String>studentCourseHomeworkInfo = null;
	String name = null;
	byte[] data=null;
	String sm=null;
	private ListView lv=null;
	private Handler handler =new Handler(){ 
		 
		@Override 
		 
		//当有消息发送出来的时候就执行Handler的这个方法 
		 
		public void handleMessage(Message msg){ 
		 
		super.handleMessage(msg); 
		 
		//处理UI 
		Bitmap bit=BitmapFactory.decodeByteArray(data, 0, data.length);
		saveMyBitmap(sm+"_副本",bit);
		Toast.makeText(StudentCheckClassHomeworkInfo_2Activity.this,"作业已经下载", Toast.LENGTH_SHORT).show();
//		Intent intent = new Intent(HomeworkCourseCSubmitInfoActivity.this,ImageViewActivity.class);
//		intent.putExtra("bitmap", sm+"_副本.JPG");
//		startActivity(intent);
		} 
		 
		};
	
	public void ButtonOnclick_StudentCheckClassHomeworkInfo_2_back(View view)
	{
		finish();
	}
	public void ButtonOnclick_StudentCheckClassHomeworkInfo_2_down(View view)
	{
		if(name==null)
		{
			Toast.makeText(StudentCheckClassHomeworkInfo_2Activity.this,"您还没有选择具体那一份作业", Toast.LENGTH_SHORT).show();
		}else
		{
			sm = name.substring(0,name.indexOf(".")).trim();
			Log.v("看下照片的名字",name);
			
			
			
			final String address=HttpUtil.server+"/studentHomeWork"+"/"+name;
			
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
				
				Toast.makeText(StudentCheckClassHomeworkInfo_2Activity.this, "下载出错", 1).show();
			}
			
		}
		
	}
	
	public void ButtonOnclick_StudentCheckClassHomeworkInfo_2_look(View view)
	{
		Intent intent = new Intent(StudentCheckClassHomeworkInfo_2Activity.this,ImageViewActivity.class);
		intent.putExtra("bitmap", sm+"_副本.JPG");
		startActivity(intent);
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_check_class_homework_info_2);
		intent = getIntent();
		title=(TextView)findViewById(R.id.StudentCheckClassHomeworkInfo_2_tv);
		title.setText(intent.getStringExtra("courseinfo"));
		lv=(ListView)findViewById(R.id.relativelayout_ascchi2);
		studentCourseHomeworkInfo = intent.getStringArrayListExtra("studentCourseHomeworkInfo");
		ArrayList<String> studentCourseHomeworkInfo_temp=new ArrayList<String>();
		for(int i=0;i<studentCourseHomeworkInfo.size();i++)
		{
			String temp=studentCourseHomeworkInfo.get(i);
			studentCourseHomeworkInfo_temp.add("学生名:    "+temp.substring(temp.indexOf(" ")+1,temp.length()));
		}
		ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_checked, studentCourseHomeworkInfo_temp);
		lv.setAdapter(aaRadioButtonAdapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put("shinfo", studentCourseHomeworkInfo.get(arg2)+"");
//				params.put("sid", sid+"");
				 client.post(HttpUtil.server_student_StudentClassHMPath, params,
							new JsonHttpResponseHandler(){
					  @Override
						public void onSuccess(int arg0, JSONObject arg1) {
						 
							
							name = arg1.optString("result");
						  
						  
						  super.onSuccess(arg0, arg1); 
					  }
				  });
			}}
		);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_check_class_homework_info_2,
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
