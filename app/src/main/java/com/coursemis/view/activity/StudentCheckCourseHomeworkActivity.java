package com.coursemis.view.activity;

import java.util.List;

import com.coursemis.R;
import com.coursemis.util.P;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class StudentCheckCourseHomeworkActivity extends Activity {

	
	private Intent intent =null;
	private Button back=null;
	private String courseinfo =null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_check_course_homework);
		intent=getIntent();
		List<String> studentCourseHomeworkInfo = intent.getStringArrayListExtra("studentCourseHomeworkInfo");
		back = (Button)findViewById(R.id.studentcheckcoursehomework_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
//
		LinearLayout ll=(LinearLayout)findViewById(R.id.studentcheckcoursehomework_scch);
		for(int i=0;i<studentCourseHomeworkInfo.size();i++)
		{
			final Button btn = new Button(this);
			btn.setText(studentCourseHomeworkInfo.get(i));
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Intent intent = new Intent(HomeworkManageCourseSelectInfoActivity.this,HomeworkManageJobInfoActivity.class);
					courseinfo = (String) btn.getText(); 
					
					
				}
			});
			
			
			
			P.p("叭叭叭");
			P.p(studentCourseHomeworkInfo.get(i));
			
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ll.addView(btn,lp1);
		
		
		}
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_check_course_homework, menu);
		return true;
	}

}
