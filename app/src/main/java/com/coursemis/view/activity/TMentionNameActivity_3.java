package com.coursemis.view.activity;

import java.util.ArrayList;

import com.coursemis.R;
import com.coursemis.util.P;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TMentionNameActivity_3 extends Activity{
	
	private ArrayList<String> coursetime=null;
	Intent intent=null;
	Intent intent_temp=null;
	LinearLayout layout=null;
	ListView tmn=null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tmentionnameactivity_3);
		tmn=(ListView)findViewById(R.id.tmentionname_3);
		LinearLayout ll=(LinearLayout)findViewById(R.id.layout_4);
		ArrayList<String> coursetime_temp=new ArrayList<String>();
		intent=getIntent();
		
		coursetime=intent.getStringArrayListExtra("cousetime_list");
		
		if(coursetime==null) {P.p("aaaa");}else{
//		
//		for(int i=0;i<coursetime.size();i++)
//		{
//			final Button btn = new Button(this);
//			btn.setText(coursetime.get(i));
//			P.p(coursetime.get(i));
//			btn.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					String uriString=""+btn.getText();
//					P.p(btn.getText()+"");
//					Uri data =Uri.parse(uriString);
//					Intent result=new Intent(null,data);
//					setResult(RESULT_OK,result);
//					finish();
//					
//				}
//				
//			});
//			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//			ll.addView(btn,lp1);
//		}
		
			for(int i=0;i<coursetime.size();i++)
			{
				String temp= coursetime.get(i);
				temp=temp.substring(temp.indexOf(" ")+1,temp.length());
				String temp1=temp.substring(0,temp.indexOf(" "));
				String temp2=temp.substring(temp.indexOf(" ")+1,temp.length());
				coursetime_temp.add("开始于第"+temp1+"节	结束于"+temp2+"节");
			}
		
		
		ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_checked, coursetime_temp);
		tmn.setAdapter(aaRadioButtonAdapter);
		tmn.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		tmn.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Uri data =Uri.parse(coursetime.get(arg2));
			    Intent result=new Intent(null,data);
		        setResult(RESULT_OK,result);
			finish();
			}
		});
		
		}
	
	}
	
	
	
	
	
	
	
}
