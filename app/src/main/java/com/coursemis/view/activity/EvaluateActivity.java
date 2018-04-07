package com.coursemis.view.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.coursemis.R;
import com.coursemis.model.Course;
import com.coursemis.model.Coursetime;
import com.coursemis.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EvaluateActivity extends Activity {

	public Context					context;									// /
	private AsyncHttpClient			client;

	// SharedPreferences preferences;
	// SharedPreferences.Editor editor;

	private Button					back;
	private Button					button_continue;
	private ListView				listview_courseOfStu;

	private int						studentid;

	private int						courseid_selected;
	private List<String>			courseNames		= new ArrayList<String>();
	private List<Integer>			courseid_temp	= new ArrayList<Integer>(); // 用于传递course对应的id
	private ArrayAdapter<String>	arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate);

		this.context = this;
		client = new AsyncHttpClient();

		// preferences = getSharedPreferences("courseMis", 0);
		// editor = preferences.edit();
		//
		// studentid = preferences.getInt("studentid", 0);//0为默认值

		Intent intent = getIntent();
		studentid = intent.getExtras().getInt("studentid");

		back = (Button) findViewById(R.id.reback_btn);

		button_continue = (Button) findViewById(R.id.continue_btn);

		listview_courseOfStu = (ListView) findViewById(R.id.courseOfStudentList);

		arrayAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_single_choice, courseNames);

		listview_courseOfStu.setAdapter(arrayAdapter);
		listview_courseOfStu.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview_courseOfStu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listview_courseOfStu.setItemChecked(arg2, true);
				courseid_selected = courseid_temp.get(arg2);
			}
		});

		RequestParams params = new RequestParams();
		params.put("studentid", studentid + "");
		client.post(HttpUtil.server_course_student, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, JSONObject arg1) {

						List<Course> courseList = new ArrayList<Course>();
						List<Coursetime> coursetimeList = new ArrayList<Coursetime>();

						for (int i = 0; i < arg1.optJSONArray("result")
								.length(); i++) {
							JSONObject object = arg1.optJSONArray("result")
									.optJSONObject(i);

							Course course = new Course();
							course.setCId(object.optInt("CId"));
							course.setCName(object.optString("CName"));
							courseNames.add(course.getCName());
							courseid_temp.add(course.getCId());

							Coursetime coursetime = new Coursetime();
							coursetime.setCtWeekChoose(object
									.optInt("ctWeekChoose"));
							coursetime.setCtWeekDay(object.optInt("ctWeekDay"));
							coursetime.setCtStartClass(object
									.optInt("ctStartClass"));
							coursetime.setCtEndClass(object
									.optInt("ctEndClass"));
							coursetime.setCtAddress(object
									.optString("ctAddress"));

							courseList.add(course);
							coursetimeList.add(coursetime);
						}
						arrayAdapter.notifyDataSetChanged();
					}

				});

		// 返回按钮
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EvaluateActivity.this.finish();
			}
		});
		button_continue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EvaluateActivity.this,
						EvaluateAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("studentid", studentid);
				bundle.putInt("courseid", courseid_selected);
				Toast.makeText(context, "courseid:" + courseid_selected,
						Toast.LENGTH_SHORT).show();
				i.putExtras(bundle);
				EvaluateActivity.this.startActivity(i);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.evaluate, menu);
		return true;
	}

}
