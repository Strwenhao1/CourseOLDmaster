package com.coursemis.view.activity;

import java.util.ArrayList;

import com.coursemis.R;
import com.coursemis.util.P;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SCourseSignInActivity extends Activity{
	
	private Intent intent =null;
	private ArrayList<String> list=null;
	private ListView lv=null;
	private Button scoursesigninactivity_back=null;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		intent=getIntent();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tcoursesigninactivity);
		list=intent.getStringArrayListExtra("studentCourseSignInInfo");
		
		P.p(list.get(1)+"1111111111111111111111");
		scoursesigninactivity_back=(Button)this.findViewById(R.id.scsia_back);
		P.p(list.get(1)+"1111111111111111111112");
		if(scoursesigninactivity_back==null)
				P.p("控制啊！");
		scoursesigninactivity_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		lv = (ListView)this.findViewById(R.id.s_studentcourseSignIninfo);        
		
	        ArrayAdapter<String> a = new ArrayAdapter<String>(this,
	         	R.layout.listview_item_1, list);
	        lv.setAdapter(a);
	        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		
	}
	
	
	
	
	
	
	
	
}
