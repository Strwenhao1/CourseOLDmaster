package com.coursemis.view.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.model.Student;
import com.coursemis.util.DialogUtil;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.SubActivity;
import com.coursemis.view.myView.TitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class StudentCourseActivity extends Activity {
	public Context context;
	private ListView listView_studentList;
	private List<String> studentNames = new ArrayList<String>();
	private List<String> studentNums = new ArrayList<String>();
	private int courseid;
	private List<Integer> studentid_temp = new ArrayList<Integer>(); //用于传递course对应的id
	
	private List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
	
	private AsyncHttpClient client;
	private TitleView mTitleView;
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView() ;
		initData() ;
		initInternetData() ;
		initTitle() ;


	}

	private void initInternetData() {
		RequestParams params = new RequestParams();
		params.put("courseid", courseid+"");
		params.put("action", "student_course");///

		client.post(HttpUtil.server_student_course, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						// TODO Auto-generated method stub
						List<Student> studentList = new ArrayList<Student>();
						studentid_temp.clear();
						studentNames.clear();
						studentNums.clear();
						listItems.clear();
						for(int i=0;i<arg1.optJSONArray("result").length();i++){
							JSONObject object = arg1.optJSONArray("result").optJSONObject(i);
							Student student = new Student();
							student.setSId(object.optInt("SId"));
							studentid_temp.add(student.getSId());
							student.setSNum(object.optString("SNum"));
							student.setSName(object.optString("SName"));
							studentNames.add(student.getSName());
							studentNums.add(student.getSNum());
							Map<String,Object> listItem = new HashMap<String,Object>();
							listItem.put("name", student.getSName());
							listItem.put("num", student.getSNum());
							listItems.add(listItem);

							studentList.add(student);
						}

						if (mAdapter!=null){
							mAdapter.notifyDataSetChanged();
						}else {
							mAdapter = new MyAdapter(context);
							listView_studentList.setAdapter(mAdapter);
						}

						super.onSuccess(arg0, arg1);
					}

				});
	}

	private void initData() {
		this.context = this;
		client = new AsyncHttpClient();

		Intent intent = getIntent();
		courseid = intent.getExtras().getInt("courseid");
	}

	private void initTitle() {
		mTitleView.setTitle("课程学生管理");
		mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
			@Override
			public void onClick(View button) {
				StudentCourseActivity.this.finish();
			}
		});
		mTitleView.setRightButton("添加", new TitleView.OnRightButtonClickListener() {
			@Override
			public void onClick(View button) {
				// TODO Auto-generated method stub
				Intent i = new Intent(StudentCourseActivity.this,StudentOfCourseAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("courseid", courseid);
				i.putExtras(bundle);
				StudentCourseActivity.this.startActivityForResult(i, SubActivity.SUCCESS);
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_student_course);
		listView_studentList = (ListView) findViewById(R.id.studentList);
		mTitleView = (TitleView) findViewById(R.id.student_course_title);

	}

	public class MyAdapter extends BaseAdapter {  
		  
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

        //****************************************第二种方法，高手一般都用此种方法,具体原因，我还不清楚,有待研究  
	 public View getView(int position, View convertView, ViewGroup parent) {  
	       ViewHolder holder = null;  
	       StudentDelListener myListener=null;
	       
	      if (convertView == null) {  
	            
	          holder=new ViewHolder();    
	            
	          //可以理解为从vlist获取view  之后把view返回给ListView  
	           myListener=new StudentDelListener(position);
	               
	          convertView = mInflater.inflate(R.layout.adapter_student, null);    
	          holder.name = (TextView)convertView.findViewById(R.id.adapter_student_name);  
	          holder.num = (TextView)convertView.findViewById(R.id.adapter_student_num);
	          holder.viewBtn = (Button)convertView.findViewById(R.id.adapter_student_del);  
	          convertView.setTag(holder);               
	      }else {               
	          holder = (ViewHolder)convertView.getTag();  
	      }         
	        
	     
	      holder.name.setText((String)listItems.get(position).get("name"));
	      holder.num.setText((String)listItems.get(position).get("num"));
	      holder.viewBtn.setTag(position);  
	      //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉  
	      holder.viewBtn.setOnClickListener(myListener);
	    
	         
	      return convertView;  
	  }  
	}
	
	private class StudentDelListener implements OnClickListener{  
	      int mPosition;  
	      public StudentDelListener(int inPosition){  
	          mPosition= inPosition;  
	      }  
	      @Override  
	      public void onClick(View v) {  
	          // TODO Auto-generated method stub  
	    	  RequestParams params = new RequestParams();
				params.put("studentid", studentid_temp.get(mPosition)+"");
				params.put("courseid", courseid+"");
				
				client.post(HttpUtil.server_student_del, params,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int arg0, JSONObject arg1) {
								// TODO Auto-generated method stub
								String delStudent_msg = arg1.optString("result");
								//DialogUtil.showDialog(context, delStudent_msg, true);
								Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
								initInternetData();
								super.onSuccess(arg0, arg1);
							}

						});
	          /*Toast.makeText(
	        		  CourseActivity.this, "courseid_temp:"+courseid_temp.get(mPosition), Toast.LENGTH_SHORT).show();  */
	      }  
	}
	
	//提取出来方便点  
    public final class ViewHolder {  
        public TextView name;  
        public TextView num;
        public Button viewBtn;  
    } 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_course, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SubActivity.SUCCESS){
			Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
			initInternetData();
		}
	}
}
