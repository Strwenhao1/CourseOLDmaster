package com.coursemis.view.activity;

import java.util.ArrayList;
import java.util.List;

import com.coursemis.R;
import com.coursemis.util.P;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Test extends Activity {
	
	List<String> list= new ArrayList<String>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_1);
		list.add(0,"1");
		list.add(1,"2");
		list.add(2,"3");
		
		
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);		
		ListView lv = new ListView(this);
		ArrayAdapter<String> a = new ArrayAdapter<String>(this,
		         R.layout.listview_item_1, list);
		lv.setAdapter(a);
		LinearLayout listViewLayout = new LinearLayout(this);
		listViewLayout.setOrientation(LinearLayout.VERTICAL);
		listViewLayout.addView(lv,lp);
		final AlertDialog dialog = new AlertDialog.Builder(this)  
	        .setTitle("选择课程").setView(listViewLayout)//在这里把写好的这个listview的布局加载dialog中  
	       .create();  
		  P.p("执行到此处5");
	    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
			  									  
			  dialog.dismiss();									  
		  }								  
	});
	 P.p("执行到此处6");	  
	dialog.show(); 
		
	}
}
