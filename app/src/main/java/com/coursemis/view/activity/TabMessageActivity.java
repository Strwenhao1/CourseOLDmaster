package com.coursemis.view.activity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Teacher;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabMessageActivity extends Activity {
	public Context context;///
    private AsyncHttpClient client;
    
    SharedPreferences preferences;
	SharedPreferences.Editor editor;
    
	 //声明TabHost对象
    //private TabHost tabhost;
    private ListView listView;  
    private ListView listView_courseList;
    
    Course course[];
    private int Number=7;
    private String[] student_Info=new String[Number];
    private String[] teacher_Information = new String[7];//=new String[Number];
    
    private List<String> courseNames = new ArrayList<String>();
	private List<Integer> courseid_temp = new ArrayList<Integer>(); //用于传递course对应的id
	
	private int teacherid;

    private final static int Student=0;
    private final static int Teacher=1;
	private TitleView mTitleView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  HiddenMenu(); //隐藏标题栏
        
        setContentView(R.layout.activity_tab_message);
        //获取TabHost对象
        //tabhost=getTabHost();
        /*
         * 为TabHost添加标签
         * 新建一个标签newTabSpec
         * 设置其标签和图标
         * 设置内容
         */
        init();

		initTitle() ;
        
        /*tabhost.setOnTabChangedListener(new OnTabChangeListener() {
        	
		   @Override
		   public void onTabChanged(String tabId) {
		    if(tabId.equals("1")){
		    	Mail_list(Teacher);
		    }
		    
		   }
		  });    */
   }

	private void initTitle() {
		mTitleView = (TitleView) findViewById(R.id.tab_message_title);
		mTitleView.setTitle("短信管理");
		mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
			@Override
			public void onClick(View button) {
				TabMessageActivity.this.finish();
			}
		});

	}


	/*******/
    //初始化
	void init()
    {
		
   	 	Loading_data();
   	 	
   	    /*tabhost.addTab(tabhost.newTabSpec("1")
   	          .setIndicator("教师",getResources().getDrawable(android.R.drawable.alert_light_frame))
   	          .setContent(R.id.teacher));
   	    */
   	    load();		//读取老师所有的课程
    
    }
	
	
	public void load() {

		 client  = new AsyncHttpClient();
			
		 RequestParams params = new RequestParams();
		 params.put("teacherid", teacherid+"");
		 params.put("action", "course_teacher");///
			
		 client.post(HttpUtil.server_course_teacher, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, JSONObject arg1) {
							List<Course> courseList = new ArrayList<Course>();
							for(int i=0;i<arg1.optJSONArray("result").length();i++){
								JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
								
								Course course = new Course();
								course.setCId(object.optInt("CId"));
								courseid_temp.add(course.getCId());
								course.setCName(object.optString("CName").toString());
								courseNames.add(course.getCName());
								course.setCNum(object.optString("CNum"));
								
								//Teacher
								Teacher teacher = new Teacher();
								teacher.setTId(object.optJSONObject("teacher").optInt("TId"));
								
								course.setCFlag(object.optBoolean("CFlag"));
								course.setCPointTotalNum(object.optInt("CPointTotalNum"));
								courseList.add(course);
							}
							
							teacher_Information = new String[courseList.size()];
							
							course = new Course[courseList.size()];
							
							for(int i=0;i<courseList.size();i++)
							{
								course[i]= new Course();
								course[i]=(Course)courseList.get(i);
							}
							
							Mail_list(Teacher);
							
							super.onSuccess(arg0, arg1);
						}

					});

	}
	
	//列表
    void Mail_list(int person)  
    {
    	 if(person==Teacher)
    	{
    		//创建老师的
    		
    		listView = (ListView) findViewById(R.id.teacher); 
    		
    		for(int i=0;i<course.length;i++){
    			teacher_Information[i]=course[i].getCName();
	    	}
    		  //创建一个ArrayAdapter  
    		listView.setAdapter(new ArrayAdapter<String>(this,
					R.layout.text_list_item_1, teacher_Information));
  	         //listView注册一个元素点击事件监听器  
  	        listView.setOnItemClickListener(
  	        		new AdapterView.OnItemClickListener() {  
  	         @Override  
  	         //跳转到短信发送界面
  	        public void onItemClick(AdapterView<?> arg0, 
  	        		View arg1, int arg2,long arg3) {
  	        	 servicer(course[arg2]);//将所选择的课程传递到servicer中
  	            }  
  	         }); 

    	}
    }
    
    
    void servicer(Course courses)
    {
    	RequestParams params = new RequestParams();
		params.put("teacherid", teacherid+"");
		params.put("action", "class_phone_number");
		params.put("courseid", courses.getCId()+"");
		
		// TODO Auto-generated method stub
		Intent intent = new Intent(TabMessageActivity.this, MessageInfoActivity.class);
		Bundle bundle = new Bundle(); 
		bundle.putInt("teacherid", teacherid);//老师的id
		bundle.putString("ClassName", courses.getCName());//班级的名称
		bundle.putString("CName", courses.getCName());//多选择的课程名
		//bundle.putString("CID", courses.getCId()+"");//多选择的课程id
		bundle.putInt("courseid", courses.getCId());//多选择的课程id
		
		intent.putExtras(bundle);
		TabMessageActivity.this.startActivity(intent);//跳转到message界面
		/****/
    }
    

    private void HiddenMenu()
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
    
    
    
	
    void Loading_data()
    {
    	
    	this.context = this;
		client = new AsyncHttpClient();
		
		preferences = getSharedPreferences("courseMis", 0);
		editor = preferences.edit();
		
		teacherid = preferences.getInt("teacherid", 0);//0为默认值
		
		/*Intent intent = getIntent();
		teacherid = intent.getExtras().getInt("teacherid");*/
    }

}