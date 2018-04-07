package com.coursemis.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeworkManageJobInfoActivity extends Activity {

	private AsyncHttpClient client=new AsyncHttpClient();
	TextView tvinput=null;
	Intent intent = null;
	TextView tv = null;
	Button back=null;
	Button  check=null;
	Button comment=null;
	Button download= null;
	String title=null;
	String homeworkinfo=null;
	String sm=null;
	byte[] data=null;
	String smname=null;
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
	private Handler handler =new Handler(){ 
		 
		@Override 
		 
		//当有消息发送出来的时候就执行Handler的这个方法 
		 
		public void handleMessage(Message msg){ 
		 
		super.handleMessage(msg); 
		 
		//处理UI 
		
		if(data!=null){
			Bitmap bit=BitmapFactory.decodeByteArray(data, 0, data.length);
		saveMyBitmap(sm+"_副本",bit);
		Toast.makeText(HomeworkManageJobInfoActivity.this,"学生作业已经下载", Toast.LENGTH_SHORT).show();
		}else
		{
			Toast.makeText(HomeworkManageJobInfoActivity.this,"学生作业不存在，服务器数据出现错误..", Toast.LENGTH_SHORT).show();
		}
		//		Intent intent = new Intent(HomeworkManageCourseSelectInfoActivity.this,ImageViewActivity.class);
//		intent.putExtra("bitmap", sm+"_副本.JPG");
//		startActivity(intent);
		} 
		 
		}; 
		public void buttonOnclick_homeworkmanagejob_look(View view)
		{
			if(sm==null)
			{
				Toast.makeText(HomeworkManageJobInfoActivity.this,"您没有选择哪份作业且下载~", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(HomeworkManageJobInfoActivity.this,ImageViewActivity.class);
				intent.putExtra("bitmap", sm+"_副本.JPG");
				startActivity(intent);
			}
		}
		
		
		
		public void buttonOnclick_homeworkmanagejob_down(View view)
		{
			if(homeworkinfo==null)
			{
				Toast.makeText(HomeworkManageJobInfoActivity.this,"您没有选择是哪门作业", Toast.LENGTH_SHORT).show();
			}else
			{
				String smname = homeworkinfo.substring(homeworkinfo.indexOf(" "),homeworkinfo.length());
				smname = smname.substring(1,smname.length());
				sm = smname.substring(0,smname.indexOf(".")).trim();
				
				final String address=HttpUtil.server+"/studentHomeWork"+"/"+smname;
				
				try {
					new Thread(){ 
						 
						@Override 
						 
						public void run(){ 
						 
							data=WebService.getImage(address); //得到图片的输入流
						 
						//执行完毕后给handler发送一个空消息 
						 
						handler.sendEmptyMessage(0); 
						 
						} 
						 
						}.start();	
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homework_manage_job_info);
		intent = getIntent();
		List<String>shl = intent.getStringArrayListExtra("shlist");
		LinearLayout ll=(LinearLayout)findViewById(R.id.hmmji);
		List<String>  sml = intent.getStringArrayListExtra("smlist");
		tv = (TextView)findViewById(R.id.homeworkmanagejob_textview);
		
		back=(Button)findViewById(R.id.homeworkmanagejob_back);
		check=(Button)findViewById(R.id.homeworkmanagecourseselect_info__check);
		comment = (Button)findViewById(R.id.homeworkmanagejob_comment);
		download=(Button)findViewById(R.id.homeworkmanagejob_down);
		
		title = intent.getStringExtra("title");
//		tv.setText(title);
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}});
		
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(homeworkinfo==null)
				{
					Toast.makeText(HomeworkManageJobInfoActivity.this,"您没有选择哪份作业", Toast.LENGTH_SHORT).show();
				}else
				{
					final EditText etinput =   new EditText(HomeworkManageJobInfoActivity.this);
					P.p("final");
					
					
					final AlertDialog dialog = new AlertDialog.Builder(HomeworkManageJobInfoActivity.this)  
			        .setTitle("选择课程").setView(etinput).setPositiveButton("确定",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(etinput.getText().toString().trim()!=null)
							{
							// TODO Auto-generated method stub
							RequestParams params = new RequestParams();
							params.put("shinfo", homeworkinfo);
							params.put("score", etinput.getText().toString().trim());
							client.post(HttpUtil.server_teacher_comment_homework, params,
									new JsonHttpResponseHandler() {

										@Override
										public void onSuccess(int arg0, JSONObject arg1) {
										
												Toast.makeText(HomeworkManageJobInfoActivity.this,"成功批阅！", Toast.LENGTH_SHORT).show();
											
											super.onSuccess(arg0, arg1);
										}
									});
							}else
							{
								Toast.makeText(HomeworkManageJobInfoActivity.this,"您还没有输入分数", Toast.LENGTH_SHORT).show();
							}
							
							
							
							
						}
					} ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).setMessage("请注意请输入数字值！").setTitle("提示").create()//在这里把写好的这个listview的布局加载dialog中  
			       ;
					
					dialog.show();
				}
				
				
			}
		});
		
		
		
		for(int i=0;i<sml.size();i++)
		{
			final Button btn = new Button(this);
			btn.setText(sml.get(i));
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Intent intent = new Intent(HomeworkManageCourseSelectInfoActivity.this,HomeworkManageJobInfoActivity.class);
					
					homeworkinfo = (String)btn.getText();
					
				}
			});
			
			
			
			P.p("叭叭叭");
			P.p(sml.get(i));
			
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ll.addView(btn,lp1);
		
		
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homework_manage_job_info, menu);
		return true;
	}

}
