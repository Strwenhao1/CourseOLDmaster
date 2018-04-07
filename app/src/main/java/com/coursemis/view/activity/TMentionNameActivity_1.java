package com.coursemis.view.activity;

import java.util.ArrayList;

import com.coursemis.R;
import com.coursemis.util.P;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TMentionNameActivity_1 extends Activity{
	
	private ArrayList<String> courseInfo=null;
	Intent intent=null;
	Intent intent_temp=null;
	LinearLayout layout=null;
	ListView tmn=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tmentionnameactivity_1);
		LinearLayout ll=(LinearLayout)findViewById(R.id.layout_1);
		tmn=(ListView)findViewById(R.id.tmentionname_1);
		intent=getIntent();
		courseInfo=intent.getStringArrayListExtra("courselist");
		ArrayList<String> courseInfo_temp=new ArrayList<String>();
		for(int i=0;i<courseInfo.size();i++)
		{
			String temp=courseInfo.get(i);
			courseInfo_temp.add("课程名：		"+temp.substring(temp.indexOf(" ")+1, temp.length()));
		}

		
		ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_checked, courseInfo_temp);
		tmn.setAdapter(aaRadioButtonAdapter);
		tmn.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		tmn.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Uri data =Uri.parse(courseInfo.get(arg2));
			    Intent result=new Intent(null,data);
		        setResult(RESULT_OK,result);
			    finish();
			}
		});
		
	}
	
}
