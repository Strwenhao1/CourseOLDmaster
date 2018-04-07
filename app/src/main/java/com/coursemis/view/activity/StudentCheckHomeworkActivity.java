
package com.coursemis.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.util.HttpUtil;
import com.coursemis.util.P;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentCheckHomeworkActivity extends Activity {
	private ListView sch=null;
	Intent intent1 =null;
	Button back = null;

	int sid;
	private AsyncHttpClient client = new AsyncHttpClient();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent1 = getIntent();
		SharedPreferences  sharedata=getSharedPreferences("courseMis", 0);
		sid = Integer.parseInt(sharedata.getString("userID",null));
		setContentView(R.layout.activity_student_check_homework);
//		back=(Button)findViewById(R.id.studentcheckhomework_back);
		sch=(ListView)findViewById(R.id.studentcheckhomework_listview);

		final List<String>  courseinfol= intent1.getStringArrayListExtra("studentCourseInfo1");
		ArrayList<String> courseinfol_temp=new ArrayList<String>();
		for(int i=0;i<courseinfol.size();i++)
		{
			String temp = courseinfol.get(i);
			String cname = temp.substring(temp.indexOf(" ")+1,temp.indexOf("_"));
			String tname = temp.substring(temp.indexOf("_")+1,temp.length());
			courseinfol_temp.add(cname+"       "+tname+" 老师");
		}
		ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_checked, courseinfol_temp);
		sch.setAdapter(aaRadioButtonAdapter);
		sch.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		sch.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put("courseinfo", courseinfol.get(arg2)+"");
				params.put("sid", sid+"");
				client.post(HttpUtil.server_student_StudentCourseCheckhomework, params,
						new JsonHttpResponseHandler(){
							@Override
							public void onSuccess(int arg0, JSONObject arg1) {
								JSONArray object = arg1.optJSONArray("result");

								if(object.length()==0){
									Toast.makeText(StudentCheckHomeworkActivity.this,"这门课程没有任何作业", Toast.LENGTH_SHORT).show();
								}else{
									ArrayList<String> list=new ArrayList<String>();

									for(int i=0;i<arg1.optJSONArray("result").length();i++){
										JSONObject object_temp = arg1.optJSONArray("result").optJSONObject(i);
										P.p(object_temp.toString()+2222);
										list.add(i, (object_temp.optInt("smid")+" "+object_temp.optString("smname")+"_"+object_temp.optString("flag")));
									}

									Intent intent = new Intent(StudentCheckHomeworkActivity.this,HomeworkCourseCSubmitInfoActivity.class);
									intent.putExtra("courseinfo", courseinfol.get(arg2)+"");
									intent.putStringArrayListExtra("studentCourseHomeworkInfo", list);
									P.p("!@#$%^&*@#$%^&*#$%^&");
									startActivity(intent);
								}


								super.onSuccess(arg0, arg1);
							}
						});

			}

		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});



		for(int i=0;i<courseinfol.size();i++)
		{
			final Button btn = new Button(this);
			btn.setText(courseinfol.get(i));
			P.p(courseinfol.get(i));
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

				}

			});

		}



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_check_homework, menu);
		return true;
	}

}
