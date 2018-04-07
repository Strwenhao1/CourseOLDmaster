package com.coursemis.view.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.adapter.CourseAdapter;
import com.coursemis.model.Course;
import com.coursemis.model.Teacher;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.SubActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseActivity extends Activity {
	public Context context;///
	
	//private Button back;
	//private TextView top_title;
	//private Button button_add;
	
	private List<String> courseNames = new ArrayList<String>();
	private int teacherid;
	private List<Integer> courseid_temp = new ArrayList<Integer>(); //用于传递course对应的id
	
	private List<Integer> courseid_empty_student = new ArrayList<Integer>(); //用于传递course对应的id
	
	private AsyncHttpClient client;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	private List<Map<String, Object>> listItems;
	private TitleView mTitleView;
	private RecyclerView mListView;
	private CourseAdapter mCourseAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView() ;
		initData() ;
		initTitle() ;
		initList() ;


		/*Intent intent = getIntent();
		teacherid = intent.getExtras().getInt("teacherid");*/
		
		
		//返回按钮
				/*back=(Button)findViewById(R.id.reback_btn);
				
				back.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CourseActivity.this.finish();
					}
					
				});
				*/
				/*top_title = (TextView)findViewById(R.id.title);
				top_title.setText("         课程管理            ");
		*/
		//button_add = (Button) findViewById(R.id.add_btn);
		

		/*button_add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CourseActivity.this,CourseAddActivity.class);
				Bundle bundle = new Bundle(); 
				bundle.putInt("teacherid", teacherid);
				i.putExtras(bundle);
				CourseActivity.this.startActivity(i);
			}
			
		});
		*/
		
	}

	private void initTitle() {
		mTitleView.setTitle("课程管理");
		mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
			@Override
			public void onClick(View button) {
				CourseActivity.this.finish();
			}
		});
		mTitleView.setRightButton("添加", new TitleView.OnRightButtonClickListener() {
			@Override
			public void onClick(View button) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CourseActivity.this,CourseAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("teacherid", teacherid);
				i.putExtras(bundle);
				CourseActivity.this.startActivityForResult(i, SubActivity.SUCCESS);
				//CourseActivity.this.startActivity(i);
			}
		});
	}

	public void initList() {
		getInternetData() ;
		RequestParams params = new RequestParams();
		params.put("teacherid", teacherid+"");
		params.put("action", "course_teacher");///

		client.post(HttpUtil.server_course_teacher, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						// TODO Auto-generated method stub

						List<Course> courseList = new ArrayList<Course>();
						for(int i=0;i<arg1.optJSONArray("result").length();i++){
							JSONObject object = arg1.optJSONArray("result").optJSONObject(i);

							Course course = new Course();
							course.setCId(object.optInt("CId"));
							courseid_temp.add(course.getCId());
							course.setCName(object.optString("CName").toString());
							courseNames.add(course.getCName());
							course.setCNum(object.optString("CNum"));

							//某课程学生数为0则加入List，用于广播显示课程学生为0的课程
							if(object.optBoolean("Flag_Empty_Student")){
								courseid_empty_student.add(object.optInt("CId"));
							}


							//Teacher
							Teacher teacher = new Teacher();
							teacher.setTId(object.optJSONObject("teacher").optInt("TId"));

							course.setCFlag(object.optBoolean("CFlag"));
							course.setCPointTotalNum(object.optInt("CPointTotalNum"));
							courseList.add(course);
						}

						//initTeamsSequence();
						//设置列表的数据
						setListData(courseList) ;
						//如果学生数为0的课程存在，就进行广播
						if(courseid_empty_student.size()>0){
							broadcast();
						}

						super.onSuccess(arg0, arg1);
					}

				});

	}

	private void getInternetData() {
		RequestParams params = new RequestParams();
		params.put("teacherid", teacherid+"");
		params.put("action", "course_teacher");///

		client.post(HttpUtil.server_course_teacher, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						// TODO Auto-generated method stub

						List<Course> courseList = new ArrayList<Course>();
						for(int i=0;i<arg1.optJSONArray("result").length();i++){
							JSONObject object = arg1.optJSONArray("result").optJSONObject(i);

							Course course = new Course();
							course.setCId(object.optInt("CId"));
							courseid_temp.add(course.getCId());
							course.setCName(object.optString("CName").toString());
							courseNames.add(course.getCName());
							course.setCNum(object.optString("CNum"));

							//某课程学生数为0则加入List，用于广播显示课程学生为0的课程
							if(object.optBoolean("Flag_Empty_Student")){
								courseid_empty_student.add(object.optInt("CId"));
							}


							//Teacher
							Teacher teacher = new Teacher();
							teacher.setTId(object.optJSONObject("teacher").optInt("TId"));

							course.setCFlag(object.optBoolean("CFlag"));
							course.setCPointTotalNum(object.optInt("CPointTotalNum"));
							courseList.add(course);
						}

						//initTeamsSequence();
						//设置列表的数据
						setListData(courseList) ;
						//如果学生数为0的课程存在，就进行广播
						if(courseid_empty_student.size()>0){
							broadcast();
						}

						super.onSuccess(arg0, arg1);
					}

				});

	}

	private void setListData(List<Course> courseList) {
		mListView.setLayoutManager(new LinearLayoutManager(CourseActivity.this));
		mCourseAdapter = new CourseAdapter(CourseActivity.this,courseList,teacherid);
		mListView.setAdapter(mCourseAdapter);
	}

	private void initData() {
		this.context = this;
		client = new AsyncHttpClient();
		preferences = getSharedPreferences("courseMis", 0);
		editor = preferences.edit();
		teacherid = preferences.getInt("teacherid", 0);//0为默认值
	}

	private void initView() {
		setContentView(R.layout.activity_course);
		mTitleView = (TitleView) findViewById(R.id.course_title);
		mListView = (RecyclerView) findViewById(R.id.course_list);
	}


	/*public class MyAdapter extends BaseAdapter {
		  
        private LayoutInflater mInflater;  
  
        public MyAdapter(Context context) {  
            this.mInflater = LayoutInflater.from(context);  
        }  
  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return listItems.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return 0;  
        }  

        /*//****************************************第二种方法，高手一般都用此种方法,具体原因，我还不清楚,有待研究
	 public View getView(int position, View convertView, ViewGroup parent) {
	       ViewHolder holder = null;  
	       MyListener myListener=null;
	       MyListener2 myListener2=null;
	      if (convertView == null) {  
	            
	          holder=new ViewHolder();    
	            
	          //可以理解为从vlist获取view  之后把view返回给ListView  
	           myListener=new MyListener(position);
	           myListener2=new MyListener2(position);
	               
	          convertView = mInflater.inflate(R.layout.adapter_course, null);    
	          holder.name = (TextView)convertView.findViewById(R.id.adapter_course_name);  
	          holder.detail = (Button)convertView.findViewById(R.id.adapter_course_detail);
	          holder.viewBtn = (Button)convertView.findViewById(R.id.adapter_course_del);  
	          convertView.setTag(holder);               
	      }else {               
	          holder = (ViewHolder)convertView.getTag();  
	      }         
	        
	     
	      holder.name.setText((String)listItems.get(position).get("coursename"));  
	      holder.viewBtn.setTag(position);  
	      //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉  
	      holder.viewBtn.setOnClickListener(myListener);
	    		 
	      
	      holder.detail.setTag(position);  
	      //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉  
	      holder.detail.setOnClickListener(myListener2); 
	         
	      return convertView;  
	  }  
	}  */

	private class MyListener implements OnClickListener{  
	      int mPosition;  
	      public MyListener(int inPosition){  
	          mPosition= inPosition;  
	      }  
	      @Override  
	      public void onClick(View v) {  
	          // TODO Auto-generated method stub  
	    	  //DialogUtil.decideDialog(context, "确定删除该课程吗", true);
	    	  
	    	  AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("确定删除该课程吗").setCancelable(false);
				builder.setPositiveButton("确定", 
						new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						RequestParams params = new RequestParams();
				    	  params.put("courseid", courseid_temp.get(mPosition)+"");
				    	  params.put("teacherid", teacherid+"");
				    	  client.post(HttpUtil.server_course_del, params,
				    			  new JsonHttpResponseHandler() {
				    		  @Override
				    		  public void onSuccess(int arg0, JSONObject arg1) {
				    			  // TODO Auto-generated method stub
				    			  String delCourse_msg = arg1.optString("result");
				    			  DialogUtil.showDialog(context, delCourse_msg, true);
				    			  super.onSuccess(arg0, arg1);
				    		  }
				    	  });
					}
				});
				builder.setNegativeButton("取消", 
						new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}

				});
				
				builder.create().show();
	    	  
	    	  
	      }  
	}
	
	private class MyListener2 implements OnClickListener{  
	      int mPosition;  
	      public MyListener2(int inPosition){  
	          mPosition= inPosition;  
	      }  
	      @Override  
	      public void onClick(View v) {  
	          // TODO Auto-generated method stub  
	    	  Intent intent = new Intent(CourseActivity.this, CourseInfoActivity.class);
				Bundle bundle = new Bundle(); 
				bundle.putInt("courseid", courseid_temp.get(mPosition));
				// 将课程ID作为额外参数传过去
				intent.putExtras(bundle);
				
				startActivity(intent); 
	      }  
//	       
	}
	
	//提取出来方便点  
    public final class ViewHolder {  
        public TextView name;  
        public Button detail;
        public Button viewBtn;  
    }  

	/*private void initTeamsSequence() {

		ListView view = (ListView) super.findViewById(R.id.courseList);
		 

		listItems = new ArrayList<Map<String, Object>>();
		
		for(int i = 0; i < courseNames.size(); i ++){
			Map<String,Object> listItem = new HashMap<String,Object>();
			listItem.put("coursename", courseNames.get(i));
			listItems.add(listItem);
		}
		MyAdapter adapter = new MyAdapter(this); 
        view.setAdapter(adapter);
	}*/
	
	
	//显示一条广播
	void broadcast(){
     // 创建Intent对象
		Intent intent = new Intent();
		// 设置Intent的Action属性
		intent.setAction("com.coursemis.action.BROAD_ACTION");
		intent.putExtra("msg" , "您有一些课程还没有学生！！！");
		// 发送广播
		sendBroadcast(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SubActivity.SUCCESS){
			//成功
			Toast.makeText(context,"返回成功",Toast.LENGTH_SHORT).show();
			//mCourseAdapter.refreshData();
			initList();
		}
	}
}
