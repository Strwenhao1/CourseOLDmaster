package com.coursemis.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.coursemis.R;
import com.coursemis.model.LocationData;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class StudentMainActivity extends Activity{

	private AsyncHttpClient client;
	SharedPreferences  sharedata;
	private Button student_checkSign =null;
	private Button student_signIn =null;
	private Button student_checkHomework=null;
	private Button button_coursetable;
	private Button button_evaluate;
	private int sid;
	private Location currentLocation;
	private String best;
	private TextView mTv = null;

	private Button   mStartBtn;
	private boolean  mIsStart;
	private static int count = 1;
	private Vibrator mVibrator01 =null;
	private LocationClient mLocClient;
	public static String TAG = "LocTestDemo11";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studentwelcomeactivity);
		LocationData.latitude=0.0;
		LocationData.longitude=0.0;
		LocationData.radius=0.0f;
		//百度定位参数设置
		mTv = (TextView)findViewById(R.id.student_location);

		mStartBtn = (Button)findViewById(R.id.startStudentLoc);


		mIsStart = false;

		mLocClient = ((Location)getApplication()).mLocationClient;
		((Location)getApplication()).mTv = mTv;
		mVibrator01 =(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		((Location)getApplication()).mVibrator01 = mVibrator01;


		client = new AsyncHttpClient();

		sharedata=getSharedPreferences("courseMis", 0);
		sid = Integer.parseInt(sharedata.getString("userID",null));

		student_checkSign=(Button)findViewById(R.id.student_checkSign);
		student_signIn=(Button)findViewById(R.id.s_signIn);
		student_checkHomework=(Button)findViewById(R.id.student_checkhomework);
		button_coursetable = (Button) findViewById(R.id.coursetablemanage);
		button_evaluate = (Button) findViewById(R.id.courseevaluate_stu);


		//开始/停止按钮 
		mStartBtn.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {



				if (!mIsStart) {
					setLocationOption();
					mLocClient.start();
					mStartBtn.setText("停止");
					Toast.makeText(getApplicationContext(), "定位开始，请耐心等待",
							Toast.LENGTH_SHORT).show();
					mIsStart = true;

				} else {
					mLocClient.stop();
					mIsStart = false;
					mStartBtn.setText("开始");
					Toast.makeText(getApplicationContext(), "定位结束",
							Toast.LENGTH_SHORT).show();
				}
				Log.d(TAG, "... mStartBtn onClick... pid="+Process.myPid()+" count="+count++);



			}
		});

		student_signIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put("sid", sid+"");
				client.post(HttpUtil.server_student_SignIn, params,
						new JsonHttpResponseHandler(){

							@Override
							public void onSuccess(int arg0, JSONObject arg1) {
								JSONArray object = arg1.optJSONArray("result");
								Log.e(TAG, object.length()+"",null );
								if(object.length()==0){
									Toast.makeText(StudentMainActivity.this,"暂时没有需要课程需要签到!", Toast.LENGTH_SHORT).show();
								}else{
									final ArrayList<String> list=new ArrayList<String>();

									for(int i=0;i<=arg1.optJSONArray("result").length();i++){
										JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
										if(object_temp!=null&&object_temp.optString("SCId")!=null){list.add(i, object_temp.optString("SCId"));}
									}


									if(list.size()==0)
									{
										AlertDialog dialog = new AlertDialog.Builder(StudentMainActivity.this)
												.setTitle("消息").setMessage("您当前没有课程需要点到！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

													@Override
													public void onClick(DialogInterface arg0, int arg1) {
														// TODO Auto-generated method stub

													}
												})//在这里把写好的这个listview的布局加载dialog中
												.create();
										P.p("执行到磁珠了吗");
										dialog.show();
									}
									else
									{
										if(LocationData.latitude==0.0|| LocationData.longitude==0)
										{
											Toast.makeText(StudentMainActivity.this,"请先获取您的位置信息之后再尝试签到。", Toast.LENGTH_SHORT).show();
										}else
										{
											AlertDialog dialog = new AlertDialog.Builder(StudentMainActivity.this)
													.setTitle("消息").setMessage("当前有课程需要签到，您需要立即签到吗！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface arg0, int arg1) {
															// TODO Auto-generated method stub
															RequestParams params = new RequestParams();
															P.p("这里2    "+list.size());
															for(int i = 0;i<list.size();i++)
															{
																params.put(i+"", list.get(i));
																P.p("i is "+i+"   "+list.get(i));
															}
															params.put("size",list.size()+"");
															params.put("latitude", LocationData.latitude+"");
															params.put("longitude", LocationData.longitude+"");
															P.p("这里1");
															client.post(HttpUtil.server_student_SignInComfirm, params,
																	new JsonHttpResponseHandler(){
																		@Override
																		public void onSuccess(int arg0, JSONObject arg1) {
																			JSONObject object = arg1.optJSONObject("result");
																			String success = object.optString("success");
																			if(success=="您没有在课堂附近签到"){
																				Toast.makeText(StudentMainActivity.this,"您没有在课堂附近签到!", Toast.LENGTH_SHORT).show();
																			}else{
																				if(success!=null)
																				{
																					Toast.makeText(StudentMainActivity.this,"签到成功!", Toast.LENGTH_SHORT).show();
																				}else
																				{
																					Toast.makeText(StudentMainActivity.this,"签到失败!", Toast.LENGTH_SHORT).show();
																				}
																			}
																			super.onSuccess(arg0, arg1);
																		}
																	});
														}
													})//在这里把写好的这个listview的布局加载dialog中
													.setNegativeButton("取消", new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface arg0, int arg1) {
															// TODO Auto-generated method stub

														}
													}).create();
											dialog.show();

										}
									}
								}
								super.onSuccess(arg0, arg1);
							}
						});
			}
		});

		student_checkHomework.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				RequestParams params = new RequestParams();
				params.put("sid", sid+"");
				client.post(HttpUtil.server_student_StudentCourse, params,
						new JsonHttpResponseHandler(){
							@Override
							public void onSuccess(int arg0, JSONObject arg1) {
								JSONArray object = arg1.optJSONArray("result");

								if(object.length()==0){
									Toast.makeText(StudentMainActivity.this,"您没有选修任何课程!", Toast.LENGTH_SHORT).show();
								}else{
									ArrayList<String> list=new ArrayList<String>();

									for(int i=0;i<arg1.optJSONArray("result").length();i++){
										JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
										P.p(object_temp.toString()+2222);
										list.add(i, (object_temp.optInt("CNumber")+" "+object_temp.optString("CName")+"_"+object_temp.optString("CTname")));
									}

									Intent i = new Intent(StudentMainActivity.this,StudentCheckHomeworkActivity.class);

									i.putStringArrayListExtra("studentCourseInfo1", list);
									P.p(list+"");
									startActivity(i);
								}
								super.onSuccess(arg0, arg1);
							}
						});
			}
		});



		student_checkSign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RequestParams params = new RequestParams();
				params.put("sid", sid+"");
				client.post(HttpUtil.server_student_StudentCourse, params,
						new JsonHttpResponseHandler(){
							@Override
							public void onSuccess(int arg0, JSONObject arg1) {
								JSONArray object = arg1.optJSONArray("result");

								if(object.length()==0){
									Toast.makeText(StudentMainActivity.this,"您没有选修任何课程!", Toast.LENGTH_SHORT).show();
								}else{
									ArrayList<String> list=new ArrayList<String>();
									list.add(0, "课程编号"+"    "+"课程名"+"    "+" 已到次数"+"    "+"总点到次数");
									for(int i=1;i<=arg1.optJSONArray("result").length();i++){
										JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i-1);
										P.p(object_temp.toString()+2222);
										list.add(i, (object_temp.optInt("CNumber")+"    					"+object_temp.optString("CName")+"    			"+object_temp.optString("SCPointNum")+"   				 "+object_temp.optString("ScPointTotalNum")));
									}
									Intent intent = new Intent(StudentMainActivity.this,TCourseSignInActivity.class);
									intent.putStringArrayListExtra("studentCourseSignInInfo", list);
									P.p("!@#$%^&*@#$%^&*#$%^&");
									startActivity(intent);
								}
								super.onSuccess(arg0, arg1);
							}
						});




			}
		});


		button_coursetable.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StudentMainActivity.this, CoursetableActivity.class);
				Bundle bundle = new Bundle();
				//bundle.putSerializable("courseList", (Serializable) courseList);
				bundle.putInt("studentid", sid);
				intent.putExtras(bundle);
				StudentMainActivity.this.startActivity(intent);
			}

		});

		button_evaluate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(StudentMainActivity.this, EvaluateActivity.class);//
				Bundle bundle = new Bundle();
				bundle.putInt("studentid", sid);
				i.putExtras(bundle);
				StudentMainActivity.this.startActivity(i);
			}

		});

	}
//	
//	 @Override
//		protected void onResume() {
//			super.onResume();
//			// 取得最佳的定位提供者
//			Criteria criteria = new Criteria();
//			best = manager.getBestProvider(criteria, true);
//			// 更新位置频率的条件 
//	        int minTime = 5000; // 毫秒
//	        float minDistance = 5; // 公尺
//			if (best != null) { // 取得快取的最后位置,如果有的话 
//		       currentLocation = manager.getLastKnownLocation(best); 
//		       manager.requestLocationUpdates(best, minTime,
//		    		                     minDistance, listener);
//			}
//			else { // 取得快取的最后位置,如果有的话 
//	           currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
//	           manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
//	                   minTime, minDistance, listener);
//			}
//		    updatePosition(); // 更新位置 
//		}
//		@Override
//		protected void onPause() {
//			super.onPause();
//			manager.removeUpdates(listener); 
//		}
//		// 更新现在的位置 
//	    private void updatePosition() { 
//	    	TextView output;
//	    	output = (TextView) findViewById(R.id.student_location);
//	        if (currentLocation == null) { 
//	            output.setText("取得定位信息中..."); 
//	        } else { 
//	            output.setText(getLocationInfo(currentLocation)); 
//	        } 
//	    } 
//	    // 创建定位服务的监听者对象 
//	    private LocationListener listener = new LocationListener() { 
//	        @Override 
//	        public void onLocationChanged(Location location) { 
//	            currentLocation = location; 
//	            updatePosition(); 
//	        }  
//	        @Override 
//	        public void onProviderDisabled(String provider) { } 
//	        @Override 
//	        public void onProviderEnabled(String provider) { } 
//	        @Override 
//	        public void onStatusChanged(String provider, int status, Bundle extras) { } 
//	    }; 

	public void ButtonOnclick_ShareMedia(View view)
	{
		RequestParams params = new RequestParams();
//		/	params.put("sid", sid+"");
		client.post(HttpUtil.server_getMedia, params,
				new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						JSONArray object = arg1.optJSONArray("result");

						if(object.length()==0){
							Toast.makeText(StudentMainActivity.this,"当前没有任何资源共享!", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(StudentMainActivity.this,UploadAudioActivity.class);
							intent.putStringArrayListExtra("mediainfolist", new ArrayList<String>());
							startActivity(intent);

						}else{
							ArrayList<String> list=new ArrayList<String>();
							for(int i=0;i<arg1.optJSONArray("result").length();i++){
								JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
								P.p(object_temp.toString()+2222);
								list.add(i, ("    多媒体文件:"+object_temp.optString("smname")));
							}

//							Intent intent = new Intent(StudentMainActivity.this,TCourseSignInActivity.class);


							Intent intent = new Intent(StudentMainActivity.this,UploadAudioActivity.class);
							intent.putStringArrayListExtra("mediainfolist", list);
							startActivity(intent);
						}
						super.onSuccess(arg0, arg1);
					}
				});
	}


	//	    // 取得定位信息
//		public String getLocationInfo(Location location) {
//			StringBuffer str = new StringBuffer();
//			str.append("定位提供者(Provider): "+location.getProvider());
//			str.append("\n纬度(Latitude): " + Double.toString(location.getLatitude()));
//			str.append("\n经度(Longitude): " + Double.toString(location.getLongitude()));
//			str.append("\n高度(Altitude): " + Double.toString(location.getAltitude()));
//			latitude = Double.toString(location.getLatitude())+"";
//			longitude=Double.toString(location.getLongitude())+"";
//			return str.toString();
//		}
	// 启动Google地图
	public void button1_Click(View view) {
//	    	// 取得经纬度坐标
//	    	float latitude = (float) currentLocation.getLatitude();
//	    	float longitude = (float) currentLocation.getLongitude();   
//	    	// 创建URI字符串
//	    	String uri = String.format("geo:%f,%f?z=18", latitude, longitude);
//	    	// 创建Intent对象
//	    	Intent geoMap = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
//	    	startActivity(geoMap);  // 启动活动
		if(LocationData.latitude!=0.0|| LocationData.longitude!=0.0)
		{
			Intent intent =  new Intent(StudentMainActivity.this,MapActivity.class);
			intent.putExtra("latitude", LocationData.latitude);
			intent.putExtra("longitude", LocationData.longitude);
			intent.putExtra("radius", LocationData.radius);
			startActivity(intent);
		}else
		{
			Toast.makeText(StudentMainActivity.this,"您还没有定位哦，无法获得您的位置信息哟。", Toast.LENGTH_SHORT).show();
		}

	}


	//一下是百度定位


	@Override
	public void onDestroy() {
		mLocClient.stop();
		((Location)getApplication()).mTv = null;
		super.onDestroy();
	}

	//设置相关参数
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();


		option.setCoorType("bd09ll");
		option.setPoiNumber(10);
		option.disableCache(true);
		option.setPriority(LocationClientOption.NetWorkFirst);
		mLocClient.setLocOption(option);
	}

	protected boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}


	public void ButtonOnclick_classhomework(View view)
	{
		SharedPreferences  sharedata=getSharedPreferences("courseMis", 0);
		sid = Integer.parseInt(sharedata.getString("userID",null));

		RequestParams params = new RequestParams();
		params.put("sid", sid+"");
		client.post(HttpUtil.server_student_StudentCourse, params,
				new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						JSONArray object = arg1.optJSONArray("result");

						if(object.length()==0){
							Toast.makeText(StudentMainActivity.this,"您没有选修任何课程!", Toast.LENGTH_SHORT).show();
						}else{
							ArrayList<String> list=new ArrayList<String>();

							for(int i=0;i<arg1.optJSONArray("result").length();i++){
								JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
								P.p(object_temp.toString()+2222);
								list.add(i, (object_temp.optInt("CNumber")+" "+object_temp.optString("CName")+"_"+object_temp.optString("CTname")));
							}

							Intent i = new Intent(StudentMainActivity.this,StudentCheckClassHomeworkActivity.class);
							i.putExtra("sssss", "fanqq");
							i.putStringArrayListExtra("studentCourseInfo1", list);
							P.p(list+"");
							startActivity(i);
						}


						super.onSuccess(arg0, arg1);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, 1, 0, "密码修改");
		menu.add(0, 2, 0, "个人信息查看");
		//getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem mi){
		switch(mi.getItemId()){
			case 1:
				Intent i = new Intent(StudentMainActivity.this, PasswordChangeActivity.class);//
				Bundle bundle = new Bundle();
				bundle.putInt("studentid", sid);
				bundle.putString("type", "教师");
				i.putExtras(bundle);
				StudentMainActivity.this.startActivity(i);
				break;
			case 2:
				break;
		}
		return true;
	}



}
