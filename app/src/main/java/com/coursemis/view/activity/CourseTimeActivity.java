package com.coursemis.view.activity;

import com.coursemis.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CourseTimeActivity extends Activity  {
	
	public Context context;
	private ListView listView_coursetimeList;
	private static String [] time_1 = {"周一","周二","周三","周四","周五","周六","周日"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_time);
		
		this.context = this;
		listView_coursetimeList = (ListView) findViewById(R.id.coursetimeList);
		listView_coursetimeList.setAdapter(new ArrayAdapter<String>(
				context,
				android.R.layout.simple_expandable_list_item_1,
				time_1));
		
		listView_coursetimeList.setOnItemClickListener(
				new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(CourseTimeActivity.this, CourseEditActivity.class);
						Bundle bundle = new Bundle(); 
						System.out.println(arg0.getItemAtPosition(arg2).toString());
						bundle.putString("time_1", arg0.getItemAtPosition(arg2).toString());
						bundle.putInt("weekday_1", arg2+1);
						intent.putExtras(bundle);
						CourseTimeActivity.this.setResult(RESULT_OK,intent);
						CourseTimeActivity.this.finish();
					}
			
		});
		
	}
}
